package RandevuApp.domain.auth.service.impl;

import RandevuApp.domain.auth.dto.*;
import RandevuApp.domain.auth.model.RefreshToken;
import RandevuApp.domain.auth.model.SecurityUser;
import RandevuApp.domain.auth.repository.RefreshTokenRepository;
import RandevuApp.domain.auth.service.IAuthService;
import RandevuApp.domain.auth.service.TokenBlacklistService;
import RandevuApp.domain.notification.model.NotificationChannel;
import RandevuApp.domain.user.model.Role;
import RandevuApp.domain.user.model.User;
import RandevuApp.domain.user.model.UserStatus;
import RandevuApp.domain.user.repository.UserRepository;
import RandevuApp.domain.verification.model.VerificationPurpose;
import RandevuApp.domain.verification.model.VerificationRequest;
import RandevuApp.domain.verification.model.VerificationType;
import RandevuApp.domain.verification.service.VerificationService;
import RandevuApp.exceptions.auth.ExpiredTokenException;
import RandevuApp.exceptions.auth.InvalidCredentialsException;
import RandevuApp.exceptions.auth.InvalidTokenException;
import RandevuApp.exceptions.auth.UserBannedException;
import RandevuApp.exceptions.client.ConflictException;
import com.authcore.service.JwtService;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthServiceImpl implements IAuthService {

    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$"
    );
    private static final Pattern PHONE_PATTERN = Pattern.compile(
            "^\\+?[0-9. ()-]{7,25}$"
    );

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final VerificationService verificationService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final TokenBlacklistService blacklistService;

    @Value("${auth-core.refresh-token-expiration-ms}")
    private Long refreshTokenDurationMs;

    @Value("${auth-core.expiration-ms}")
    private Long accessTokenDurationMs;

    @Value("${app.security.phone-verification-validity-days}")
    private long phoneVerificationValidityDays;

    @Override
    @Transactional
    public void register(RegisterRequest request) {
        log.info("Registering new user with email: {}", request.getEmail());

        // 1. Check if email or phone already exists
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new ConflictException("Email already in use");
        }
        if (userRepository.existsByPhoneNumber(request.getPhoneNumber())) {
            throw new ConflictException("Phone number already in use");
        }

        // 2. Create User
        User user = User.builder()
                .email(request.getEmail())
                .phoneNumber(request.getPhoneNumber())
                .password(passwordEncoder.encode(request.getPassword()))
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .address(request.getAddress())
                .gender(request.getGender())
                .roles(Set.of(Role.USER)) // Default role
                .status(UserStatus.PENDING) // Initial status
                .build();

        userRepository.save(user);

        // 3. Start Verification (SMS)
        // Maybe verification starting could be in frontend
        VerificationRequest verificationRequest = new VerificationRequest(
                user.getId(),
                VerificationType.CODE,
                NotificationChannel.SMS,
                VerificationPurpose.REGISTRATION,
                null
        );

        verificationService.startVerification(verificationRequest);
        
        log.info("User registered successfully. Verification SMS sent to: {}", request.getPhoneNumber());
    }

    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        log.info("Login request received for identifier: {}", loginRequest.getIdentifier());

        // 1. Find User (Email or Phone)
        User user = findUserByIdentifier(loginRequest.getIdentifier());

        // 2. Check Password
        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException("Invalid email/phone or password");
        }

        // 3. Check Status
        if (user.getStatus() == UserStatus.BANNED) {
            throw new UserBannedException("Your account has been banned. Please contact support.");
        }

        // 4. Generate Tokens
        SecurityUser securityUser = new SecurityUser(user);
        
        String accessToken = jwtService.generateToken(generateUserClaims(securityUser), securityUser);
        RefreshToken savedRefreshToken = refreshTokenRepository.save(createRefreshToken(user));

        log.info("User logged in successfully: {}", user.getId());

        return LoginResponse.builder()
                .accessToken(accessToken)
                .refreshToken(savedRefreshToken.getRefreshToken())
                .accessTokenExpiresIn(accessTokenDurationMs)
                .refreshTokenExpiresIn(refreshTokenDurationMs)
                .build();
    }

    private boolean isValidRefreshToken(Instant expiredDate) {
        return expiredDate.isAfter(Instant.now());
    }

    @Override
    public RefreshResponse refresh(RefreshRequest refreshRequest) {
        Optional<RefreshToken> optRefreshToken = refreshTokenRepository.findByRefreshToken(refreshRequest.getRefreshToken());

        if (optRefreshToken.isEmpty()){
            throw  new InvalidTokenException("Refresh token not found: " + refreshRequest.getRefreshToken());
        }
        if (!isValidRefreshToken(optRefreshToken.get().getExpiredDate())){
            refreshTokenRepository.delete(optRefreshToken.get()); // Expired token should be deleted
            throw new ExpiredTokenException("Refresh token has expired: " + refreshRequest.getRefreshToken());
        }

        // Generate new access token
        User user = optRefreshToken.get().getUser();
        SecurityUser securityUser = new SecurityUser(user);
        
        String accessToken = jwtService.generateToken(generateUserClaims(securityUser), securityUser);

        // Delete old refresh token and create a new one
        refreshTokenRepository.delete(optRefreshToken.get());
        RefreshToken newRefreshToken = refreshTokenRepository.save(createRefreshToken(user));
        
        return RefreshResponse.builder()
                .accessToken(accessToken)
                .refreshToken(newRefreshToken.getRefreshToken())
                .accessTokenExpiresIn(accessTokenDurationMs)
                .refreshTokenExpiresIn(refreshTokenDurationMs)
                .build();
    }

    @Override
    @Transactional
    public String logout(String token) {

        long expirationMillis = jwtService.getRemainingExpirationMillis(token);

        refreshTokenRepository.deleteByUserId(Long.parseLong(jwtService.extractIdentifier(token)));

        blacklistService.blacklistToken(jwtService.extractClaim(token,Claims::getId), expirationMillis);

        return "User logout successful";
    }

    @Override
    @Transactional
    public void resendVerification(String identifier) {
        log.info("Resend verification request received for: {}", identifier);

        User user = userDomainService.findUserByIdentifier(identifier);

        if (user.getStatus() != UserStatus.PENDING) {
            throw new InvalidInputException("User is already active or banned. Cannot resend registration verification.");
        }

        if(user.getPhoneVerificationStatus(phoneVerificationValidityDays) != VerificationStatus.NOT_VERIFIED){
            throw new InvalidInputException("User phone is already verified.");        }

        // Start Verification (SMS)
        VerificationRequest verificationRequest = new VerificationRequest(
                user.getId(),
                VerificationType.CODE,
                NotificationChannel.SMS,
                VerificationPurpose.PHONE_VERIFICATION,
                null
        );

        verificationService.startVerification(verificationRequest);
        log.info("Verification SMS resent to: {}", user.getPhoneNumber());
    }

    private Map<String, Object> generateUserClaims(SecurityUser securityUser) {
        Map<String, Object> claims = new HashMap<>();

        claims.put("roles", securityUser.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList()));

        return claims;
    }

    private User findUserByIdentifier(String identifier) {
        if (isValidEmail(identifier)) {
            return userRepository.findByEmail(identifier)
                    .orElseThrow(() -> new InvalidCredentialsException("Invalid email/phone or password"));
        } else if (isValidPhoneNumber(identifier)) {
            return userRepository.findByPhoneNumber(identifier)
                    .orElseThrow(() -> new InvalidCredentialsException("Invalid email/phone or password"));
        } else {
            throw new InvalidCredentialsException("Invalid email/phone format");
        }
    }

    private boolean isValidEmail(String email) {
        if (email == null) return false;
        return EMAIL_PATTERN.matcher(email).matches();
    }

    private boolean isValidPhoneNumber(String phone) {
        if (phone == null) return false;
        return PHONE_PATTERN.matcher(phone).matches();
    }

    private RefreshToken createRefreshToken(User user) {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setExpiredDate(Instant.now().plusMillis(refreshTokenDurationMs));
        refreshToken.setRefreshToken(UUID.randomUUID().toString());
        refreshToken.setUser(user);
        return refreshToken;
    }
}

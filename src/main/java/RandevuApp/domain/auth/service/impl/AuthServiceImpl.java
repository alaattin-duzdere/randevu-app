package RandevuApp.domain.auth.service.impl;

import RandevuApp.domain.auth.dto.*;
import RandevuApp.domain.auth.model.RefreshToken;
import RandevuApp.domain.auth.model.SecurityUser;
import RandevuApp.domain.auth.repository.RefreshTokenRepository;
import RandevuApp.domain.auth.service.IAuthService;
import RandevuApp.domain.auth.service.TokenBlacklistService;
import RandevuApp.domain.notification.model.NotificationChannel;
import RandevuApp.domain.user.model.User;
import RandevuApp.domain.user.model.UserStatus;
import RandevuApp.domain.user.model.VerificationStatus;
import RandevuApp.domain.user.service.IUserDomainService;
import RandevuApp.domain.verification.model.VerificationPurpose;
import RandevuApp.domain.verification.model.VerificationRequest;
import RandevuApp.domain.verification.model.VerificationType;
import RandevuApp.domain.verification.service.VerificationService;
import RandevuApp.exceptions.auth.ExpiredTokenException;
import RandevuApp.exceptions.auth.InvalidCredentialsException;
import RandevuApp.exceptions.auth.InvalidTokenException;
import RandevuApp.exceptions.auth.UserBannedException;
import RandevuApp.exceptions.client.InvalidInputException;
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
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthServiceImpl implements IAuthService {

    private final IUserDomainService userDomainService;
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

        // 1. Create and save User via UserDomainService
        User user = userDomainService.createNewUser(request, passwordEncoder.encode(request.getPassword()));
        user = userDomainService.saveUser(user);

        // 2. Start Verification (SMS)
        VerificationRequest verificationRequest = new VerificationRequest(
                user.getId(),
                VerificationType.CODE,
                NotificationChannel.SMS,
                VerificationPurpose.PHONE_VERIFICATION,
                null
        );

        verificationService.startVerification(verificationRequest);

        log.info("User registered successfully. Verification SMS sent to: {}", request.getPhoneNumber());
    }

    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        log.info("Login request received for identifier: {}", loginRequest.getIdentifier());

        // 1. Find User (Email or Phone)
        User user = userDomainService.findUserByIdentifier(loginRequest.getIdentifier());

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

    private RefreshToken createRefreshToken(User user) {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setExpiredDate(Instant.now().plusMillis(refreshTokenDurationMs));
        refreshToken.setRefreshToken(UUID.randomUUID().toString());
        refreshToken.setUser(user);
        return refreshToken;
    }
}

package RandevuApp.domain.auth.service.impl;

import RandevuApp.commons.util.ContactFormatUtil;
import RandevuApp.config.SecurityProperties;
import RandevuApp.domain.auth.dto.*;
import RandevuApp.domain.auth.model.PendingUser;
import RandevuApp.domain.auth.model.RefreshToken;
import RandevuApp.domain.auth.model.SecurityUser;
import RandevuApp.domain.auth.repository.PendingUserRepository;
import RandevuApp.domain.auth.repository.RefreshTokenRepository;
import RandevuApp.domain.auth.service.IAuthService;
import RandevuApp.domain.auth.service.TokenBlacklistService;
import RandevuApp.domain.notification.model.NotificationChannel;
import RandevuApp.domain.user.model.User;
import RandevuApp.domain.user.model.UserStatus;
import RandevuApp.domain.user.model.VerificationStatus;
import RandevuApp.domain.user.service.IUserDomainService;
import RandevuApp.domain.user.service.param.CreateUserParams;
import RandevuApp.domain.verification.model.VerificationPurpose;
import RandevuApp.domain.verification.model.VerificationRequest;
import RandevuApp.domain.verification.model.VerificationType;
import RandevuApp.domain.verification.service.VerificationService;
import RandevuApp.exceptions.auth.ExpiredTokenException;
import RandevuApp.exceptions.auth.InvalidCredentialsException;
import RandevuApp.exceptions.auth.InvalidTokenException;
import RandevuApp.exceptions.auth.UserBannedException;
import RandevuApp.exceptions.client.ConflictException;
import RandevuApp.exceptions.client.InvalidInputException;
import RandevuApp.exceptions.client.ResourceNotFoundException;
import com.authcore.property.AuthProperties;
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
    private final PendingUserRepository pendingUserRepository;
    private final SecurityProperties securityProperties;
    private final AuthProperties authProperties;

    @Value("${app.auth.pending-user.ttl-minutes:15}")
    private long pendingUserTtlMinutes;

    @Override
    @Transactional
    public void register(RegisterRequest request) {

        if (userDomainService.existsByVerifiedEmail(request.email())) {
            throw new ConflictException("Email already in use by a verified account.");
        }
        if (userDomainService.existsByPhoneNumber(request.phoneNumber())) {
            throw new ConflictException("Phone number already in use");
        }

        PendingUser pendingUser = PendingUser.builder()
                .phoneNumber(request.phoneNumber())
                .firstName(request.firstName())
                .lastName(request.lastName())
                .email(request.email())
                .gender(request.gender())
                .password(passwordEncoder.encode(request.password()))
                .ttl(pendingUserTtlMinutes)
                .build();

        pendingUserRepository.save(pendingUser);

        VerificationRequest verificationRequest = new VerificationRequest(
                null,
                VerificationType.CODE,
                NotificationChannel.SMS,
                VerificationPurpose.USER_REGISTRATION,
                request.phoneNumber()
        );

        verificationService.startVerification(verificationRequest,request.phoneNumber());

        log.info("Pending registration created and SMS sent for: {}", request.phoneNumber());
    }

    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        String identifier = loginRequest.identifier();

        log.info("Login request received for phoneNumber: {}", identifier);

        User user = userDomainService.findUserByIdentifier(identifier);

        if (ContactFormatUtil.isEmail(identifier)) {
            VerificationStatus emailStatus = user.getEmailVerificationStatus(securityProperties.getEmailVerificationValidityDays());
            if (emailStatus != VerificationStatus.VERIFIED) {
                // throw smth here
            }
        }

        if (!passwordEncoder.matches(loginRequest.password(), user.getPassword())) {
            throw new InvalidCredentialsException("Invalid email/phone or password");
        }

        if (user.getStatus() == UserStatus.BANNED) {
            throw new UserBannedException("Your account has been banned. Please contact support.");
        }

        SecurityUser securityUser = new SecurityUser(user);

        String accessToken = jwtService.generateToken(generateUserClaims(securityUser), securityUser);
        RefreshToken savedRefreshToken = refreshTokenRepository.save(createRefreshToken(user));

        log.info("User logged in successfully: {}", user.getId());

        return LoginResponse.builder()
                .accessToken(accessToken)
                .refreshToken(savedRefreshToken.getRefreshToken())
                .accessTokenExpiresIn(authProperties.getExpirationMs())
                .refreshTokenExpiresIn(authProperties.getRefreshExpirationMs())
                .build();
    }

    private boolean isValidRefreshToken(Instant expiredDate) {
        return expiredDate.isAfter(Instant.now());
    }

    @Override
    public RefreshResponse refresh(RefreshRequest refreshRequest) {
        String refreshToken = refreshRequest.refreshToken();

        Optional<RefreshToken> optRefreshToken = refreshTokenRepository.findByRefreshToken(refreshToken);

        if (optRefreshToken.isEmpty()){
            throw  new InvalidTokenException("Refresh token not found: " + refreshToken);
        }
        if (!isValidRefreshToken(optRefreshToken.get().getExpiredDate())){
            refreshTokenRepository.delete(optRefreshToken.get()); // Expired token should be deleted
            throw new ExpiredTokenException("Refresh token has expired: " + refreshToken);
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
                .accessTokenExpiresIn(authProperties.getExpirationMs())
                .refreshTokenExpiresIn(authProperties.getRefreshExpirationMs())
                .build();
    }

    @Override
    @Transactional
    public void logout(String token) {

        long expirationMillis = jwtService.getRemainingExpirationMillis(token);

        refreshTokenRepository.deleteByUserId(Long.parseLong(jwtService.extractIdentifier(token)));

        blacklistService.blacklistToken(jwtService.extractClaim(token,Claims::getId), expirationMillis);
    }

    @Override
    @Transactional
    public void resendVerificationForPhone(ResendVerificationRequest request) {
        log.info("Resend verification request received for: {}", request.phoneNumber());
        String phoneNumber = request.phoneNumber();

        PendingUser pendingUser = pendingUserRepository.findById(phoneNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Pending user", "phone", phoneNumber));

        VerificationRequest verificationRequest = new VerificationRequest(
                null,
                VerificationType.CODE,
                NotificationChannel.SMS,
                VerificationPurpose.PHONE_VERIFICATION,
                pendingUser.getPhoneNumber()
        );

        verificationService.startVerification(verificationRequest, pendingUser.getPhoneNumber());
        log.info("Verification SMS resent to: {}", pendingUser.getPhoneNumber());
    }

    @Override
    @Transactional
    public void completePendingRegistration(String phoneNumber) {
        log.info("Completing pending registration for phone: {}", phoneNumber);

        PendingUser pendingUser = pendingUserRepository.findById(phoneNumber)
                .orElseThrow(() -> new InvalidInputException(
                        "Pending registration not found or expired for phone: " + phoneNumber));

        CreateUserParams params = new CreateUserParams(
                pendingUser.getEmail(),
                pendingUser.getPhoneNumber(),
                pendingUser.getFirstName(),
                pendingUser.getLastName(),
                pendingUser.getGender()
        );

        User newUser = userDomainService.createNewUser(params, pendingUser.getPassword());
        newUser.setPhoneVerifiedAt(Instant.now());
        newUser.setStatus(UserStatus.ACTIVE);

        userDomainService.saveUser(newUser);
        pendingUserRepository.delete(pendingUser);

        log.info("User created successfully from pending registration. Phone: {}, UserId: {}", phoneNumber, newUser.getId());
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
        refreshToken.setExpiredDate(Instant.now().plusMillis(authProperties.getRefreshExpirationMs()));
        refreshToken.setRefreshToken(UUID.randomUUID().toString());
        refreshToken.setUser(user);
        return refreshToken;
    }
}

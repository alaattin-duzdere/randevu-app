package RandevuApp.domain.auth.service.impl;

import RandevuApp.config.AuthPasswordProperties;
import RandevuApp.domain.auth.dto.ResetPasswordRequest;
import RandevuApp.domain.auth.dto.VerifyOtpRequest;
import RandevuApp.domain.auth.model.PasswordResetToken;
import RandevuApp.domain.auth.repository.PasswordResetTokenRepository;
import RandevuApp.domain.auth.service.IAuthPasswordService;
import RandevuApp.domain.notification.model.NotificationCategory;
import RandevuApp.domain.notification.model.NotificationChannel;
import RandevuApp.domain.notification.model.NotificationRequest;
import RandevuApp.domain.notification.service.INotificationService;
import RandevuApp.domain.user.model.User;
import RandevuApp.domain.user.repository.UserRepository;
import RandevuApp.domain.user.service.IUserDomainService;
import RandevuApp.domain.verification.model.VerificationPurpose;
import RandevuApp.domain.verification.model.VerificationRequest;
import RandevuApp.domain.verification.model.VerificationType;
import RandevuApp.domain.verification.service.VerificationService;
import RandevuApp.exceptions.auth.ExpiredTokenException;
import RandevuApp.exceptions.auth.InvalidTokenException;
import RandevuApp.exceptions.client.InvalidInputException;
import RandevuApp.exceptions.client.PasswordMismatchException;
import RandevuApp.exceptions.client.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.Random;
import java.util.Set;

@Service
@Slf4j
public class AuthPasswordServiceImpl implements IAuthPasswordService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final IUserDomainService userDomainService;
    private final PasswordResetTokenRepository tokenRepository;
    private final VerificationService verificationService;
    private final INotificationService notificationService;
    private final AuthPasswordProperties authPasswordProperties;
    private final Random random = new SecureRandom();

    public AuthPasswordServiceImpl(PasswordEncoder passwordEncoder,
                                   UserRepository userRepository, IUserDomainService userDomainService,
                                   PasswordResetTokenRepository tokenRepository, VerificationService verificationService, INotificationService notificationService,
                                   AuthPasswordProperties authPasswordProperties) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.userDomainService = userDomainService;
        this.tokenRepository = tokenRepository;
        this.verificationService = verificationService;
        this.notificationService = notificationService;
        this.authPasswordProperties = authPasswordProperties;
    }

    @Override
    @Transactional(noRollbackFor = {ResourceNotFoundException.class})
    public void forgotPassword(String phoneNumber) {

        User user = userRepository.findByPhoneNumber(phoneNumber).orElse(null);

        if (user == null) {
            log.warn("Phone number not found for forgot password: {}", phoneNumber);
            return;
        }

        Long userId = user.getId();

        VerificationRequest verificationRequest = new VerificationRequest(
                userId,
                VerificationType.CODE,
                NotificationChannel.SMS,
                VerificationPurpose.PASSWORD_RESET,
                null
        );

        NotificationRequest warningForAttempt = NotificationRequest.builder()
                .recipient(user.getEmail())
                .category(NotificationCategory.SECURITY_ALERT)
                .userId(userId)
                .variables(Map.of("name", user.getFirstName()))
                .subject("TalosGym - Yakın zamanda şifre sıfırlama isteği gönderdiniz mi?")
                .message("Eğer siz değilseniz lütfen iletişime geçin.")
                .explicitChannels(Set.of(NotificationChannel.EMAIL))
                .build();

        notificationService.send(warningForAttempt);


        verificationService.startVerification(verificationRequest);
    }

    @Override
    @Transactional(noRollbackFor = {InvalidInputException.class})
    public String verifyOTP(VerifyOtpRequest verifyOtpRequest) {
        log.info("Verifying OTP for phone number: {}", verifyOtpRequest.phoneNumber());

        User user = userDomainService.findUserByIdentifier(verifyOtpRequest.phoneNumber());

        verificationService.verify(
                verifyOtpRequest.otpCode(),
                VerificationType.CODE,
                user.getId().toString(),
                VerificationPurpose.PASSWORD_RESET
        );

        tokenRepository.deleteByUserId(user.getId());

        int codeLength = authPasswordProperties.getCodeLength();
        String resetTokenStr = RandomStringUtils.random(codeLength, 0, 0, true, true, null, random);

        PasswordResetToken resetToken = new PasswordResetToken();
        resetToken.setToken(resetTokenStr);
        resetToken.setUser(user);
        resetToken.setExpiryDate(Instant.now().plus(authPasswordProperties.getResetTokenValidityMinutes(), ChronoUnit.MINUTES));

        tokenRepository.save(resetToken);

        log.info("OTP verified successfully. Generated resetToken for user: {}", user.getId());

        return resetTokenStr;
    }

    @Override
    @Transactional
    public void resetPassword(ResetPasswordRequest resetPasswordRequest) {
        log.warn("Resetting password for token: {}", resetPasswordRequest.resetToken());

        PasswordResetToken resetToken = tokenRepository.findByToken(resetPasswordRequest.resetToken())
                .orElseThrow(() -> new InvalidTokenException("Invalid password reset token"));

        if (resetToken.isExpired()) {
            tokenRepository.delete(resetToken);
            throw new ExpiredTokenException("Password reset token has expired");
        }

        if (!resetPasswordRequest.newPassword().equals(resetPasswordRequest.confirmNewPassword())){
            throw new PasswordMismatchException();
        }

        User user = resetToken.getUser();
        user.setPassword(passwordEncoder.encode(resetPasswordRequest.newPassword()));
        userRepository.save(user);

        tokenRepository.delete(resetToken);

        log.warn("Password updated successfully for user: {}", user.getEmail());
    }
}
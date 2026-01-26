package RandevuApp.domain.auth.service.impl;

import RandevuApp.domain.auth.dto.ResetPasswordRequest;
import RandevuApp.domain.auth.model.PasswordResetToken;
import RandevuApp.domain.auth.repository.PasswordResetTokenRepository;
import RandevuApp.domain.auth.service.IAuthPasswordService;
import RandevuApp.domain.auth.service.password.IPasswordResetStrategy;
import RandevuApp.domain.user.model.User;
import RandevuApp.domain.user.repository.UserRepository;
import RandevuApp.exceptions.auth.ExpiredTokenException;
import RandevuApp.exceptions.auth.InvalidTokenException;
import RandevuApp.exceptions.client.InvalidInputException;
import RandevuApp.exceptions.client.PasswordMismatchException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.regex.Pattern;

@Service
@Slf4j
public class AuthPasswordServiceImpl implements IAuthPasswordService {

    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$"
    );
    private static final Pattern PHONE_PATTERN = Pattern.compile(
            "^\\+?[0-9. ()-]{7,25}$"
    );

    private final IPasswordResetStrategy linkStrategy;
    private final IPasswordResetStrategy smsStrategy;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final PasswordResetTokenRepository tokenRepository;

    public AuthPasswordServiceImpl(@Qualifier("linkPasswordReset") IPasswordResetStrategy linkStrategy,
                                   @Qualifier("smsPasswordReset") IPasswordResetStrategy smsStrategy,
                                   PasswordEncoder passwordEncoder,
                                   UserRepository userRepository,
                                   PasswordResetTokenRepository tokenRepository) {
        this.linkStrategy = linkStrategy;
        this.smsStrategy = smsStrategy;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
    }

    @Override
    public String forgotPassword(String recipient) {
        if (isValidEmail(recipient)) {
            return linkStrategy.sendResetToken(recipient);
        } else if (isValidPhoneNumber(recipient)) {
            return smsStrategy.sendResetToken(recipient);
        } else {
            throw new InvalidInputException("Invalid email or phone number format.");
        }
    }

    @Override
    public ResponseEntity<Void> handleResetPassword(String token) {
        return ResponseEntity.ok().build();
    }

    @Override
    @Transactional
    public String resetPassword(ResetPasswordRequest resetPasswordRequest) {
        log.warn("Resetting password for token: {}", resetPasswordRequest.getToken());

        PasswordResetToken resetToken = tokenRepository.findByToken(resetPasswordRequest.getToken())
                .orElseThrow(() -> new InvalidTokenException("Invalid password reset token"));

        if (resetToken.isExpired()) {
            tokenRepository.delete(resetToken);
            throw new ExpiredTokenException("Password reset token has expired");
        }

        if (!resetPasswordRequest.getNewPassword().equals(resetPasswordRequest.getConfirmNewPassword())){
            throw new PasswordMismatchException();
        }

        User user = resetToken.getUser();
        user.setPassword(passwordEncoder.encode(resetPasswordRequest.getNewPassword()));
        userRepository.save(user);

        tokenRepository.delete(resetToken);

        log.warn("Password updated successfully for user: {}", user.getEmail());
        return "Password reset successful";
    }

    private boolean isValidEmail(String email) {
        if (email == null) {
            return false;
        }
        return EMAIL_PATTERN.matcher(email).matches();
    }

    private boolean isValidPhoneNumber(String phone) {
        if (phone == null) {
            return false;
        }
        return PHONE_PATTERN.matcher(phone).matches();
    }
}

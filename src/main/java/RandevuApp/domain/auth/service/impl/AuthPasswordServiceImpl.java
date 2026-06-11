package RandevuApp.domain.auth.service.impl;

import RandevuApp.commons.util.ContactFormatUtil;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class AuthPasswordServiceImpl implements IAuthPasswordService {

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
    public void forgotPassword(String recipient) {
        if (ContactFormatUtil.isEmail(recipient)) {
            linkStrategy.sendResetToken(recipient);
        } else if (ContactFormatUtil.isPhone(recipient)) {
            smsStrategy.sendResetToken(recipient);
        } else {
            throw new InvalidInputException("Invalid email or phone number format.");
        }
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

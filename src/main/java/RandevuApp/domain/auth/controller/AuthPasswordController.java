package RandevuApp.domain.auth.controller;

import RandevuApp.domain.auth.dto.ForgotPasswordRequest;
import RandevuApp.domain.auth.dto.ResetPasswordRequest;
import RandevuApp.domain.auth.service.IAuthPasswordService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
@Slf4j
public class AuthPasswordController {

    private final IAuthPasswordService authService;

    @PostMapping("/forgot-password")
    public String forgotPassword(@Valid @RequestBody ForgotPasswordRequest forgotPasswordRequest) {
        authService.forgotPassword(forgotPasswordRequest.phoneNumber());
        return "Password reset link has been sent to your email if it exists in our system.";
    }

    @PostMapping("/reset-password-submit")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void resetPassword(@Valid @RequestBody ResetPasswordRequest resetPasswordRequest) {
        log.warn("Input for /api/auth/reset-password-submit" + resetPasswordRequest);
        authService.resetPassword(resetPasswordRequest);
    }
}

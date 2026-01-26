package RandevuApp.domain.auth.controller;

import RandevuApp.api.CustomResponseBody;
import RandevuApp.domain.auth.dto.ForgotPasswordRequest;
import RandevuApp.domain.auth.dto.ResetPasswordRequest;
import RandevuApp.domain.auth.service.IAuthPasswordService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
@Slf4j
public class AuthPasswordController {

    private final IAuthPasswordService authService;

    @PostMapping("/forgot-password")
    public ResponseEntity<CustomResponseBody<String>> forgotPassword(@Valid @RequestBody ForgotPasswordRequest forgotPasswordRequest) {
        CustomResponseBody<String> body = CustomResponseBody.ok(authService.forgotPassword(forgotPasswordRequest.getRecipient()), "Password reset instructions sent to recipient if it exists in our system");
        return new ResponseEntity<>(body, HttpStatusCode.valueOf(body.getHttpStatus()));
    }

    @PostMapping("/reset-password-submit")
    public ResponseEntity<CustomResponseBody<String>> resetPassword(@Valid @RequestBody ResetPasswordRequest resetPasswordRequest) {
        log.warn("Input for /api/auth/reset-password-submit" + resetPasswordRequest);
        CustomResponseBody<String> body = CustomResponseBody.ok(authService.resetPassword(resetPasswordRequest), "Password has been reset successfully");
        return new ResponseEntity<>(body, HttpStatusCode.valueOf(body.getHttpStatus()));
    }
}

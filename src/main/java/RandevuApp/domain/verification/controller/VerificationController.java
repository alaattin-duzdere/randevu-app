package RandevuApp.domain.verification.controller;

import RandevuApp.domain.verification.dto.CodeConfirmRequest;
import RandevuApp.domain.verification.model.VerificationPurpose;
import RandevuApp.domain.verification.model.VerificationType;
import RandevuApp.domain.verification.service.VerificationService;
import RandevuApp.exceptions.verification.VerificationPurposeException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/verification")
@RequiredArgsConstructor
public class VerificationController {

    private final VerificationService verificationService;

    @PostMapping("/confirm-code")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public String confirmCode(@RequestBody CodeConfirmRequest request) {
        validateConfirmPurpose(request.purpose());
        verificationService.verify(request.code(), VerificationType.CODE,request.referenceId(), request.purpose());
        return "Code Confirmed";
    }

    @GetMapping("/confirm-link")
    public String confirmLink(@RequestParam("token") String token,
                              @RequestParam(value = "referenceId", required = false) String referenceId,
                              @RequestParam(value = "purpose", defaultValue = "GENERAL") VerificationPurpose purpose) {
        validateConfirmPurpose(purpose);
        verificationService.verify(token, VerificationType.LINK, referenceId, purpose);
        return "Link Confirmed!";
    }

    private void validateConfirmPurpose(VerificationPurpose purpose) {
        if (purpose == VerificationPurpose.PASSWORD_RESET) {
            throw new VerificationPurposeException("Password reset verification must be done via /auth/forgot-password/complete endpoint.");
        }
    }
}

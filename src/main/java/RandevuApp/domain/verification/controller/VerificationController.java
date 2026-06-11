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

//    @PostMapping("/initiate")
//    @ResponseStatus(Http.NO_CONTENT)
//    public ResponseEntity<void> initiate(@RequestBody VerificationRequest request) {
//        // Find user
//        User user = userDomainService.findUserById(request.getUserId());
//
//        // Validate request
//        filterChainManager.validateForController(request, user);
//
//        verificationService.startVerification(request);
//    }

    @PostMapping("/confirm-code")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void confirmCode(@RequestBody CodeConfirmRequest request) {
        validateConfirmPurpose(request.getPurpose());
        verificationService.verify(request.getCode(), VerificationType.CODE, request.getUserId(), request.getPurpose());
    }

    @GetMapping("/confirm-link")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void confirmLink(@RequestParam("token") String token, @RequestParam("userId") Long userId, @RequestParam(value = "purpose", defaultValue = "GENERAL") VerificationPurpose purpose) {
        validateConfirmPurpose(purpose);
        verificationService.verify(token, VerificationType.LINK, userId, purpose);
    }

    private void validateConfirmPurpose(VerificationPurpose purpose) {
        if (purpose == VerificationPurpose.PASSWORD_RESET) {
            throw new VerificationPurposeException("Password reset verification must be done via /auth/forgot-password/complete endpoint.");
        }
    }
}

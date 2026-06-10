package RandevuApp.domain.verification.controller;

import RandevuApp.domain.verification.dto.CodeConfirmRequest;
import RandevuApp.domain.verification.model.VerificationPurpose;
import RandevuApp.domain.verification.model.VerificationType;
import RandevuApp.domain.verification.service.VerificationService;
import RandevuApp.exceptions.verification.VerificationPurposeException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/verification")
@RequiredArgsConstructor
public class VerificationController {

    private final VerificationService verificationService;

//    // A. DOĞRULAMA BAŞLATMA (Herkes için ortak)
//    @PostMapping("/initiate")
//    public ResponseEntity<Void> initiate(@RequestBody VerificationRequest request) {
//        // Find user
//        User user = userDomainService.findUserById(request.getUserId());
//
//        // Validate request
//        filterChainManager.validateForController(request, user);
//
//        verificationService.startVerification(request);
//        return ResponseEntity.ok().build();
//    }

    @PostMapping("/confirm-code")
    public ResponseEntity<String> confirmCode(@RequestBody CodeConfirmRequest request) {
        validateConfirmPurpose(request.getPurpose());
        verificationService.verify(request.getCode(), VerificationType.CODE, request.getUserId(), request.getPurpose());
        return ResponseEntity.ok("Kod doğrulandı!");
    }

    @GetMapping("/confirm-link")
    public ResponseEntity<String> confirmLink(@RequestParam("token") String token, @RequestParam("userId") Long userId, @RequestParam(value = "purpose", defaultValue = "GENERAL") VerificationPurpose purpose) {
        validateConfirmPurpose(purpose);
        verificationService.verify(token, VerificationType.LINK, userId, purpose);
        return ResponseEntity.ok("Hesap doğrulandı!");
    }

    private void validateConfirmPurpose(VerificationPurpose purpose) {
        if (purpose == VerificationPurpose.PASSWORD_RESET) {
            throw new VerificationPurposeException("Password reset verification must be done via /auth/forgot-password/complete endpoint.");
        }
    }
}

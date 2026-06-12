package RandevuApp.domain.verification.dto;

import RandevuApp.domain.verification.model.VerificationPurpose;

public record CodeConfirmRequest(
        String referenceId,
        String code,
        VerificationPurpose purpose
) {}

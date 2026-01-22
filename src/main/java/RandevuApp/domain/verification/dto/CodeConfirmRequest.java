package RandevuApp.domain.verification.dto;

import RandevuApp.domain.verification.model.VerificationPurpose;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CodeConfirmRequest {
    private Long userId;
    private String code;
    private VerificationPurpose purpose;
}

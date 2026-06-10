package RandevuApp.domain.auth.service;

import RandevuApp.domain.auth.dto.ResetPasswordRequest;
import org.springframework.http.ResponseEntity;

public interface IAuthPasswordService {

    public String forgotPassword(String email);

    public String resetPassword(ResetPasswordRequest resetPasswordRequest);
}

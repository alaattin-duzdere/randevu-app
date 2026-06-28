package RandevuApp.domain.auth.service;

import RandevuApp.domain.auth.dto.ResetPasswordRequest;
import RandevuApp.domain.auth.dto.VerifyOtpRequest;

public interface IAuthPasswordService {
    void forgotPassword(String email);
    String verifyOTP(VerifyOtpRequest verifyOtpRequest);
    void resetPassword(ResetPasswordRequest resetPasswordRequest);
}

package RandevuApp.domain.auth.service;

import RandevuApp.domain.auth.dto.ResetPasswordRequest;

public interface IAuthPasswordService {

    void forgotPassword(String email);

    void resetPassword(ResetPasswordRequest resetPasswordRequest);
}

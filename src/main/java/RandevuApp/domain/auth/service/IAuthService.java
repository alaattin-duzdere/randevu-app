package RandevuApp.domain.auth.service;

import RandevuApp.domain.auth.dto.*;

public interface IAuthService {

    void register(RegisterRequest registerRequest);

    LoginResponse login(LoginRequest loginRequest);

    RefreshResponse refresh(RefreshRequest refreshRequest);

    String logout(String token);

    void resendVerification(String identifier);
}

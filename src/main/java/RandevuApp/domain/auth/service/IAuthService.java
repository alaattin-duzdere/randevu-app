package RandevuApp.domain.auth.service;

import RandevuApp.domain.auth.dto.*;
import jakarta.validation.Valid;

public interface IAuthService {

    void register(RegisterRequest registerRequest);

    LoginResponse login(LoginRequest loginRequest);

    RefreshResponse refresh(RefreshRequest refreshRequest);

    String logout(String token);
}

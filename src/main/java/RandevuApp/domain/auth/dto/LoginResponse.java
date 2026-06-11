package RandevuApp.domain.auth.dto;

import lombok.Builder;

@Builder
public record LoginResponse(
        String accessToken,
        String refreshToken,
        String tokenType,
        Long accessTokenExpiresIn,
        Long refreshTokenExpiresIn
) {
    public LoginResponse {
        if (tokenType == null) {
            tokenType = "Bearer";
        }
    }
}

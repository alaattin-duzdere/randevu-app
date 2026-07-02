package RandevuApp.domain.auth.dto;

import lombok.Builder;

@Builder
public record RefreshResponse(
        String accessToken,
        String refreshToken,
        String tokenType,
        Long accessTokenExpiresIn,
        Long refreshTokenExpiresIn
) {
    public RefreshResponse {
        if (tokenType == null) {
            tokenType = "Bearer";
        }
    }
}

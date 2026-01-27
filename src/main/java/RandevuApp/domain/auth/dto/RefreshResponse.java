package RandevuApp.domain.auth.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RefreshResponse {
    private String accessToken;
    private String refreshToken;
    @Builder.Default
    private String tokenType = "Bearer";
    private Long accessTokenExpiresIn;
    private Long refreshTokenExpiresIn;
}

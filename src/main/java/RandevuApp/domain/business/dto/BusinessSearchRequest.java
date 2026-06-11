package RandevuApp.domain.business.dto;

public record BusinessSearchRequest(
        String query,
        String address,
        Boolean active
) {
    public BusinessSearchRequest {
        if (active == null) active = true;
    }
}

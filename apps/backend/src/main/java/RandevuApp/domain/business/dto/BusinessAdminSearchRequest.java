package RandevuApp.domain.business.dto;

public record BusinessAdminSearchRequest(
        String query,
        String address,
        boolean isDeleted,
        Boolean active
) {
    public BusinessAdminSearchRequest {
        if (active == null) active = true;
    }
}
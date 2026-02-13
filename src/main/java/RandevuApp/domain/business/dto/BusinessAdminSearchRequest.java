package RandevuApp.domain.business.dto;


import lombok.Data;

@Data
public class BusinessAdminSearchRequest {
    private String query;

    private String address;

    private boolean isDeleted;

    private Boolean active = true;
}
package RandevuApp.domain.staff.dto;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class StaffResponse {

    private Long id;
    private Long businessId;

    private String name;
    private String title;
    private String email;
    private String phone;
    private String colorCode;
    private String photo;

    private boolean active;

    private List<Long> serviceIds;
}
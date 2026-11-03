package RandevuApp.domain.business.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.util.List;

public record UpdateOperatingHoursRequest(
        @NotEmpty(message = "Çalışma saatleri listesi boş olamaz")
        @Size(min = 1, max = 7, message = "Çalışma saatleri listesi 1 ile 7 gün arasında olmalıdır")
        @Valid List<OperatingHourDto> operatingHours
) {}

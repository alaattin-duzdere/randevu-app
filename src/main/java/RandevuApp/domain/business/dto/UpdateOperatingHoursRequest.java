package RandevuApp.domain.business.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.util.List;

public record UpdateOperatingHoursRequest(
        @NotEmpty(message = "Çalışma saatleri listesi boş olamaz")
        @Size(min = 7, max = 7, message = "Tam olarak 7 günlük (Pazartesi-Pazar) bir liste gönderilmelidir")
        @Valid List<OperatingHourDto> operatingHours
) {}

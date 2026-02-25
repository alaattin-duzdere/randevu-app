package RandevuApp.domain.business.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
public class UpdateOperatingHoursRequest {

    @NotEmpty(message = "Çalışma saatleri listesi boş olamaz")
    @Size(min = 7, max = 7, message = "Tam olarak 7 günlük (Pazartesi-Pazar) bir liste gönderilmelidir")
    @Valid
    private List<OperatingHourDto> operatingHours;
}
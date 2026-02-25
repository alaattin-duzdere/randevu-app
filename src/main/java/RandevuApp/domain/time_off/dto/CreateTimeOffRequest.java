package RandevuApp.domain.time_off.dto;

import RandevuApp.domain.time_off.model.TimeOffType;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CreateTimeOffRequest {

    @NotNull(message = "Başlangıç zamanı boş olamaz")
    @FutureOrPresent(message = "Geçmişe dönük izin oluşturulamaz")
    private LocalDateTime startTime;

    @NotNull(message = "Bitiş zamanı boş olamaz")
    @FutureOrPresent(message = "Geçmişe dönük izin oluşturulamaz")
    private LocalDateTime endTime;

    @NotNull(message = "İzin tipi seçilmelidir")
    private TimeOffType type;

    private String note;
}
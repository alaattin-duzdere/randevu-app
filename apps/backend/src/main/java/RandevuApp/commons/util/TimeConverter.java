package RandevuApp.commons.util;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;

@Component
public class TimeConverter {
    public OffsetDateTime toOffset(LocalDateTime local, ZoneId zone) {
        return local.atZone(zone).toOffsetDateTime();
    }

    public LocalDateTime toLocal(OffsetDateTime offset, ZoneId zone) {
        return offset.atZoneSameInstant(zone).toLocalDateTime();
    }
}

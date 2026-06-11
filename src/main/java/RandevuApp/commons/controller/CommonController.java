package RandevuApp.commons.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/common")
public class CommonController {

    @GetMapping("/timezones")
    public List<String> getAvailableTimeZones() {
        return ZoneId.getAvailableZoneIds().stream()
                .sorted()
                .collect(Collectors.toList());
    }
}

package RandevuApp.commons.controller;

import RandevuApp.api.CustomResponseBody;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<CustomResponseBody<List<String>>> getAvailableTimeZones() {
        List<String> zones = ZoneId.getAvailableZoneIds().stream()
                .sorted()
                .collect(Collectors.toList());

        return ResponseEntity.ok(CustomResponseBody.ok(zones,"Timezones retrieved successfully"));
    }
}

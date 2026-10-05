package com.freightboard.status;

import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class StatusController {

    // SB03 step 6: inject Spring's Environment through a constructor and report the ACTIVE profiles in activeProfiles.
    //         With no profile active, Spring uses the profile "default", and the list should be ["default"].
    //         Hint: environment.getActiveProfiles() is empty when nothing is active; getDefaultProfiles() isn't.
    private final Environment environment;

    public StatusController(Environment environment) {
        this.environment = environment;
    }

    @GetMapping("/api/status")
    public StatusResponse status() {
        String[] active = environment.getActiveProfiles();
        List<String> profiles = List.of(active.length > 0 ? active : environment.getDefaultProfiles());
        return new StatusResponse("FreightBoard", "UP", profiles);
    }

    @GetMapping("/api/greeting")
    public String greeting(@RequestParam(defaultValue = "guest") String name) {
        return "Welcome to FreightBoard, " + name + "!";
    }
}

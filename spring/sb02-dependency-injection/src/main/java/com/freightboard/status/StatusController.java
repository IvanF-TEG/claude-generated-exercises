package com.freightboard.status;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

// SB01 step 1: make this a REST controller and map GET /api/status to status().
//         It should return {"service":"FreightBoard","status":"UP"}.
// SB01 step 2: map GET /api/greeting to greeting(). The query parameter 'name' is optional:
//           /api/greeting?name=Ana  -> Welcome to FreightBoard, Ana!
//           /api/greeting           -> Welcome to FreightBoard, guest!
//         Returning a String from a @RestController sends it as plain text.
@RestController
public class StatusController {

    @GetMapping("/api/status")
    public StatusResponse status() {
        return new StatusResponse("FreightBoard", "UP");
    }

    @GetMapping("/api/greeting")
    public String greeting(@RequestParam(defaultValue = "guest") String name) {
        return "Welcome to FreightBoard, " + name + "!";
    }
}

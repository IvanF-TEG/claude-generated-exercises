package com.freightboard.status;


// TODO 1: make this a REST controller and map GET /api/status to status().
//         It should return {"service":"FreightBoard","status":"UP"}.
// TODO 2: map GET /api/greeting to greeting(). The query parameter 'name' is optional:
//           /api/greeting?name=Ana  -> Welcome to FreightBoard, Ana!
//           /api/greeting           -> Welcome to FreightBoard, guest!
//         Returning a String from a @RestController sends it as plain text.
public class StatusController {

    public StatusResponse status() {
        return null;
    }

    public String greeting(String name) {
        return null;
    }
}

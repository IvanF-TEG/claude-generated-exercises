package com.freightboard.status;

import java.util.List;

// GIVEN. activeProfiles is new in SB03: {"service":"FreightBoard","status":"UP","activeProfiles":["prod"]}
public record StatusResponse(String service, String status, List<String> activeProfiles) {
}

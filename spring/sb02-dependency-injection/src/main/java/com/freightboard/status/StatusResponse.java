package com.freightboard.status;

// GIVEN. Jackson turns this record into {"service":"FreightBoard","status":"UP"}.
public record StatusResponse(String service, String status) {
}

package com.freightboard.security;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.request;

/**
 * TODO 1a + 2b: every rule in SecurityConfig, checked with the REAL demo users and passwords over HTTP Basic.
 * "allowed" means security let the request THROUGH to the controller: the status may still be 404 or 400
 * (load 999 doesn't exist, {} isn't a valid body), just never 401 or 403.
 */
@SpringBootTest
@AutoConfigureMockMvc
class SecurityRulesTest {

    static final String PASSWORD = "freight123";

    @Autowired
    MockMvc mvc;

    int statusFor(String method, String path, String user) throws Exception {
        MockHttpServletRequestBuilder request = request(HttpMethod.valueOf(method), path)
                .contentType(MediaType.APPLICATION_JSON).content("{}");
        if (!user.equals("anonymous")) {
            request = request.with(httpBasic(user, PASSWORD));
        }
        return mvc.perform(request).andReturn().getResponse().getStatus();
    }

    @ParameterizedTest(name = "{0} {1} as {2} -> allowed")
    @CsvSource({
            "GET,    /api/status,                 anonymous",
            "GET,    /api/postcodes/LS1,          anonymous",
            "POST,   /api/quotes,                 anonymous",
            "POST,   /api/conversions/weight,     anonymous",
            "GET,    /api/loads,                  pennine",
            "GET,    /api/loads/999,              dales",
            "GET,    /api/carriers,               shipper",
            "POST,   /api/loads,                  shipper",
            "PUT,    /api/loads/999,              shipper2",
            "POST,   /api/loads/999/cancel,       shipper",
            "POST,   /api/loads/999/bids,         pennine",
            "GET,    /api/loads/999/bids,         shipper",
            "GET,    /api/loads/999/bids,         admin",
            "POST,   /api/bids/999/accept,        shipper",
            "DELETE, /api/loads/999,              admin",
            "POST,   /api/carriers,               admin",
    })
    void allowed(String method, String path, String user) throws Exception {
        int status = statusFor(method, path, user);
        assertNotEquals(401, status, "401: not logged in");
        assertNotEquals(403, status, "403: logged in, but not allowed");
    }

    @ParameterizedTest(name = "{0} {1} as {2} -> {3}")
    @CsvSource({
            "GET,    /api/loads,                  anonymous, 401",
            "POST,   /api/loads,                  anonymous, 401",
            "POST,   /api/loads,                  pennine,   403",
            "POST,   /api/loads,                  admin,     403",
            "PUT,    /api/loads/999,              pennine,   403",
            "POST,   /api/loads/999/cancel,       dales,     403",
            "POST,   /api/loads/999/bids,         shipper,   403",
            "POST,   /api/loads/999/bids,         admin,     403",
            "GET,    /api/loads/999/bids,         pennine,   403",
            "POST,   /api/bids/999/accept,        pennine,   403",
            "DELETE, /api/loads/999,              shipper,   403",
            "POST,   /api/carriers,               shipper,   403",
    })
    void denied(String method, String path, String user, int expected) throws Exception {
        assertEquals(expected, statusFor(method, path, user));
    }

    @ParameterizedTest(name = "{0} can log in")
    @CsvSource({"shipper", "shipper2", "pennine", "dales", "admin"})
    void everyDemoUserExists(String user) throws Exception {
        assertEquals(200, statusFor("GET", "/api/loads", user));
    }
}

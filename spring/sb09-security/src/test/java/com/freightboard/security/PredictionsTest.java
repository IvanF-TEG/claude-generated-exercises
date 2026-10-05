package com.freightboard.security;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

// PREDICTIONS: finish TODOs 1-2 first. Then replace each -1 / "???" with what you expect, and only THEN run.
// Keep a comment on any you got wrong.
@SpringBootTest
@AutoConfigureMockMvc
class PredictionsTest {

    @Autowired
    MockMvc mvc;

    MockHttpServletResponse send(RequestBuilder request) throws Exception {
        return mvc.perform(request).andReturn().getResponse();
    }

    @Test
    void aLoadThatDoesNotExistWithoutLoggingIn() throws Exception {
        // 401 or 404? Which runs first: the security filters or the controller?
        assertEquals(-1, send(get("/api/loads/999")).getStatus());
    }

    @Test
    void wrongPassword() throws Exception {
        assertEquals(-1, send(get("/api/loads").with(httpBasic("shipper", "wrong"))).getStatus());
    }

    @Test
    void userThatDoesNotExist() throws Exception {
        assertEquals(-1, send(get("/api/loads").with(httpBasic("nobody", "freight123"))).getStatus());
    }

    @Test
    void theHeaderThatAsksTheClientToLogIn() throws Exception {
        assertEquals("???", send(get("/api/loads")).getHeader("WWW-Authenticate"));
    }

    @Test
    void doesTheAppRememberYou() throws Exception {
        // Log in once. Is a session cookie sent back, so the next request can skip the password? "yes" or "no"
        MockHttpServletResponse response = send(get("/api/loads").with(httpBasic("shipper", "freight123")));
        String cookie = response.getHeader("Set-Cookie") == null ? "no" : "yes";
        assertEquals("???", cookie);
    }

    @Test
    void publicEndpointWithAWrongPassword() throws Exception {
        // /api/status is permitAll(). What if the client sends BAD credentials anyway?
        assertEquals(-1, send(get("/api/status").with(httpBasic("shipper", "wrong"))).getStatus());
    }
}

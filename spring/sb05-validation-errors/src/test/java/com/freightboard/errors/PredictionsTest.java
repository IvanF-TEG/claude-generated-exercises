package com.freightboard.errors;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

// PREDICTIONS: finish TODOs 1-6 first. Then replace each -1 / "???" with what you expect, and only THEN run.
// Keep a comment on any you got wrong.
@SpringBootTest
@AutoConfigureMockMvc
class PredictionsTest {

    @Autowired
    MockMvc mvc;

    MockHttpServletResponse send(RequestBuilder request) throws Exception {
        return mvc.perform(request).andReturn().getResponse();
    }

    static RequestBuilder createLoad(String json) {
        return post("/api/loads").contentType(MediaType.APPLICATION_JSON).content(json);
    }

    @Test
    void howManyMessagesForAnEmptyOrigin() throws Exception {
        String body = send(createLoad("""
                {"origin": "", "destination": "M1", "weightKg": 5, "pickupDate": "2031-03-01"}
                """)).getContentAsString();
        int messages = body.replaceAll(".*\"origin\":\\[([^]]*)].*", "$1").split("\",\"").length;
        assertEquals(-1, messages);
    }

    @Test
    void howManyMessagesForAMissingOrigin() throws Exception {
        String body = send(createLoad("""
                {"destination": "M1", "weightKg": 5, "pickupDate": "2031-03-01"}
                """)).getContentAsString();
        int messages = body.replaceAll(".*\"origin\":\\[([^]]*)].*", "$1").split("\",\"").length;
        assertEquals(-1, messages);
    }

    @Test
    void titleForMalformedJson() throws Exception {
        String body = send(createLoad("{\"origin\": ")).getContentAsString();
        // The "title" of the problem detail that Spring builds for you (TODO 6 doesn't change this one)
        assertEquals("???", body.replaceAll(".*\"title\":\"([^\"]*)\".*", "$1"));
    }

    @Test
    void contentTypeOfA405() throws Exception {
        // POST to GET-only /api/status. Before SB05 this had an empty body. And now?
        assertEquals("???", send(post("/api/status")).getContentType());
    }

    @Test
    void validationOrNotFoundFirst() throws Exception {
        // Load 999 doesn't exist AND the body is invalid. Which wins: 400 or 404?
        int status = send(put("/api/loads/999").contentType(MediaType.APPLICATION_JSON).content("""
                {"origin": "", "destination": "M1", "weightKg": 5, "pickupDate": "2031-03-01"}
                """)).getStatus();
        assertEquals(-1, status);
    }

    @Test
    void instanceFieldOfA404() throws Exception {
        String body = send(get("/api/loads/999")).getContentAsString();
        assertEquals("???", body.replaceAll(".*\"instance\":\"([^\"]*)\".*", "$1"));
    }
}

package com.freightboard;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

// PREDICTIONS: finish TODOs 1-5 first. Then, BEFORE running this class, replace each -1 with the HTTP status code
// you expect and each "???" with the text you expect. Keep a comment on any you got wrong, saying why.
// Status code cheat sheet: 200 OK, 201 Created, 204 No Content, 400 Bad Request, 401 Unauthorized, 403 Forbidden,
// 404 Not Found, 405 Method Not Allowed, 406 Not Acceptable, 409 Conflict, 415 Unsupported Media Type, 500 Server Error
@SpringBootTest
@AutoConfigureMockMvc
class PredictionsTest {

    @Autowired
    MockMvc mvc;

    int statusOf(org.springframework.test.web.servlet.RequestBuilder request) throws Exception {
        return mvc.perform(request).andReturn().getResponse().getStatus();
    }

    @Test
    void pathThatNothingIsMappedTo() throws Exception {
        assertEquals(-1, statusOf(get("/api/nowhere")));
    }

    @Test
    void postToAGetOnlyEndpoint() throws Exception {
        assertEquals(-1, statusOf(post("/api/status")));
    }

    @Test
    void greetingWithAnEmptyName() throws Exception {
        String body = mvc.perform(get("/api/greeting?name=")).andReturn().getResponse().getContentAsString();
        assertEquals("???", body);
    }

    @Test
    void bodyThatIsNotJson() throws Exception {
        assertEquals(-1, statusOf(post("/api/conversions/weight").contentType(MediaType.APPLICATION_JSON).content("lots")));
    }

    @Test
    void jsonSentWithoutSayingItIsJson() throws Exception {
        assertEquals(-1, statusOf(post("/api/conversions/weight").contentType(MediaType.TEXT_PLAIN).content("{\"kg\": 5}")));
    }

    @Test
    void askingForXml() throws Exception {
        assertEquals(-1, statusOf(get("/api/status").accept(MediaType.APPLICATION_XML)));
    }

    @Test
    void wrongTypeForAJsonField() throws Exception {
        assertEquals(-1, statusOf(post("/api/conversions/weight").contentType(MediaType.APPLICATION_JSON).content("{\"kg\": \"heavy\"}")));
    }
}

package com.freightboard.loads;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

// PREDICTIONS: finish TODOs 1-6 first. Then replace each -1 / "???" with what you expect, and only THEN run.
// Keep a comment on any you got wrong.
@SpringBootTest
@AutoConfigureMockMvc
class PredictionsTest {

    static final String BODY = """
            {"origin": "LS1", "destination": "M1", "weightKg": 1500, "pickupDate": "2031-03-01"}
            """;

    @Autowired
    MockMvc mvc;

    int statusOf(RequestBuilder request) throws Exception {
        return mvc.perform(request).andReturn().getResponse().getStatus();
    }

    MockHttpServletRequestBuilder json(MockHttpServletRequestBuilder request, String body) {
        return request.contentType(MediaType.APPLICATION_JSON).content(body);
    }

    long createdId() throws Exception {
        String body = mvc.perform(json(post("/api/loads"), BODY)).andReturn().getResponse().getContentAsString();
        return Long.parseLong(body.replaceAll(".*\"id\":(\\d+).*", "$1"));
    }

    @Test
    void samePostTwiceGivesTheSameId() throws Exception {
        // true or false? (Is POST idempotent?)
        assertEquals("???", String.valueOf(createdId() == createdId()));
    }

    @Test
    void secondDeleteOfTheSameLoad() throws Exception {
        long id = createdId();
        mvc.perform(delete("/api/loads/" + id));
        assertEquals(-1, statusOf(delete("/api/loads/" + id)));
    }

    @Test
    void secondIdenticalPut() throws Exception {
        long id = createdId();
        mvc.perform(json(put("/api/loads/" + id), BODY));
        assertEquals(-1, statusOf(json(put("/api/loads/" + id), BODY)));
    }

    @Test
    void statusFilterInLowerCase() throws Exception {
        assertEquals(-1, statusOf(get("/api/loads?status=open")));
    }

    @Test
    void idThatIsNotANumber() throws Exception {
        assertEquals(-1, statusOf(get("/api/loads/seven")));
    }

    @Test
    void howAPickupDateLooksInJson() throws Exception {
        long id = createdId();
        String body = mvc.perform(get("/api/loads/" + id)).andReturn().getResponse().getContentAsString();
        String pickupDate = body.replaceAll(".*\"pickupDate\":(\"[^\"]*\"|\\[[^]]*]).*", "$1");
        // Exactly as it appears in the JSON, quotes and all
        assertEquals("???", pickupDate);
    }

    @Test
    void createWithoutAWeight() throws Exception {
        // weightKg is missing from the JSON, and LoadRequest.weightKg is a primitive int (it can't be null).
        assertEquals(-1, statusOf(json(post("/api/loads"), """
                {"origin": "LS1", "destination": "M1", "pickupDate": "2031-03-01"}
                """)));
    }

    @Test
    void createWithoutAnOrigin() throws Exception {
        // origin is a String, so it CAN be null. Does the request succeed?
        assertEquals(-1, statusOf(json(post("/api/loads"), """
                {"destination": "M1", "weightKg": 1500, "pickupDate": "2031-03-01"}
                """)));
    }
}

package com.freightboard.loads;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class LoadApiIntegrationTest {

    @Autowired
    MockMvc mvc;

    long create(String origin) throws Exception {
        String location = mvc.perform(post("/api/loads").contentType(MediaType.APPLICATION_JSON).content("""
                        {"origin": "%s", "destination": "M1", "weightKg": 1500, "pickupDate": "2031-03-01"}
                        """.formatted(origin)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getHeader("Location");
        return Long.parseLong(location.substring(location.lastIndexOf('/') + 1));
    }

    @Test
    void fullLifecycle() throws Exception {
        long id = create("ZE1");

        mvc.perform(put("/api/loads/" + id).contentType(MediaType.APPLICATION_JSON).content("""
                        {"origin": "ZE2", "destination": "M1", "weightKg": 900, "pickupDate": "2031-03-02"}
                        """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.weightKg").value(900));

        mvc.perform(post("/api/loads/" + id + "/cancel")).andExpect(jsonPath("$.status").value("CANCELLED"));
        mvc.perform(post("/api/loads/" + id + "/cancel")).andExpect(status().isConflict());

        mvc.perform(get("/api/loads").param("status", "CANCELLED").param("origin", "ze2"))
                .andExpect(jsonPath("$[*].id").value(hasItem((int) id)));

        mvc.perform(delete("/api/loads/" + id)).andExpect(status().isNoContent());
        mvc.perform(delete("/api/loads/" + id)).andExpect(status().isNotFound());
        mvc.perform(get("/api/loads/" + id))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.detail").value("No load with id " + id));
        mvc.perform(get("/api/loads")).andExpect(jsonPath("$[*].id").value(not(hasItem((int) id))));
    }

    @Test
    void heaviestAndPickupsThroughTheWholeStack() throws Exception {
        long light = create("ZE3");
        mvc.perform(put("/api/loads/" + light).contentType(MediaType.APPLICATION_JSON).content("""
                {"origin": "ZE3", "destination": "M1", "weightKg": 43999, "pickupDate": "2031-03-09"}
                """)).andExpect(status().isOk());

        mvc.perform(get("/api/loads/heaviest").param("limit", "1"))
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].weightKg").value(43999));

        mvc.perform(get("/api/loads/pickups").param("from", "2031-03-09").param("to", "2031-03-09"))
                .andExpect(jsonPath("$[*].id").value(hasItem((int) light)));
    }

    @Test
    void todo4FindByReference() throws Exception {
        long id = create("ZE4");
        String reference = mvc.perform(get("/api/loads/" + id)).andReturn().getResponse().getContentAsString()
                .replaceAll(".*\"reference\":\"([^\"]+)\".*", "$1");
        mvc.perform(get("/api/loads/by-reference/" + reference))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id));
        mvc.perform(get("/api/loads/by-reference/FB-NOPE00")).andExpect(status().isNotFound());
    }
}

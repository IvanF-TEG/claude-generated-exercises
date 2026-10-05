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

/**
 * The whole application, all layers together: controller -> service -> repository.
 * Other test classes share this application context (Spring caches it), so this test only relies on the ids
 * that IT created, never on "the first load" or "there are exactly 3 loads".
 */
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

        mvc.perform(get("/api/loads/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.origin").value("ZE1"));

        mvc.perform(put("/api/loads/" + id).contentType(MediaType.APPLICATION_JSON).content("""
                        {"origin": "ZE2", "destination": "M1", "weightKg": 900, "pickupDate": "2031-03-02"}
                        """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.weightKg").value(900));

        mvc.perform(post("/api/loads/" + id + "/cancel"))
                .andExpect(jsonPath("$.status").value("CANCELLED"));

        mvc.perform(get("/api/loads").param("status", "CANCELLED").param("origin", "ze2"))
                .andExpect(jsonPath("$[*].id").value(hasItem((int) id)));

        mvc.perform(delete("/api/loads/" + id)).andExpect(status().isNoContent());
        mvc.perform(get("/api/loads/" + id)).andExpect(status().isNotFound());
        mvc.perform(get("/api/loads")).andExpect(jsonPath("$[*].id").value(not(hasItem((int) id))));
    }
}

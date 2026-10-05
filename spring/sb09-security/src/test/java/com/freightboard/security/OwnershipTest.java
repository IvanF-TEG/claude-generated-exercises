package com.freightboard.security;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * TODOs 4-6 end to end, with the real users. The role rules let every SHIPPER in; these checks are about WHICH
 * load. This is OWASP's number one API risk, "Broken Object Level Authorization": /api/loads/7 is protected,
 * but can user B change user A's load 7 just by guessing the number?
 */
@SpringBootTest
@AutoConfigureMockMvc
class OwnershipTest {

    static final String PASSWORD = "freight123";
    static final String LOAD = """
            {"origin": "ZE1", "destination": "M1", "weightKg": 1500, "pickupDate": "2031-03-01"}
            """;

    @Autowired
    MockMvc mvc;

    static MockHttpServletRequestBuilder as(String user, MockHttpServletRequestBuilder request) {
        return request.with(httpBasic(user, PASSWORD)).contentType(MediaType.APPLICATION_JSON);
    }

    long created(MockHttpServletRequestBuilder request) throws Exception {
        String body = mvc.perform(request).andExpect(status().isCreated()).andReturn().getResponse().getContentAsString();
        return Long.parseLong(body.replaceAll(".*?\"id\":(\\d+).*", "$1"));
    }

    @Test
    void shippersOnlyChangeTheirOwnLoads() throws Exception {
        long load = created(as("shipper", post("/api/loads")).content(LOAD));

        mvc.perform(as("shipper2", put("/api/loads/" + load)).content(LOAD)).andExpect(status().isForbidden());
        mvc.perform(as("shipper2", post("/api/loads/" + load + "/cancel"))).andExpect(status().isForbidden());

        mvc.perform(as("shipper", post("/api/loads/" + load + "/cancel")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("CANCELLED"));
    }

    @Test
    void carriersBidAsThemselvesAndOnlyTheOwnerAccepts() throws Exception {
        mvc.perform(as("admin", post("/api/carriers"))
                .content("{\"name\": \"Pennine Haulage (ownership test)\", \"maxWeightKg\": 20000, \"username\": \"pennine\"}"));
        long load = created(as("shipper", post("/api/loads")).content(LOAD));

        long bid = created(as("pennine", post("/api/loads/" + load + "/bids")).content("{\"amountPence\": 9000}"));

        // "dales" is a CARRIER user, but no carrier row is linked to that login
        mvc.perform(as("dales", post("/api/loads/" + load + "/bids")).content("{\"amountPence\": 100}"))
                .andExpect(status().isForbidden());

        mvc.perform(as("shipper2", post("/api/bids/" + bid + "/accept"))).andExpect(status().isForbidden());
        mvc.perform(as("shipper", post("/api/bids/" + bid + "/accept")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.carrierName").value("Pennine Haulage (ownership test)"));
    }
}

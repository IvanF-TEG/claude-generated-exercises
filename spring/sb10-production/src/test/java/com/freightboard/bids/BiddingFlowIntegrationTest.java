package com.freightboard.bids;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.RequestPostProcessor;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// SB09: each request says WHO is sending it, with .with(user(...)). Different steps need different people.
@SpringBootTest
@AutoConfigureMockMvc
class BiddingFlowIntegrationTest {

    static final RequestPostProcessor ADMIN = user("admin").roles("ADMIN");
    static final RequestPostProcessor SHIPPER = user("flow-shipper").roles("SHIPPER");
    static final RequestPostProcessor CARRIER_A = user("flow-a").roles("CARRIER");
    static final RequestPostProcessor CARRIER_B = user("flow-b").roles("CARRIER");

    @Autowired
    MockMvc mvc;

    static MockHttpServletRequestBuilder json(MockHttpServletRequestBuilder request, String body) {
        return request.contentType(MediaType.APPLICATION_JSON).content(body);
    }

    long idFrom(MockHttpServletRequestBuilder request) throws Exception {
        String body = mvc.perform(request).andExpect(status().isCreated()).andReturn().getResponse().getContentAsString();
        return Long.parseLong(body.replaceAll(".*?\"id\":(\\d+).*", "$1"));
    }

    @Test
    void placeAcceptAndBook() throws Exception {
        idFrom(json(post("/api/carriers"), "{\"name\": \"Flow Carrier A\", \"maxWeightKg\": 20000, \"username\": \"flow-a\"}").with(ADMIN));
        idFrom(json(post("/api/carriers"), "{\"name\": \"Flow Carrier B\", \"maxWeightKg\": 20000, \"username\": \"flow-b\"}").with(ADMIN));
        long load = idFrom(json(post("/api/loads"), """
                {"origin": "ZE1", "destination": "M1", "weightKg": 1500, "pickupDate": "2031-03-01"}
                """).with(SHIPPER));

        long bidA = idFrom(json(post("/api/loads/" + load + "/bids"), "{\"amountPence\": 9000}").with(CARRIER_A));
        idFrom(json(post("/api/loads/" + load + "/bids"), "{\"amountPence\": 8000}").with(CARRIER_B));

        mvc.perform(get("/api/loads/" + load + "/bids").with(SHIPPER))
                .andExpect(jsonPath("$[0].carrierName").value("Flow Carrier B"))
                .andExpect(jsonPath("$[1].carrierName").value("Flow Carrier A"));

        mvc.perform(post("/api/bids/" + bidA + "/accept").with(user("someone-else").roles("SHIPPER")))
                .andExpect(status().isForbidden());

        mvc.perform(post("/api/bids/" + bidA + "/accept").with(SHIPPER))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("ACCEPTED"));

        mvc.perform(get("/api/loads/" + load).with(CARRIER_A)).andExpect(jsonPath("$.status").value("BOOKED"));

        mvc.perform(json(post("/api/loads/" + load + "/bids"), "{\"amountPence\": 7000}").with(CARRIER_B))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.detail").value("Load " + load + " is BOOKED and can't take bids"));

        mvc.perform(delete("/api/loads/" + load).with(ADMIN))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.detail").value("Load " + load + " has bids and can't be deleted"));
    }
}

package com.freightboard.bids;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class BiddingFlowIntegrationTest {

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
        long carrierA = idFrom(json(post("/api/carriers"), "{\"name\": \"Flow Carrier A\", \"maxWeightKg\": 20000}"));
        long carrierB = idFrom(json(post("/api/carriers"), "{\"name\": \"Flow Carrier B\", \"maxWeightKg\": 20000}"));
        long load = idFrom(json(post("/api/loads"), """
                {"origin": "ZE1", "destination": "M1", "weightKg": 1500, "pickupDate": "2031-03-01"}
                """));

        long bidA = idFrom(json(post("/api/loads/" + load + "/bids"), "{\"carrierId\": " + carrierA + ", \"amountPence\": 9000}"));
        idFrom(json(post("/api/loads/" + load + "/bids"), "{\"carrierId\": " + carrierB + ", \"amountPence\": 8000}"));

        mvc.perform(get("/api/loads/" + load + "/bids"))
                .andExpect(jsonPath("$[0].carrierName").value("Flow Carrier B"))
                .andExpect(jsonPath("$[1].carrierName").value("Flow Carrier A"));

        mvc.perform(post("/api/bids/" + bidA + "/accept"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("ACCEPTED"));

        mvc.perform(get("/api/loads/" + load)).andExpect(jsonPath("$.status").value("BOOKED"));
        mvc.perform(get("/api/loads/" + load + "/bids")).andExpect(jsonPath("$[0].status").value("REJECTED"));

        mvc.perform(json(post("/api/loads/" + load + "/bids"), "{\"carrierId\": " + carrierB + ", \"amountPence\": 7000}"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.detail").value("Load " + load + " is BOOKED and can't take bids"));

        mvc.perform(delete("/api/loads/" + load))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.detail").value("Load " + load + " has bids and can't be deleted"));
    }
}

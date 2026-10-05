package com.freightboard.quotes;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("prod")
class ProdProfileTest {

    @Autowired
    PricingProperties properties;

    @Autowired
    QuoteService quoteService;

    @Autowired
    MockMvc mvc;

    @Test
    void todo4ProdOverridesOnlyWhatItLists() {
        // base fee and rate come from application-prod.yml; the other two still come from application.yml
        assertEquals(new PricingProperties.Distance(3000, 110, 1000, 3), properties.distance());
    }

    @Test
    void todo4ProdPrice() {
        // 3000 + 110 * 70 + 3 * 500
        assertEquals(12200, quoteService.quoteWith("distance", new QuoteRequest("LS1", "M1", 70, 1500)).orElseThrow().pricePence());
    }

    @Test
    void todo5NoPromoInProd() {
        assertEquals(List.of("distance", "weight-band"), quoteService.strategyNames());
    }

    @Test
    void todo6StatusShowsProd() throws Exception {
        mvc.perform(get("/api/status"))
                .andExpect(jsonPath("$.activeProfiles.length()").value(1))
                .andExpect(jsonPath("$.activeProfiles[0]").value("prod"));
    }
}

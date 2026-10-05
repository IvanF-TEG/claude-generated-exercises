package com.freightboard.quotes;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Import(QuoteControllerTest.FixedClockConfig.class)
class QuoteControllerTest {

    // Adds a SECOND Clock bean for this test only. @Primary tells Spring to prefer it wherever one Clock is injected.
    @TestConfiguration
    static class FixedClockConfig {
        @Bean
        @Primary
        Clock fixedClock() {
            return Clock.fixed(Instant.parse("2026-01-05T09:00:00Z"), ZoneOffset.UTC);
        }
    }

    static final String BODY = """
            {"origin": "LS1", "destination": "M1", "distanceKm": 70, "weightKg": 1500}
            """;

    @Autowired
    MockMvc mvc;

    @Test
    void todo6AllQuotesCheapestFirst() throws Exception {
        mvc.perform(post("/api/quotes").contentType(MediaType.APPLICATION_JSON).content(BODY))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                        [
                          {"strategy": "weight-band", "pricePence": 7700,  "quotedAt": "2026-01-05T09:00:00Z"},
                          {"strategy": "distance",    "pricePence": 10650, "quotedAt": "2026-01-05T09:00:00Z"}
                        ]
                        """));
    }

    @Test
    void todo6Cheapest() throws Exception {
        mvc.perform(post("/api/quotes/cheapest").contentType(MediaType.APPLICATION_JSON).content(BODY))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.strategy").value("weight-band"))
                .andExpect(jsonPath("$.pricePence").value(7700));
    }

    @Test
    void todo6OneStrategy() throws Exception {
        mvc.perform(post("/api/quotes/distance").contentType(MediaType.APPLICATION_JSON).content(BODY))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.strategy").value("distance"))
                .andExpect(jsonPath("$.pricePence").value(10650));
    }

    @Test
    void todo6UnknownStrategyIs404() throws Exception {
        mvc.perform(post("/api/quotes/teleport").contentType(MediaType.APPLICATION_JSON).content(BODY))
                .andExpect(status().isNotFound());
    }
}

package com.freightboard.conversions;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ConversionControllerTest {

    @Autowired
    MockMvc mvc;

    @Nested
    class Todo4aVehicleRules {

        @ParameterizedTest
        @CsvSource({
                "1,     van",
                "1000,  van",
                "1001,  rigid",
                "18000, rigid",
                "18001, artic",
                "44000, artic",
                "44001, abnormal load",
        })
        void picksSmallestVehicle(int kg, String vehicle) {
            assertEquals(vehicle, WeightConversion.of(kg).vehicle());
        }

        @Test
        void convertsToTonnes() {
            assertEquals(new WeightConversion(1500, 1.5, "rigid"), WeightConversion.of(1500));
        }
    }

    @Nested
    class Todo4bEndpoint {

        @Test
        void convertsJsonBody() throws Exception {
            mvc.perform(post("/api/conversions/weight")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("""
                                    {"kg": 1500}
                                    """))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.kg").value(1500))
                    .andExpect(jsonPath("$.tonnes").value(1.5))
                    .andExpect(jsonPath("$.vehicle").value("rigid"));
        }
    }

    @Nested
    class Todo5BadRequest {

        @Test
        void zeroIs400() throws Exception {
            mvc.perform(post("/api/conversions/weight")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("""
                                    {"kg": 0}
                                    """))
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string(""));
        }

        @Test
        void negativeIs400() throws Exception {
            mvc.perform(post("/api/conversions/weight")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("""
                                    {"kg": -5}
                                    """))
                    .andExpect(status().isBadRequest());
        }
    }
}

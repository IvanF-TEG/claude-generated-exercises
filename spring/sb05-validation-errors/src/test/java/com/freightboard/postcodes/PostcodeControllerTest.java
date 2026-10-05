package com.freightboard.postcodes;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class PostcodeControllerTest {

    @Autowired
    MockMvc mvc;

    @Nested
    class Lookup {

        @Test
        void returnsPostcodeInfo() throws Exception {
            mvc.perform(get("/api/postcodes/ec1a"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.outwardCode").value("EC1A"))
                    .andExpect(jsonPath("$.area").value("EC"))
                    .andExpect(jsonPath("$.london").value(true));
        }

        @Test
        void worksOutsideLondon() throws Exception {
            mvc.perform(get("/api/postcodes/LS1"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.area").value("LS"))
                    .andExpect(jsonPath("$.london").value(false));
        }
    }

    @Nested
    class BadRequest {

        @Test
        void invalidCodeIs400WithEmptyBody() throws Exception {
            mvc.perform(get("/api/postcodes/123"))
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string(""));
        }
    }
}

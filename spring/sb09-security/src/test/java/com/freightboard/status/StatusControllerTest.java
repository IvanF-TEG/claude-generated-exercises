package com.freightboard.status;

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

// MockMvc sends fake HTTP requests straight into Spring MVC: no real server or network, but the same routing,
// JSON conversion and status codes a real client would get.
@SpringBootTest
@AutoConfigureMockMvc
class StatusControllerTest {

    @Autowired
    MockMvc mvc;

    @Nested
    class Status {

        @Test
        void returnsServiceAndStatusAsJson() throws Exception {
            mvc.perform(get("/api/status"))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType("application/json"))
                    .andExpect(jsonPath("$.service").value("FreightBoard"))
                    .andExpect(jsonPath("$.status").value("UP"));
        }

        @Test
        void wholeBodyMatches() throws Exception {
            mvc.perform(get("/api/status"))
                    .andExpect(content().json("""
                            {"service": "FreightBoard", "status": "UP"}
                            """));
        }
    }

    @Nested
    class ActiveProfiles {

        @Test
        void defaultProfileWhenNoneIsActive() throws Exception {
            mvc.perform(get("/api/status"))
                    .andExpect(jsonPath("$.activeProfiles.length()").value(1))
                    .andExpect(jsonPath("$.activeProfiles[0]").value("default"));
        }
    }

    @Nested
    class Greeting {

        @Test
        void greetsByName() throws Exception {
            mvc.perform(get("/api/greeting").param("name", "Ana"))
                    .andExpect(status().isOk())
                    .andExpect(content().string("Welcome to FreightBoard, Ana!"));
        }

        @Test
        void nameIsOptional() throws Exception {
            mvc.perform(get("/api/greeting"))
                    .andExpect(status().isOk())
                    .andExpect(content().string("Welcome to FreightBoard, guest!"));
        }

        @Test
        void isPlainText() throws Exception {
            mvc.perform(get("/api/greeting").param("name", "Ana"))
                    .andExpect(content().contentTypeCompatibleWith("text/plain"));
        }
    }
}

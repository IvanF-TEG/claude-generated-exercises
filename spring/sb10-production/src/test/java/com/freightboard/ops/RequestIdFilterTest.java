package com.freightboard.ops;

import org.junit.jupiter.api.Test;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.matchesPattern;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// TODO 5: MockMvc runs the request on THIS thread, so the test can check the MDC is empty afterwards.
@SpringBootTest
@AutoConfigureMockMvc
class RequestIdFilterTest {

    @Autowired
    MockMvc mvc;

    @Test
    void keepsTheCallersId() throws Exception {
        mvc.perform(get("/api/status").header("X-Request-Id", "abc-123"))
                .andExpect(header().string("X-Request-Id", "abc-123"));
    }

    @Test
    void makesOneUpOtherwise() throws Exception {
        mvc.perform(get("/api/status"))
                .andExpect(header().string("X-Request-Id", matchesPattern("[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}")));
    }

    @Test
    void blankCountsAsMissing() throws Exception {
        mvc.perform(get("/api/status").header("X-Request-Id", "  "))
                .andExpect(header().string("X-Request-Id", matchesPattern("[0-9a-f-]{36}")));
    }

    @Test
    void evenA401HasOne() throws Exception {
        mvc.perform(get("/api/loads").header("X-Request-Id", "who-am-i"))
                .andExpect(status().isUnauthorized())
                .andExpect(header().string("X-Request-Id", "who-am-i"));
    }

    @Test
    void mdcIsClearedAfterTheRequest() throws Exception {
        mvc.perform(get("/api/status").header("X-Request-Id", "leaky"));
        assertNull(MDC.get("requestId"), "the next request on this thread would inherit 'leaky'");
    }
}

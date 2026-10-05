package com.freightboard.ops;

import com.freightboard.bids.BidService;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

// PREDICTIONS: finish TODOs 1-5 first. Then replace each -1 / "???" with what you expect, and only THEN run.
// Keep a comment on any you got wrong.
@SpringBootTest
@AutoConfigureMockMvc
class PredictionsTest {

    @Autowired
    MockMvc mvc;

    int statusOf(RequestBuilder request) throws Exception {
        return mvc.perform(request).andReturn().getResponse().getStatus();
    }

    @Test
    void kubernetesStyleLivenessProbe() throws Exception {
        // Kubernetes asks "are you alive?" and "are you ready for traffic?" separately. We're not on Kubernetes,
        // and we didn't configure any probes. Does the liveness endpoint exist anyway? (And is it public?)
        assertEquals(-1, statusOf(get("/actuator/health/liveness")));
    }

    @Test
    void shutdownEndpointAsAdmin() throws Exception {
        assertEquals(-1, statusOf(post("/actuator/shutdown").with(user("admin").roles("ADMIN"))));
    }

    @Test
    void metricsWithOurPrefix() throws Exception {
        String body = mvc.perform(get("/actuator/metrics").with(user("admin").roles("ADMIN")))
                .andReturn().getResponse().getContentAsString();
        long ours = java.util.Arrays.stream(body.split("\"")).filter(s -> s.startsWith("freightboard.")).count();
        assertEquals(-1, ours);
    }

    @Test
    void isDebugLoggingOnForBidService() {
        // "true" or "false"? (Look at the logback-test.xml in src/test/resources.)
        assertEquals("???", String.valueOf(LoggerFactory.getLogger(BidService.class).isDebugEnabled()));
    }

    @Test
    void httpMetricsAreRecordedAutomatically() throws Exception {
        mvc.perform(get("/api/status"));
        // Did Spring record a timer for that request without us writing any code? The status of this lookup:
        assertEquals(-1, statusOf(get("/actuator/metrics/http.server.requests").with(user("admin").roles("ADMIN"))));
    }
}

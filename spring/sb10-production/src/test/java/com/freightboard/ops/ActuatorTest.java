package com.freightboard.ops;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// TODO 1 (and 2): what Actuator shows, and to whom.
@SpringBootTest
@AutoConfigureMockMvc
class ActuatorTest {

    @Autowired
    MockMvc mvc;

    @Test
    void healthIsPublicButHidesDetails() throws Exception {
        mvc.perform(get("/actuator/health"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("UP"))
                .andExpect(jsonPath("$.components").doesNotExist());
    }

    @Test
    void adminSeesEveryComponent() throws Exception {
        mvc.perform(get("/actuator/health").with(user("admin").roles("ADMIN")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.components.db.status").value("UP"))
                .andExpect(jsonPath("$.components.loadBoard.status").value("UP"))
                .andExpect(jsonPath("$.components.loadBoard.details.openLoads").isNumber());
    }

    @Test
    void shipperSeesNoDetails() throws Exception {
        mvc.perform(get("/actuator/health").with(user("shipper").roles("SHIPPER")))
                .andExpect(jsonPath("$.components").doesNotExist());
    }

    @Test
    void infoShowsTheAppAndTheBuild() throws Exception {
        mvc.perform(get("/actuator/info"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.app.name").value("FreightBoard"))
                .andExpect(jsonPath("$.build.artifact").value("sb10-production"));
    }

    @Test
    void metricsNeedAnAdmin() throws Exception {
        mvc.perform(get("/actuator/metrics")).andExpect(status().isUnauthorized());
        mvc.perform(get("/actuator/metrics").with(user("shipper").roles("SHIPPER"))).andExpect(status().isForbidden());
        mvc.perform(get("/actuator/metrics/freightboard.bids.placed").with(user("admin").roles("ADMIN")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("freightboard.bids.placed"));
    }

    @Test
    void onlyThreeEndpointsAreExposed() throws Exception {
        mvc.perform(get("/actuator/env").with(user("admin").roles("ADMIN"))).andExpect(status().isNotFound());
        mvc.perform(get("/actuator/beans").with(user("admin").roles("ADMIN"))).andExpect(status().isNotFound());
    }
}

package com.freightboard.loads;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * A SLICE test: @WebMvcTest starts only the web layer (this one controller, JSON conversion, MockMvc).
 * No services, no repositories. LoadService is replaced by a Mockito MOCK: a fake whose answers each test
 * scripts with given(...).willReturn(...). This checks ONLY the controller's job: URLs, status codes, JSON.
 */
@WebMvcTest(LoadController.class)
class LoadControllerTest {

    static final LocalDate DAY = LocalDate.of(2031, 3, 1);
    static final Load LOAD = new Load(7, "LS1", "M1", 1500, DAY, LoadStatus.OPEN);
    static final String BODY = """
            {"origin": "LS1", "destination": "M1", "weightKg": 1500, "pickupDate": "2031-03-01"}
            """;
    static final LoadRequest REQUEST = new LoadRequest("LS1", "M1", 1500, DAY);

    @Autowired
    MockMvc mvc;

    @MockitoBean
    LoadService loadService;

    @Nested
    class Todo3Read {

        @Test
        void listPassesFiltersToTheService() throws Exception {
            given(loadService.search(LoadStatus.OPEN, "LS1")).willReturn(List.of(LOAD));
            mvc.perform(get("/api/loads").param("status", "OPEN").param("origin", "LS1"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.length()").value(1))
                    .andExpect(jsonPath("$[0].id").value(7));
        }

        @Test
        void listWithoutFilters() throws Exception {
            given(loadService.search(null, null)).willReturn(List.of());
            mvc.perform(get("/api/loads"))
                    .andExpect(status().isOk())
                    .andExpect(content().json("[]"));
        }

        @Test
        void findByIdAsJson() throws Exception {
            given(loadService.findById(7)).willReturn(Optional.of(LOAD));
            mvc.perform(get("/api/loads/7"))
                    .andExpect(status().isOk())
                    .andExpect(content().json("""
                            {"id": 7, "origin": "LS1", "destination": "M1", "weightKg": 1500,
                             "pickupDate": "2031-03-01", "status": "OPEN"}
                            """));
        }

        @Test
        void findByIdMissing() throws Exception {
            given(loadService.findById(99)).willReturn(Optional.empty());
            mvc.perform(get("/api/loads/99")).andExpect(status().isNotFound());
        }
    }

    @Nested
    class Todo4Create {

        @Test
        void createdWithLocationHeader() throws Exception {
            given(loadService.create(REQUEST)).willReturn(LOAD);
            mvc.perform(post("/api/loads").contentType(MediaType.APPLICATION_JSON).content(BODY))
                    .andExpect(status().isCreated())
                    .andExpect(header().string("Location", "http://localhost/api/loads/7"))
                    .andExpect(jsonPath("$.id").value(7))
                    .andExpect(jsonPath("$.status").value("OPEN"));
        }
    }

    @Nested
    class Todo5UpdateAndCancel {

        @Test
        void update() throws Exception {
            given(loadService.update(7, REQUEST)).willReturn(Optional.of(LOAD));
            mvc.perform(put("/api/loads/7").contentType(MediaType.APPLICATION_JSON).content(BODY))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(7));
            verify(loadService).update(7, REQUEST);
        }

        @Test
        void updateMissing() throws Exception {
            given(loadService.update(any(Long.class), any())).willReturn(Optional.empty());
            mvc.perform(put("/api/loads/99").contentType(MediaType.APPLICATION_JSON).content(BODY))
                    .andExpect(status().isNotFound());
        }

        @Test
        void cancel() throws Exception {
            given(loadService.cancel(7)).willReturn(Optional.of(LOAD.withStatus(LoadStatus.CANCELLED)));
            mvc.perform(post("/api/loads/7/cancel"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value("CANCELLED"));
        }

        @Test
        void cancelMissing() throws Exception {
            given(loadService.cancel(99)).willReturn(Optional.empty());
            mvc.perform(post("/api/loads/99/cancel")).andExpect(status().isNotFound());
        }
    }

    @Nested
    class Todo6Delete {

        @Test
        void deleted() throws Exception {
            given(loadService.delete(7)).willReturn(true);
            mvc.perform(delete("/api/loads/7"))
                    .andExpect(status().isNoContent())
                    .andExpect(content().string(""));
        }

        @Test
        void missing() throws Exception {
            given(loadService.delete(99)).willReturn(false);
            mvc.perform(delete("/api/loads/99")).andExpect(status().isNotFound());
        }
    }
}

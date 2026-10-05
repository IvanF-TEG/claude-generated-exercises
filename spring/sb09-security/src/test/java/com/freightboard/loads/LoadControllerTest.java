package com.freightboard.loads;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import com.freightboard.security.SecurityConfig;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// @WebMvcTest also loads @RestControllerAdvice classes, so GlobalExceptionHandler takes part in these tests.
// SB06: the mocked service returns ENTITIES; these tests check that the JSON (via LoadResponse) is unchanged.
// SB09: a slice test doesn't pick up @Configuration classes by itself, so the security rules are imported, and every
// test runs as a pretend logged-in user (@WithMockUser: no password check, just a name and roles).
@WebMvcTest(LoadController.class)
@Import(SecurityConfig.class)
@WithMockUser(username = "shipper", roles = {"SHIPPER", "ADMIN"})
class LoadControllerTest {

    static final LocalDate DAY = LocalDate.of(2031, 3, 1);
    static final LoadRequest REQUEST = new LoadRequest("LS1", "M1", 1500, DAY);
    static final Load LOAD = TestLoads.withId(7, REQUEST);
    static final String BODY = """
            {"origin": "LS1", "destination": "M1", "weightKg": 1500, "pickupDate": "2031-03-01"}
            """;

    @Autowired
    MockMvc mvc;

    @MockitoBean
    LoadService loadService;

    @Nested
    class ValidationRunsBeforeTheService {

        @Test
        void invalidCreateIs400() throws Exception {
            mvc.perform(post("/api/loads").contentType(MediaType.APPLICATION_JSON).content("""
                            {"origin": "", "destination": "M1", "weightKg": 0, "pickupDate": "2031-03-01"}
                            """))
                    .andExpect(status().isBadRequest());
            verify(loadService, never()).create(any(), any());
        }

        @Test
        void invalidUpdateIs400() throws Exception {
            mvc.perform(put("/api/loads/7").contentType(MediaType.APPLICATION_JSON).content("""
                            {"origin": "LS1", "destination": "LS1", "weightKg": 10, "pickupDate": "2031-03-01"}
                            """))
                    .andExpect(status().isBadRequest());
            verify(loadService, never()).update(any(Long.class), any(), any());
        }
    }

    @Nested
    class ReturnsDtos {

        @Test
        void get() throws Exception {
            given(loadService.get(7)).willReturn(LOAD);
            mvc.perform(LoadControllerTest.get("/api/loads/7"))
                    .andExpect(status().isOk())
                    .andExpect(content().json("""
                            {"id": 7, "reference": "FB-TEST07", "origin": "LS1", "destination": "M1", "weightKg": 1500,
                             "pickupDate": "2031-03-01", "status": "OPEN"}
                            """, org.springframework.test.json.JsonCompareMode.STRICT));
        }

        @Test
        void create() throws Exception {
            given(loadService.create(REQUEST, "shipper")).willReturn(LOAD);
            mvc.perform(post("/api/loads").contentType(MediaType.APPLICATION_JSON).content(BODY))
                    .andExpect(status().isCreated())
                    .andExpect(header().string("Location", "http://localhost/api/loads/7"));
        }

        @Test
        void update() throws Exception {
            given(loadService.update(7, REQUEST, "shipper")).willReturn(LOAD);
            mvc.perform(put("/api/loads/7").contentType(MediaType.APPLICATION_JSON).content(BODY))
                    .andExpect(status().isOk());
        }

        @Test
        void cancel() throws Exception {
            Load cancelled = TestLoads.withId(7, REQUEST);
            cancelled.cancel();
            given(loadService.cancel(7, "shipper")).willReturn(cancelled);
            mvc.perform(post("/api/loads/7/cancel"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value("CANCELLED"));
        }

        @Test
        void delete() throws Exception {
            mvc.perform(LoadControllerTest.delete("/api/loads/7"))
                    .andExpect(status().isNoContent())
                    .andExpect(content().string(""));
            verify(loadService).delete(7);
        }
    }

    // static helpers so the nested tests can call get/delete despite having methods with the same names
    static org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder get(String url) {
        return org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get(url);
    }

    static org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder delete(String url) {
        return org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete(url);
    }

    @Nested
    class NewEndpoints {

        @Test
        void pickupsParsesIsoDates() throws Exception {
            given(loadService.pickupsBetween(DAY, DAY.plusDays(30))).willReturn(java.util.List.of(LOAD));
            mvc.perform(LoadControllerTest.get("/api/loads/pickups").param("from", "2031-03-01").param("to", "2031-03-31"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$[0].id").value(7));
        }

        @Test
        void heaviestDefaultsToFive() throws Exception {
            given(loadService.heaviestOpen(5)).willReturn(java.util.List.of(LOAD));
            mvc.perform(LoadControllerTest.get("/api/loads/heaviest"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$[0].weightKg").value(1500));
        }

        @Test
        void heaviestWithLimit() throws Exception {
            given(loadService.heaviestOpen(2)).willReturn(java.util.List.of());
            mvc.perform(LoadControllerTest.get("/api/loads/heaviest").param("limit", "2"))
                    .andExpect(status().isOk())
                    .andExpect(content().json("[]"));
        }
    }

    @Nested
    class ByReference {

        @Test
        void found() throws Exception {
            given(loadService.getByReference("FB-TEST07")).willReturn(LOAD);
            mvc.perform(LoadControllerTest.get("/api/loads/by-reference/FB-TEST07"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.reference").value("FB-TEST07"));
        }

        @Test
        void missing() throws Exception {
            given(loadService.getByReference("FB-NOPE00")).willThrow(new LoadNotFoundException("FB-NOPE00"));
            mvc.perform(LoadControllerTest.get("/api/loads/by-reference/FB-NOPE00"))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.detail").value("No load with reference FB-NOPE00"));
        }
    }

    @Nested
    class ErrorResponses {

        @Test
        void notFoundIsAProblemDetail() throws Exception {
            given(loadService.get(99)).willThrow(new LoadNotFoundException(99));
            mvc.perform(LoadControllerTest.get("/api/loads/99"))
                    .andExpect(status().isNotFound())
                    .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                    .andExpect(content().json("""
                            {"title": "Load not found", "status": 404, "detail": "No load with id 99", "instance": "/api/loads/99"}
                            """));
        }

        @Test
        void deleteMissingIs404() throws Exception {
            willThrow(new LoadNotFoundException(99)).given(loadService).delete(99);
            mvc.perform(LoadControllerTest.delete("/api/loads/99"))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.title").value("Load not found"));
        }

        @Test
        void wrongStateIs409() throws Exception {
            given(loadService.cancel(7, "shipper")).willThrow(new InvalidLoadStateException("Load 7 is CANCELLED and can't be changed"));
            mvc.perform(post("/api/loads/7/cancel"))
                    .andExpect(status().isConflict())
                    .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                    .andExpect(jsonPath("$.title").value("Invalid load state"))
                    .andExpect(jsonPath("$.detail").value("Load 7 is CANCELLED and can't be changed"));
        }

        @Test
        void validationErrorsAreListedByField() throws Exception {
            mvc.perform(post("/api/loads").contentType(MediaType.APPLICATION_JSON).content("""
                            {"origin": "", "destination": "M1", "weightKg": 0, "pickupDate": "2031-03-01"}
                            """))
                    .andExpect(status().isBadRequest())
                    .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                    .andExpect(content().json("""
                            {
                              "title": "Validation failed",
                              "status": 400,
                              "detail": "The request has 2 invalid field(s)",
                              "instance": "/api/loads",
                              "errors": {
                                "origin": ["must be a UK outward code such as LS1", "must not be blank"],
                                "weightKg": ["must be greater than 0"]
                              }
                            }
                            """, org.springframework.test.json.JsonCompareMode.STRICT));
        }

        @Test
        void crossFieldErrorIsOnDestination() throws Exception {
            mvc.perform(post("/api/loads").contentType(MediaType.APPLICATION_JSON).content("""
                            {"origin": "LS1", "destination": "LS1", "weightKg": 10, "pickupDate": "2031-03-01"}
                            """))
                    .andExpect(jsonPath("$.errors.destination[0]").value("must be different from origin"));
        }
    }
}

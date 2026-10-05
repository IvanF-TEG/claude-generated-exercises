package com.freightboard.db;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.postgresql.PostgreSQLContainer;

import javax.sql.DataSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.hamcrest.Matchers.matchesPattern;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * The whole app against a REAL PostgreSQL, started in Docker for this test class and thrown away afterwards.
 * disabledWithoutDocker: if Docker isn't running, the class is SKIPPED (not failed).
 *
 * SB08 step 5: declare the database container. It's a static field (one container for the whole class) of type
 *         PostgreSQLContainer, created with the image name "postgres:17-alpine", and annotated
 *           @Container          -> Testcontainers starts it before the tests and stops it afterwards
 *           @ServiceConnection  -> Spring Boot points spring.datasource.* at it (URL, user, password)
 *         Then un-comment the @Test annotations below.
 */
@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers(disabledWithoutDocker = true)
@WithMockUser(username = "shipper", roles = "SHIPPER")
class PostgresIntegrationTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer postgres = new PostgreSQLContainer("postgres:17-alpine");

    @Autowired
    DataSource dataSource;

    @Autowired
    JdbcTemplate jdbc;

    @Autowired
    MockMvc mvc;

    @Test
    void reallyIsPostgres() throws Exception {
        try (var connection = dataSource.getConnection()) {
            assertEquals("PostgreSQL", connection.getMetaData().getDatabaseProductName());
        }
    }

    @Test
    void flywayRanEveryMigration() {
        assertEquals(4, jdbc.queryForObject("select count(*) from flyway_schema_history where type = 'SQL' and success", Integer.class));
    }

    @Test
    void loadRoundTripWithAReference() throws Exception {
        String body = mvc.perform(post("/api/loads").contentType(MediaType.APPLICATION_JSON).content("""
                        {"origin": "LS1", "destination": "M1", "weightKg": 1500, "pickupDate": "2031-03-01"}
                        """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.reference").value(matchesPattern("FB-[A-Z][A-Z2-9]{5}")))
                .andReturn().getResponse().getContentAsString();
        String reference = body.replaceAll(".*\"reference\":\"([^\"]+)\".*", "$1");
        mvc.perform(get("/api/loads/by-reference/" + reference))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.origin").value("LS1"));
    }
}

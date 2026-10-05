package com.freightboard.db;

import org.flywaydb.core.Flyway;
import org.h2.jdbcx.JdbcDataSource;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.UUID;

// Test helper: a brand-new, empty in-memory H2 database, plus Flyway pointed at OUR migration scripts.
// No Spring at all, so a test can migrate step by step and look at the rows in between.
final class ScratchDatabase {

    final JdbcDataSource dataSource = new JdbcDataSource();
    final JdbcTemplate jdbc;

    ScratchDatabase() {
        dataSource.setURL("jdbc:h2:mem:" + UUID.randomUUID() + ";DB_CLOSE_DELAY=-1");
        dataSource.setUser("sa");
        jdbc = new JdbcTemplate(dataSource);
    }

    Flyway flyway() {
        return Flyway.configure().dataSource(dataSource).locations("classpath:db/migration").load();
    }

    /** Migrate only up to (and including) the given version. */
    Flyway flywayUpTo(String version) {
        return Flyway.configure().dataSource(dataSource).locations("classpath:db/migration").target(version).load();
    }

    void insertLoad(long id) {
        jdbc.update("insert into loads (id, origin, destination, weight_kg, pickup_date, status) "
                + "values (?, 'LS1', 'M1', 1500, DATE '2031-03-01', 'OPEN')", id);
    }
}

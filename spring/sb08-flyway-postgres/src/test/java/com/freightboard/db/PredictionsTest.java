package com.freightboard.db;

import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

// PREDICTIONS: finish TODOs 1-3 first. Then replace each -1 / "???" with what you expect, and only THEN run.
// Keep a comment on any you got wrong. Each test gets its own empty database.
class PredictionsTest {

    final ScratchDatabase db = new ScratchDatabase();

    @Test
    void rowsInTheHistoryTable() {
        db.flyway().migrate();
        int rows = db.jdbc.queryForObject("select count(*) from \"flyway_schema_history\"", Integer.class);
        assertEquals(-1, rows);
    }

    @Test
    void migratingASecondTime() {
        db.flyway().migrate();
        assertEquals(-1, db.flyway().migrate().migrationsExecuted);
    }

    @Test
    void currentVersion() {
        Flyway flyway = db.flyway();
        flyway.migrate();
        assertEquals("???", flyway.info().current().getVersion().getVersion());
    }

    @Test
    void backfillForASevenDigitId() {
        db.flywayUpTo("2").migrate();
        db.insertLoad(1234567);
        db.flyway().migrate();
        assertEquals("???", db.jdbc.queryForObject("select reference from loads", String.class));
    }

    @Test
    void pendingMigrationsBeforeMigrating() {
        assertEquals(-1, db.flyway().info().pending().length);
    }
}

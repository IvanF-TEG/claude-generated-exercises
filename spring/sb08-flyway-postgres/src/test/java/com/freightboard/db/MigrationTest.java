package com.freightboard.db;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class MigrationTest {

    final ScratchDatabase db = new ScratchDatabase();

    @Nested
    class Todo1Loads {

        @Test
        void v1CreatesLoadsWithAGeneratedId() {
            db.flywayUpTo("1").migrate();
            db.jdbc.update("insert into loads (origin, destination, weight_kg, pickup_date, status) "
                    + "values ('LS1', 'M1', 1500, DATE '2031-03-01', 'OPEN')");
            assertEquals(1, db.jdbc.queryForObject("select count(*) from loads where id is not null", Integer.class));
        }

        @Test
        void v1ColumnsAreNotNull() {
            db.flywayUpTo("1").migrate();
            assertThrows(DataIntegrityViolationException.class, () -> db.jdbc.update(
                    "insert into loads (origin, destination, weight_kg, pickup_date) values ('LS1', 'M1', 1500, DATE '2031-03-01')"));
        }
    }

    @Nested
    class Todo2CarriersAndBids {

        @Test
        void bidMustPointAtARealLoad() {
            db.flywayUpTo("2").migrate();
            db.jdbc.update("insert into carriers (id, name, max_weight_kg) values (1, 'Pennine Haulage', 20000)");
            assertThrows(DataIntegrityViolationException.class, () -> db.jdbc.update(
                    "insert into bids (load_id, carrier_id, amount_pence, status, placed_at) "
                            + "values (999, 1, 9000, 'PENDING', TIMESTAMP WITH TIME ZONE '2031-02-01 09:00:00+00')"));
        }

        @Test
        void carrierNamesAreUnique() {
            db.flywayUpTo("2").migrate();
            db.jdbc.update("insert into carriers (name, max_weight_kg) values ('Pennine Haulage', 20000)");
            assertThrows(DataIntegrityViolationException.class, () ->
                    db.jdbc.update("insert into carriers (name, max_weight_kg) values ('Pennine Haulage', 1000)"));
        }

        @Test
        void bidsAreIndexedByLoad() {
            db.flywayUpTo("2").migrate();
            assertEquals(1, db.jdbc.queryForObject(
                    "select count(*) from information_schema.indexes where index_name = 'IDX_BIDS_LOAD_ID'", Integer.class));
        }
    }

    @Nested
    class Todo3Reference {

        @Test
        void existingRowsAreBackfilled() {
            db.flywayUpTo("2").migrate();
            db.insertLoad(1);
            db.insertLoad(42);
            db.flyway().migrate();
            assertEquals(List.of("FB-000001", "FB-000042"),
                    db.jdbc.queryForList("select reference from loads order by id", String.class));
        }

        @Test
        void referenceIsRequiredAfterTheBackfill() {
            db.flyway().migrate();
            assertThrows(DataIntegrityViolationException.class, () -> db.insertLoad(7));
        }

        @Test
        void referenceIsUnique() {
            db.flyway().migrate();
            String insert = "insert into loads (origin, destination, weight_kg, pickup_date, status, reference) "
                    + "values ('LS1', 'M1', 1500, DATE '2031-03-01', 'OPEN', 'FB-SAME01')";
            db.jdbc.update(insert);
            assertThrows(DataIntegrityViolationException.class, () -> db.jdbc.update(insert));
        }

        @Test
        void allThreeMigrationsApplied() {
            assertEquals(3, db.flyway().migrate().migrationsExecuted);
        }
    }
}

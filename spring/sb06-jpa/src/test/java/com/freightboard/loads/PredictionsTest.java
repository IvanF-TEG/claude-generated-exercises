package com.freightboard.loads;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

// PREDICTIONS: finish TODOs 1-3 first. Then replace each -1 / "???" with what you expect, and only THEN run.
// Keep a comment on any you got wrong. Each test runs inside one transaction, which is rolled back at the end.
@DataJpaTest
class PredictionsTest {

    @Autowired
    LoadRepository repository;

    @Autowired
    TestEntityManager entityManager;

    static Load newLoad() {
        return new Load(new LoadRequest("LS1", "M1", 1500, LocalDate.of(2031, 3, 1)));
    }

    Object sql(String query) {
        return entityManager.getEntityManager().createNativeQuery(query).getSingleResult();
    }

    @Test
    void idBeforeSaving() {
        // String.valueOf(null) is "null"
        assertEquals("???", String.valueOf(newLoad().getId()));
    }

    @Test
    void saveReturnsTheSameObject() {
        Load load = newLoad();
        assertEquals("???", String.valueOf(repository.save(load) == load));
    }

    @Test
    void findByIdTwiceInOneTransaction() {
        long id = repository.save(newLoad()).getId();
        entityManager.flush();
        entityManager.clear();
        Load first = repository.findById(id).orElseThrow();
        Load second = repository.findById(id).orElseThrow();
        assertEquals("???", String.valueOf(first == second));
    }

    @Test
    void changeWithoutSaving() {
        Load load = repository.save(newLoad());
        entityManager.flush();
        load.updateDetails(new LoadRequest("LS1", "M1", 777, LocalDate.of(2031, 3, 1)));
        entityManager.flush(); // send any pending changes to the database now, instead of at commit
        Object inTheDatabase = sql("select weight_kg from loads where id = " + load.getId());
        assertEquals(-1, ((Number) inTheDatabase).intValue());
    }

    @Test
    void columnNameForPickupDate() {
        // H2 stores unquoted names in capitals. What did Hibernate call the column for the field 'pickupDate'?
        Object column = sql("select column_name from information_schema.columns "
                + "where table_name = 'LOADS' and column_name like '%PICK%'");
        assertEquals("???", column);
    }

    @Test
    void howManyRowsAfterSaveThenDelete() {
        Load load = repository.save(newLoad());
        repository.save(newLoad());
        repository.delete(load);
        assertEquals(-1, repository.count());
    }
}

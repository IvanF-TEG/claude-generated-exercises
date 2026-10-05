package com.freightboard.loads;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class InMemoryLoadRepositoryTest {

    static final LocalDate DAY = LocalDate.of(2031, 3, 1);
    static final LoadRequest LEEDS = new LoadRequest("LS1", "M1", 1500, DAY);
    static final LoadRequest HULL = new LoadRequest("HU1", "YO1", 800, DAY);

    final InMemoryLoadRepository repository = new InMemoryLoadRepository();

    @Test
    void saveAssignsIdsFromOne() {
        assertEquals(1, repository.save(Load.newLoad(LEEDS)).id());
        assertEquals(2, repository.save(Load.newLoad(HULL)).id());
    }

    @Test
    void saveWithAnIdReplaces() {
        Load saved = repository.save(Load.newLoad(LEEDS));
        repository.save(saved.withStatus(LoadStatus.CANCELLED));
        assertEquals(LoadStatus.CANCELLED, repository.findById(saved.id()).orElseThrow().status());
        assertEquals(1, repository.findAll().size());
    }

    @Test
    void findById() {
        Load saved = repository.save(Load.newLoad(LEEDS));
        assertEquals(Optional.of(saved), repository.findById(saved.id()));
        assertEquals(Optional.empty(), repository.findById(99));
    }

    @Test
    void findAllInIdOrder() {
        for (int i = 0; i < 20; i++) {
            repository.save(Load.newLoad(i % 2 == 0 ? LEEDS : HULL));
        }
        List<Long> ids = repository.findAll().stream().map(Load::id).toList();
        assertEquals(20, ids.size());
        assertEquals(ids.stream().sorted().toList(), ids);
    }

    @Test
    void delete() {
        Load saved = repository.save(Load.newLoad(LEEDS));
        assertTrue(repository.deleteById(saved.id()));
        assertFalse(repository.deleteById(saved.id()));
        assertTrue(repository.findAll().isEmpty());
    }
}

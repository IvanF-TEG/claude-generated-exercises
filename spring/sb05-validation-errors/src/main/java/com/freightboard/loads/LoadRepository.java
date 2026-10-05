package com.freightboard.loads;

import java.util.List;
import java.util.Optional;

// GIVEN. The service depends on this interface. In SB06 a database-backed version replaces the in-memory one.
public interface LoadRepository {

    /** id 0 -> assign the next id (1, 2, 3...) and store it. Otherwise replace the stored load with that id. Returns what was stored. */
    Load save(Load load);

    Optional<Load> findById(long id);

    /** Every load, in id order. */
    List<Load> findAll();

    /** Returns true if something was removed. */
    boolean deleteById(long id);
}

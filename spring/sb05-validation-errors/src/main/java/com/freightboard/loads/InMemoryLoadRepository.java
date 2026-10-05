package com.freightboard.loads;

import org.springframework.stereotype.Repository;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * SB04 step 1: implement LoadRepository with a map, and make it a Spring bean with @Repository.
 * A web server handles many requests AT THE SAME TIME, on different threads, and they all share this one
 * singleton bean. That's why the map is a ConcurrentHashMap and the id counter is an AtomicLong.
 */
@Repository
public class InMemoryLoadRepository implements LoadRepository {

    private final Map<Long, Load> loads = new ConcurrentHashMap<>();
    private final AtomicLong nextId = new AtomicLong(1);

    @Override
    public Load save(Load load) {
        Load toStore = load.id() == 0 ? load.withId(nextId.getAndIncrement()) : load;
        loads.put(toStore.id(), toStore);
        return toStore;
    }

    @Override
    public Optional<Load> findById(long id) {
        return Optional.ofNullable(loads.get(id));
    }

    @Override
    public List<Load> findAll() {
        return loads.values().stream().sorted(Comparator.comparingLong(Load::id)).toList();
    }

    @Override
    public boolean deleteById(long id) {
        return loads.remove(id) != null;
    }
}

package com.freightboard.loads;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * TODO 1: implement LoadRepository with a map, and make it a Spring bean with @Repository.
 * A web server handles many requests AT THE SAME TIME, on different threads, and they all share this one
 * singleton bean. That's why the map is a ConcurrentHashMap and the id counter is an AtomicLong.
 */
public class InMemoryLoadRepository implements LoadRepository {

    private final Map<Long, Load> loads = new ConcurrentHashMap<>();
    private final AtomicLong nextId = new AtomicLong(1);

    @Override
    public Load save(Load load) {
        return null;
    }

    @Override
    public Optional<Load> findById(long id) {
        return Optional.empty();
    }

    @Override
    public List<Load> findAll() {
        return List.of();
    }

    @Override
    public boolean deleteById(long id) {
        return false;
    }
}

package com.freightboard.ops;

import com.freightboard.carriers.Carrier;
import com.freightboard.carriers.CarrierRepository;
import com.freightboard.loads.Load;
import com.freightboard.loads.LoadRequest;
import com.freightboard.loads.LoadService;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.concurrent.atomic.AtomicInteger;

// Test helper bean: a fresh load and carrier for each test (unique names, as the database is shared).
@Component
class BidTestData {

    private static final AtomicInteger NEXT = new AtomicInteger();

    private final LoadService loadService;
    private final CarrierRepository carriers;

    BidTestData(LoadService loadService, CarrierRepository carriers) {
        this.loadService = loadService;
        this.carriers = carriers;
    }

    Load load() {
        return loadService.create(new LoadRequest("LS1", "M1", 1500, LocalDate.of(2031, 3, 1)), "ops-shipper");
    }

    Carrier carrier() {
        int n = NEXT.incrementAndGet();
        return carriers.save(new Carrier("Ops Test Carrier " + n, 44000, "ops-carrier-" + n));
    }
}

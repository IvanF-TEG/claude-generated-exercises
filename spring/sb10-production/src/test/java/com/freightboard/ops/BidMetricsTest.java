package com.freightboard.ops;

import com.freightboard.bids.BidRequest;
import com.freightboard.bids.BidResponse;
import com.freightboard.bids.BidService;
import com.freightboard.carriers.Carrier;
import com.freightboard.loads.Load;
import io.micrometer.core.instrument.MeterRegistry;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.support.TransactionTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

// TODO 3: counters move only for bids that really happened (committed).
@SpringBootTest
@Import(BidTestData.class)
class BidMetricsTest {

    @Autowired
    MeterRegistry registry;

    @Autowired
    BidService bidService;

    @Autowired
    BidTestData data;

    @Autowired
    TransactionTemplate transactions;

    double count(String name) {
        return registry.counter(name).count();
    }

    @Test
    void placingABidCountsOnce() {
        double before = count("freightboard.bids.placed");
        bidService.placeBid(data.load().getId(), data.carrier().getUsername(), new BidRequest(9000));
        assertEquals(before + 1, count("freightboard.bids.placed"));
    }

    @Test
    void acceptingABidCountsOnce() {
        Load load = data.load();
        BidResponse bid = bidService.placeBid(load.getId(), data.carrier().getUsername(), new BidRequest(9000));
        double before = count("freightboard.bids.accepted");
        bidService.acceptBid(bid.id(), load.getShipper());
        assertEquals(before + 1, count("freightboard.bids.accepted"));
    }

    @Test
    void aRejectedBidDoesNotCount() {
        Carrier carrier = data.carrier();
        Load load = data.load();
        bidService.placeBid(load.getId(), carrier.getUsername(), new BidRequest(9000));
        double before = count("freightboard.bids.placed");
        assertThrows(RuntimeException.class,
                () -> bidService.placeBid(load.getId(), carrier.getUsername(), new BidRequest(8000)));
        assertEquals(before, count("freightboard.bids.placed"));
    }

    @Test
    void aBidThatIsRolledBackDoesNotCount() {
        long loadId = data.load().getId();
        String carrier = data.carrier().getUsername();
        double before = count("freightboard.bids.placed");
        // placeBid runs INSIDE this outer transaction, which is then rolled back: the bid never existed
        transactions.executeWithoutResult(status -> {
            bidService.placeBid(loadId, carrier, new BidRequest(9000));
            status.setRollbackOnly();
        });
        assertEquals(before, count("freightboard.bids.placed"),
                "counted a bid that was rolled back: use @TransactionalEventListener, not @EventListener");
    }
}

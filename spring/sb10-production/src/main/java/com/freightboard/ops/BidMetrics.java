package com.freightboard.ops;

import com.freightboard.bids.BidAccepted;
import com.freightboard.bids.BidPlaced;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Component;

/**
 * Business metrics, visible at /actuator/metrics/freightboard.bids.placed (and ready for Prometheus, Datadog...).
 *
 * TODO 3b: count the events.
 *   - onBidPlaced increments bidsPlaced; onBidAccepted increments bidsAccepted
 *   - annotate both with @TransactionalEventListener. It waits until the transaction that published the event has
 *     COMMITTED (and does nothing if it rolled back). A plain @EventListener would count bids that never happened.
 */
@Component
public class BidMetrics {

    private final Counter bidsPlaced;
    private final Counter bidsAccepted;

    public BidMetrics(MeterRegistry registry) {
        this.bidsPlaced = Counter.builder("freightboard.bids.placed").description("Bids placed").register(registry);
        this.bidsAccepted = Counter.builder("freightboard.bids.accepted").description("Bids accepted").register(registry);
    }

    public void onBidPlaced(BidPlaced event) {
    }

    public void onBidAccepted(BidAccepted event) {
    }
}

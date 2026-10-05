package com.freightboard.bids;

import com.freightboard.carriers.Carrier;
import com.freightboard.loads.Load;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.Instant;

/**
 * A carrier's offer to move a load for a price. Many bids belong to one load, and many bids come from one carrier.
 *
 * TODO 1a: map the two relationships. Each one is a foreign-key column in the "bids" table:
 *   load    -> column "load_id",    required, fetched LAZILY
 *   carrier -> column "carrier_id", required, fetched LAZILY
 *   (@ManyToOne is EAGER by default: every bid would drag its load and carrier along. Make both LAZY.)
 */
@Entity
@Table(name = "bids")
public class Bid {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Load load;

    private Carrier carrier;

    @Column(nullable = false)
    private long amountPence;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BidStatus status;

    @Column(nullable = false)
    private Instant placedAt;

    protected Bid() {
    }

    public Bid(Load load, Carrier carrier, long amountPence, Instant placedAt) {
        this.load = load;
        this.carrier = carrier;
        this.amountPence = amountPence;
        this.placedAt = placedAt;
        this.status = BidStatus.PENDING;
    }

    public void accept() {
        this.status = BidStatus.ACCEPTED;
    }

    public void reject() {
        this.status = BidStatus.REJECTED;
    }

    public Long getId() {
        return id;
    }

    public Load getLoad() {
        return load;
    }

    public Carrier getCarrier() {
        return carrier;
    }

    public long getAmountPence() {
        return amountPence;
    }

    public BidStatus getStatus() {
        return status;
    }

    public Instant getPlacedAt() {
        return placedAt;
    }
}

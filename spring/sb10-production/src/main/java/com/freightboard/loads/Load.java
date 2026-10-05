package com.freightboard.loads;

import com.freightboard.bids.Bid;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A load: a row in the "loads" table (mapped in SB06).
 *
 * SB07 step 1b: SB07 adds the OTHER side of the Bid.load relationship: a load's bids, as a @OneToMany.
 *          mappedBy = "load" says "the foreign key lives in Bid.load; this side just reads it". Without mappedBy,
 *          JPA would invent a separate join table. (@OneToMany is LAZY by default, which is what we want.)
 */
@Entity
@Table(name = "loads")
public class Load {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String origin;

    @Column(nullable = false)
    private String destination;

    @Column(nullable = false)
    private int weightKg;

    @Column(nullable = false)
    private LocalDate pickupDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LoadStatus status;

    // SB08 step 4a: map the new column: "reference", NOT NULL, unique (updatable = false: a reference never changes)
    @Column(nullable = false, unique = true, updatable = false, length = 20)
    private String reference;

    // SB09 step 3b: map the new column: "shipper", NOT NULL, can't be changed after the load is created
    @Column(nullable = false, updatable = false, length = 50)
    private String shipper;

    @OneToMany(mappedBy = "load")
    private List<Bid> bids = new ArrayList<>();

    /** For JPA only. It creates the object first, then fills in the fields from the row. */
    protected Load() {
    }

    /** SB09: a new, unsaved load, owned by the shipper (a username) who posted it. */
    public Load(LoadRequest request, String shipper) {
        updateDetails(request);
        this.status = LoadStatus.OPEN;
        this.reference = LoadReference.next();
        this.shipper = shipper;
    }

    /** A load with no known shipper (seed data, imports, tests): it belongs to "system". */
    public Load(LoadRequest request) {
        this(request, "system");
    }

    public void updateDetails(LoadRequest request) {
        this.origin = request.origin();
        this.destination = request.destination();
        this.weightKg = request.weightKg();
        this.pickupDate = request.pickupDate();
    }

    public void cancel() {
        this.status = LoadStatus.CANCELLED;
    }

    public void book() {
        this.status = LoadStatus.BOOKED;
    }

    /**
     * Keeps BOTH sides of the relationship in step. The database only looks at Bid.load, but this object's list
     * wouldn't know about the new bid until the load was next read from the database.
     */
    public void addBid(Bid bid) {
        bids.add(bid);
    }

    /** Read-only view. Reading it outside a transaction throws LazyInitializationException. */
    public List<Bid> getBids() {
        return Collections.unmodifiableList(bids);
    }

    public Long getId() {
        return id;
    }

    public String getOrigin() {
        return origin;
    }

    public String getDestination() {
        return destination;
    }

    public int getWeightKg() {
        return weightKg;
    }

    public LocalDate getPickupDate() {
        return pickupDate;
    }

    public LoadStatus getStatus() {
        return status;
    }

    public String getReference() {
        return reference;
    }

    public String getShipper() {
        return shipper;
    }

    @Override
    public String toString() {
        return "Load[id=" + id + ", " + origin + " -> " + destination + ", " + weightKg + " kg, " + pickupDate + ", " + status + "]";
    }
}

package com.freightboard.bids;

import com.freightboard.carriers.Carrier;
import com.freightboard.carriers.CarrierService;
import com.freightboard.loads.InvalidLoadStateException;
import com.freightboard.loads.Load;
import com.freightboard.loads.LoadRepository;
import com.freightboard.loads.LoadService;
import com.freightboard.loads.LoadStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.util.List;

/**
 * Bidding. Every public method here returns DTOs, built INSIDE the transaction: once a method returns, the
 * transaction has ended, and touching a lazy relationship (bid.getCarrier().getName()) would throw
 * LazyInitializationException.
 */
@Service
public class BidService {

    private final BidRepository bidRepository;
    private final LoadRepository loadRepository;
    private final LoadService loadService;
    private final CarrierService carrierService;
    private final Clock clock;

    public BidService(BidRepository bidRepository, LoadRepository loadRepository, LoadService loadService,
                      CarrierService carrierService, Clock clock) {
        this.bidRepository = bidRepository;
        this.loadRepository = loadRepository;
        this.loadService = loadService;
        this.carrierService = carrierService;
        this.clock = clock;
    }

    /**
     * TODO 2: check these rules IN THIS ORDER, then save a new PENDING bid (placed at clock.instant()):
     *   1. the load exists                       -> loadService.get throws LoadNotFoundException (404)
     *   2. the carrier exists                    -> carrierService.get throws CarrierNotFoundException (404)
     *   3. the load is OPEN                      -> InvalidLoadStateException("Load 7 is BOOKED and can't take bids")
     *   4. the carrier can carry the weight      -> InvalidBidException("Carrier 3 can carry at most 1000 kg, but load 7 weighs 1500 kg")
     *   5. no PENDING bid from this carrier yet  -> InvalidBidException("Carrier 3 already has a pending bid on load 7")
     * Keep both sides of the relationship in step: load.addBid(bid). Then bidRepository.save(bid).
     */
    @Transactional
    public BidResponse placeBid(long loadId, BidRequest request) {
        return null;
    }

    /**
     * TODO 3: accept a bid. ALL of these changes happen, or NONE of them:
     *   - the bid must exist        -> BidNotFoundException (404)
     *   - the bid must be PENDING   -> InvalidBidException("Bid 5 is REJECTED, not PENDING")
     *   - its load must be OPEN     -> InvalidLoadStateException("Load 7 is CANCELLED and can't be booked")
     *   - the bid becomes ACCEPTED, every OTHER pending bid on the same load becomes REJECTED,
     *     and the load becomes BOOKED
     * No save calls needed: every entity here is managed, so dirty checking writes the changes at commit.
     */
    @Transactional
    public BidResponse acceptBid(long bidId) {
        return null;
    }

    /**
     * TODO 4: every bid on a load, cheapest first (then by id), as DTOs.
     *         A missing load is a 404 (LoadNotFoundException), not an empty list.
     *         Needs a transaction for the lazy carrier names: a READ-ONLY one is enough, and lets the database
     *         and Hibernate skip work they'd do for writes.
     */
    public List<BidResponse> bidsForLoad(long loadId) {
        return List.of();
    }

    /**
     * TODO 5b: a summary row per load with the given status, in id order: how many bids it has (of any status)
     *          and its lowest PENDING bid (null if none).
     *          Use loadRepository.findWithBidsByStatusOrderByIdAsc (TODO 5a), so this runs ONE query, not 1 + N.
     */
    @Transactional(readOnly = true)
    public List<LoadBidSummary> summaries(LoadStatus status) {
        return List.of();
    }
}

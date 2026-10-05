package com.freightboard.bids;

import com.freightboard.carriers.Carrier;
import com.freightboard.carriers.CarrierService;
import com.freightboard.loads.InvalidLoadStateException;
import com.freightboard.loads.Load;
import com.freightboard.loads.LoadRepository;
import com.freightboard.loads.LoadService;
import com.freightboard.loads.LoadStatus;
import org.springframework.security.access.AccessDeniedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.util.List;

/**
 * Bidding. Every public method here returns DTOs, built INSIDE the transaction: once a method returns, the
 * transaction has ended, and touching a lazy relationship (bid.getCarrier().getName()) would throw
 * LazyInitializationException.
 */
/**
 * TODO 3a: after saving a bid, publish a BidPlaced event; after accepting one, publish BidAccepted (with how many
 *          others were rejected). events.publishEvent(new BidPlaced(...)). The metrics listen for these (TODO 3b).
 * TODO 4a: LOG both actions at INFO, through the class's SLF4J logger, with {} placeholders (never string '+'):
 *            "Bid 5 placed on load 7 by carrier 3 for 9000p"
 *            "Bid 5 accepted on load 7, 2 other bid(s) rejected"
 *          Log IDs, not names or usernames: logs are copied, kept for a long time, and read by many people,
 *          and personal data in them is a GDPR problem.
 */
@Service
public class BidService {

    private static final Logger log = LoggerFactory.getLogger(BidService.class);

    private final BidRepository bidRepository;
    private final LoadRepository loadRepository;
    private final LoadService loadService;
    private final CarrierService carrierService;
    private final Clock clock;
    private final ApplicationEventPublisher events;

    public BidService(BidRepository bidRepository, LoadRepository loadRepository, LoadService loadService,
                      CarrierService carrierService, Clock clock, ApplicationEventPublisher events) {
        this.bidRepository = bidRepository;
        this.loadRepository = loadRepository;
        this.loadService = loadService;
        this.carrierService = carrierService;
        this.clock = clock;
        this.events = events;
    }

    /**
     * Places a bid (the rules are from SB07).
     * SB09 step 4: the carrier is no longer in the request: it's the carrier linked to the logged-in user.
     *         Replace the carrierService.get(request.carrierId()) lookup with carrierService.forUser(username)
     *         (which throws AccessDeniedException -> 403 if this login doesn't bid for any carrier).
     *         Keep the order: load first, then the carrier.
     */
    @Transactional
    public BidResponse placeBid(long loadId, String username, BidRequest request) {
        Load load = loadService.get(loadId);
        Carrier carrier = carrierService.forUser(username);
        if (load.getStatus() != LoadStatus.OPEN) {
            throw new InvalidLoadStateException("Load " + loadId + " is " + load.getStatus() + " and can't take bids");
        }
        if (load.getWeightKg() > carrier.getMaxWeightKg()) {
            throw new InvalidBidException("Carrier " + carrier.getId() + " can carry at most " + carrier.getMaxWeightKg()
                    + " kg, but load " + loadId + " weighs " + load.getWeightKg() + " kg");
        }
        if (bidRepository.existsByLoadIdAndCarrierIdAndStatus(loadId, carrier.getId(), BidStatus.PENDING)) {
            throw new InvalidBidException("Carrier " + carrier.getId() + " already has a pending bid on load " + loadId);
        }
        Bid bid = new Bid(load, carrier, request.amountPence(), clock.instant());
        load.addBid(bid);
        return BidResponse.from(bidRepository.save(bid));
    }

    /**
     * Accepts a bid (the rules are from SB07).
     * SB09 step 5b: only the shipper who OWNS the bid's load may accept it. Otherwise
     *          AccessDeniedException("Load 7 belongs to another shipper"). Check it right after finding the bid,
     *          before any other rule.
     */
    @Transactional
    public BidResponse acceptBid(long bidId, String username) {
        Bid bid = bidRepository.findById(bidId).orElseThrow(() -> new BidNotFoundException(bidId));
        if (!bid.getLoad().getShipper().equals(username)) {
            throw new AccessDeniedException("Load " + bid.getLoad().getId() + " belongs to another shipper");
        }
        if (bid.getStatus() != BidStatus.PENDING) {
            throw new InvalidBidException("Bid " + bidId + " is " + bid.getStatus() + ", not PENDING");
        }
        Load load = bid.getLoad();
        if (load.getStatus() != LoadStatus.OPEN) {
            throw new InvalidLoadStateException("Load " + load.getId() + " is " + load.getStatus() + " and can't be booked");
        }
        bid.accept();
        load.getBids().stream()
                .filter(other -> other != bid && other.getStatus() == BidStatus.PENDING)
                .forEach(Bid::reject);
        load.book();
        return BidResponse.from(bid);
    }

    /**
     * Every bid on a load, cheapest first (then by id), as DTOs.
     *         A missing load is a 404 (LoadNotFoundException), not an empty list.
     *         Needs a transaction for the lazy carrier names: a READ-ONLY one is enough, and lets the database
     *         and Hibernate skip work they'd do for writes.
     */
    @Transactional(readOnly = true)
    public List<BidResponse> bidsForLoad(long loadId) {
        loadService.get(loadId);
        return bidRepository.findByLoadIdOrderByAmountPenceAscIdAsc(loadId).stream().map(BidResponse::from).toList();
    }

    /**
     * A summary row per load with the given status, in id order: how many bids it has (of any status)
     *          and its lowest PENDING bid (null if none).
     *          Use loadRepository.findWithBidsByStatusOrderByIdAsc (SB07 step 5a), so this runs ONE query, not 1 + N.
     */
    @Transactional(readOnly = true)
    public List<LoadBidSummary> summaries(LoadStatus status) {
        return loadRepository.findWithBidsByStatusOrderByIdAsc(status).stream()
                .map(load -> new LoadBidSummary(load.getId(), load.getOrigin(), load.getDestination(),
                        load.getBids().size(),
                        load.getBids().stream()
                                .filter(bid -> bid.getStatus() == BidStatus.PENDING)
                                .map(Bid::getAmountPence)
                                .min(Long::compare)
                                .orElse(null)))
                .toList();
    }
}

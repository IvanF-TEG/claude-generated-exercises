package com.freightboard.bids;

import com.freightboard.carriers.Carrier;
import com.freightboard.carriers.CarrierNotFoundException;
import com.freightboard.carriers.CarrierRepository;
import com.freightboard.carriers.CarrierService;
import com.freightboard.loads.InvalidLoadStateException;
import com.freightboard.loads.Load;
import com.freightboard.loads.LoadNotFoundException;
import com.freightboard.loads.LoadRepository;
import com.freightboard.loads.LoadRequest;
import com.freightboard.loads.LoadService;
import com.freightboard.loads.LoadStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

// Real database, and NO test-wide transaction: every service call commits (or rolls back) on its own.
@DataJpaTest
@Import({BidService.class, LoadService.class, CarrierService.class, FixedClockConfig.class})
@Transactional(propagation = Propagation.NOT_SUPPORTED)
class BidServiceTest {

    static final LocalDate DAY = LocalDate.of(2031, 3, 1);

    @Autowired
    BidService bidService;

    @Autowired
    LoadService loadService;

    @Autowired
    BidRepository bidRepository;

    @Autowired
    LoadRepository loadRepository;

    @Autowired
    CarrierRepository carrierRepository;

    Load load;
    Carrier pennine;
    Carrier dales;
    Carrier smallVan;

    @BeforeEach
    void setUp() {
        bidRepository.deleteAll();
        loadRepository.deleteAll();
        carrierRepository.deleteAll();
        load = loadService.create(new LoadRequest("LS1", "M1", 1500, DAY));
        pennine = carrierRepository.save(new Carrier("Pennine Haulage", 20000));
        dales = carrierRepository.save(new Carrier("Dales Freight", 44000));
        smallVan = carrierRepository.save(new Carrier("Small Van Co", 1000));
    }

    BidResponse bid(Carrier carrier, long pence) {
        return bidService.placeBid(load.getId(), new BidRequest(carrier.getId(), pence));
    }

    LoadStatus loadStatus() {
        return loadRepository.findById(load.getId()).orElseThrow().getStatus();
    }

    BidStatus bidStatus(BidResponse bid) {
        return bidRepository.findById(bid.id()).orElseThrow().getStatus();
    }

    @Nested
    class Todo2PlaceBid {

        @Test
        void savesAPendingBid() {
            BidResponse bid = bid(pennine, 9000);
            assertEquals(new BidResponse(bid.id(), load.getId(), pennine.getId(), "Pennine Haulage", 9000,
                    BidStatus.PENDING, FixedClockConfig.NOW), bid);
            assertEquals(1, bidRepository.count());
        }

        @Test
        void missingLoad() {
            assertThrows(LoadNotFoundException.class,
                    () -> bidService.placeBid(-1, new BidRequest(pennine.getId(), 9000)));
        }

        @Test
        void missingCarrier() {
            assertThrows(CarrierNotFoundException.class,
                    () -> bidService.placeBid(load.getId(), new BidRequest(-1L, 9000)));
        }

        @Test
        void loadMustBeOpen() {
            loadService.cancel(load.getId());
            var e = assertThrows(InvalidLoadStateException.class, () -> bid(pennine, 9000));
            assertEquals("Load " + load.getId() + " is CANCELLED and can't take bids", e.getMessage());
        }

        @Test
        void carrierMustBeBigEnough() {
            var e = assertThrows(InvalidBidException.class, () -> bid(smallVan, 5000));
            assertEquals("Carrier " + smallVan.getId() + " can carry at most 1000 kg, but load " + load.getId()
                    + " weighs 1500 kg", e.getMessage());
        }

        @Test
        void onePendingBidPerCarrier() {
            bid(pennine, 9000);
            var e = assertThrows(InvalidBidException.class, () -> bid(pennine, 8500));
            assertEquals("Carrier " + pennine.getId() + " already has a pending bid on load " + load.getId(), e.getMessage());
            assertEquals(1, bidRepository.count());
        }

        @Test
        void loadCheckedBeforeCarrier() {
            // rule 1 comes before rule 2: both are missing, so it's the LOAD that's reported
            assertThrows(LoadNotFoundException.class, () -> bidService.placeBid(-1, new BidRequest(-1L, 9000)));
        }
    }

    @Nested
    class Todo3AcceptBid {

        @Test
        void acceptsOneRejectsTheRestBooksTheLoad() {
            BidResponse cheap = bid(pennine, 8000);
            BidResponse dear = bid(dales, 9500);
            BidResponse accepted = bidService.acceptBid(dear.id());
            assertEquals(BidStatus.ACCEPTED, accepted.status());
            assertEquals(BidStatus.ACCEPTED, bidStatus(dear));
            assertEquals(BidStatus.REJECTED, bidStatus(cheap));
            assertEquals(LoadStatus.BOOKED, loadStatus());
        }

        @Test
        void missingBid() {
            assertThrows(BidNotFoundException.class, () -> bidService.acceptBid(-1));
        }

        @Test
        void onlyPendingBids() {
            BidResponse first = bid(pennine, 8000);
            BidResponse second = bid(dales, 9500);
            bidService.acceptBid(second.id());
            var e = assertThrows(InvalidBidException.class, () -> bidService.acceptBid(first.id()));
            assertEquals("Bid " + first.id() + " is REJECTED, not PENDING", e.getMessage());
        }

        @Test
        void loadMustStillBeOpenAndNothingChanges() {
            BidResponse bid = bid(pennine, 8000);
            loadService.cancel(load.getId());
            var e = assertThrows(InvalidLoadStateException.class, () -> bidService.acceptBid(bid.id()));
            assertEquals("Load " + load.getId() + " is CANCELLED and can't be booked", e.getMessage());
            assertEquals(BidStatus.PENDING, bidStatus(bid));
            assertEquals(LoadStatus.CANCELLED, loadStatus());
        }
    }

    @Nested
    class Todo4BidsForLoad {

        @Test
        void cheapestFirstWithCarrierNames() {
            bid(dales, 9500);
            bid(pennine, 8000);
            List<String> names = bidService.bidsForLoad(load.getId()).stream().map(BidResponse::carrierName).toList();
            assertEquals(List.of("Pennine Haulage", "Dales Freight"), names);
        }

        @Test
        void noBidsIsAnEmptyList() {
            assertEquals(List.of(), bidService.bidsForLoad(load.getId()));
        }

        @Test
        void missingLoadIs404() {
            assertThrows(LoadNotFoundException.class, () -> bidService.bidsForLoad(-1));
        }
    }

    @Nested
    class Todo6aDeleteGuard {

        @Test
        void loadWithBidsCantBeDeleted() {
            bid(pennine, 8000);
            var e = assertThrows(InvalidLoadStateException.class, () -> loadService.delete(load.getId()));
            assertEquals("Load " + load.getId() + " has bids and can't be deleted", e.getMessage());
        }

        @Test
        void loadWithoutBidsCanBe() {
            loadService.delete(load.getId());
            assertEquals(0, loadRepository.count());
        }

        @Test
        void missingLoad() {
            assertThrows(LoadNotFoundException.class, () -> loadService.delete(-1));
        }
    }
}

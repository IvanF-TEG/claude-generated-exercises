package com.freightboard.bids;

import com.freightboard.carriers.Carrier;
import com.freightboard.loads.Load;
import com.freightboard.loads.LoadRequest;
import org.hibernate.Hibernate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

// SB07 step 1: the relationships between Bid, Load and Carrier.
@DataJpaTest
class BidMappingTest {

    @Autowired
    TestEntityManager entityManager;

    @Autowired
    BidRepository bidRepository;

    long saveOneBid() {
        Carrier carrier = entityManager.persist(new Carrier("Pennine Haulage", 20000));
        Load load = entityManager.persist(new Load(new LoadRequest("LS1", "M1", 1500, LocalDate.of(2031, 3, 1))));
        Bid bid = new Bid(load, carrier, 9000, FixedClockConfig.NOW);
        load.addBid(bid);
        entityManager.persist(bid);
        entityManager.flush();
        entityManager.clear();
        return bid.getId();
    }

    @Test
    void foreignKeyColumns() {
        saveOneBid();
        List<?> columns = entityManager.getEntityManager().createNativeQuery(
                        "select column_name from information_schema.columns where table_name = 'BIDS' "
                                + "and column_name like '%_ID' order by column_name")
                .getResultList();
        assertEquals(List.of("CARRIER_ID", "LOAD_ID"), columns);
    }

    @Test
    void bidKnowsItsLoadAndCarrier() {
        Bid bid = bidRepository.findById(saveOneBid()).orElseThrow();
        assertEquals("LS1", bid.getLoad().getOrigin());
        assertEquals("Pennine Haulage", bid.getCarrier().getName());
    }

    @Test
    void relationshipsAreLazy() {
        Bid bid = bidRepository.findById(saveOneBid()).orElseThrow();
        assertFalse(Hibernate.isInitialized(bid.getLoad()), "bid.load should be LAZY");
        assertFalse(Hibernate.isInitialized(bid.getCarrier()), "bid.carrier should be LAZY");
    }

    @Test
    void loadSeesItsBidsThroughMappedBy() {
        long bidId = saveOneBid();
        Load load = bidRepository.findById(bidId).orElseThrow().getLoad();
        entityManager.clear();
        Load reloaded = entityManager.find(Load.class, load.getId());
        assertEquals(1, reloaded.getBids().size());
        assertEquals(bidId, reloaded.getBids().getFirst().getId());
    }

    @Test
    void noJoinTableWasCreated() {
        saveOneBid();
        Number tables = (Number) entityManager.getEntityManager().createNativeQuery(
                "select count(*) from information_schema.tables where table_name like 'LOADS_BIDS%'").getSingleResult();
        assertEquals(0, tables.intValue(), "a LOADS_BIDS join table means mappedBy is missing on Load.bids");
    }
}

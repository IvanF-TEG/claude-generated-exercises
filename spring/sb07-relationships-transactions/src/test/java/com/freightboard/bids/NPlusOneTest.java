package com.freightboard.bids;

import com.freightboard.carriers.Carrier;
import com.freightboard.carriers.CarrierService;
import com.freightboard.loads.Load;
import com.freightboard.loads.LoadRequest;
import com.freightboard.loads.LoadService;
import com.freightboard.loads.LoadStatus;
import jakarta.persistence.EntityManagerFactory;
import org.hibernate.SessionFactory;
import org.hibernate.stat.Statistics;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.context.annotation.Import;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * TODO 5: the N+1 problem. Hibernate's statistics count every SQL statement actually sent to the database.
 * Loading 3 loads and then touching each one's bids LAZILY costs 1 query for the loads + 1 per load = 4.
 * With an entity graph, it's a single query with a join.
 */
@DataJpaTest(properties = "spring.jpa.properties.hibernate.generate_statistics=true")
@Import({BidService.class, LoadService.class, CarrierService.class, FixedClockConfig.class})
class NPlusOneTest {

    @Autowired
    TestEntityManager entityManager;

    @Autowired
    EntityManagerFactory entityManagerFactory;

    @Autowired
    BidService bidService;

    Statistics statistics;
    List<Long> loadIds;

    @BeforeEach
    void setUp() {
        Carrier a = entityManager.persist(new Carrier("Carrier A", 44000));
        Carrier b = entityManager.persist(new Carrier("Carrier B", 44000));
        loadIds = new java.util.ArrayList<>();
        for (String origin : List.of("LS1", "HU1", "YO1")) {
            Load load = entityManager.persist(new Load(new LoadRequest(origin, "M1", 1500, LocalDate.of(2031, 3, 1))));
            loadIds.add(load.getId());
            for (Carrier carrier : List.of(a, b)) {
                Bid bid = new Bid(load, carrier, carrier == a ? 7000 : 6500, FixedClockConfig.NOW);
                load.addBid(bid);
                entityManager.persist(bid);
            }
        }
        entityManager.persist(new Load(new LoadRequest("BS1", "M1", 800, LocalDate.of(2031, 3, 2))));
        entityManager.flush();
        entityManager.clear();
        statistics = entityManagerFactory.unwrap(SessionFactory.class).getStatistics();
        statistics.clear();
    }

    @Test
    void summariesAreCorrect() {
        List<LoadBidSummary> summaries = bidService.summaries(LoadStatus.OPEN);
        assertEquals(4, summaries.size());
        assertEquals(new LoadBidSummary(loadIds.get(0), "LS1", "M1", 2, 6500L), summaries.get(0));
        assertEquals(0, summaries.get(3).bidCount());
        assertEquals(null, summaries.get(3).lowestPendingBidPence());
    }

    @Test
    void summariesUseOneQuery() {
        bidService.summaries(LoadStatus.OPEN);
        assertEquals(1, statistics.getPrepareStatementCount(),
                "expected 1 SQL statement; more than 1 means the bids are loaded lazily, one query per load");
    }
}

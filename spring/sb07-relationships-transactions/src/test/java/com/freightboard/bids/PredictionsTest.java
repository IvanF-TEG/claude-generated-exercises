package com.freightboard.bids;

import com.freightboard.carriers.Carrier;
import com.freightboard.carriers.CarrierRepository;
import com.freightboard.loads.Load;
import com.freightboard.loads.LoadRepository;
import com.freightboard.loads.LoadRequest;
import com.freightboard.loads.LoadStatus;
import jakarta.persistence.EntityManagerFactory;
import org.hibernate.SessionFactory;
import org.hibernate.stat.Statistics;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

// PREDICTIONS: finish TODOs 1 and 5a first. Then replace each -1 / "???" with what you expect, and only THEN run.
// Keep a comment on any you got wrong. There's no test-wide transaction here: each call commits or rolls back.
@DataJpaTest(properties = "spring.jpa.properties.hibernate.generate_statistics=true")
@Import(PredictionsTest.Experiments.class)
@Transactional(propagation = Propagation.NOT_SUPPORTED)
class PredictionsTest {

    /** A bean, so calls from the test go through Spring's transaction proxy. */
    static class Experiments {

        private final LoadRepository loads;

        Experiments(LoadRepository loads) {
            this.loads = loads;
        }

        static Load newLoad() {
            return new Load(new LoadRequest("LS1", "M1", 1500, LocalDate.of(2031, 3, 1)));
        }

        @Transactional
        public void saveThenThrowChecked() throws Exception {
            loads.save(newLoad());
            throw new Exception("checked");
        }

        @Transactional
        public void saveThenThrowUnchecked() {
            loads.save(newLoad());
            throw new IllegalStateException("unchecked");
        }

        @Transactional(rollbackFor = Exception.class)
        public void saveThenThrowCheckedWithRollbackFor() throws Exception {
            loads.save(newLoad());
            throw new Exception("checked");
        }

        /** NOT transactional itself: it calls a @Transactional method on 'this'. */
        public void callsItsOwnTransactionalMethod() {
            saveThenThrowUnchecked();
        }
    }

    @Autowired
    Experiments experiments;

    @Autowired
    LoadRepository loadRepository;

    @Autowired
    BidRepository bidRepository;

    @Autowired
    CarrierRepository carrierRepository;

    @Autowired
    EntityManagerFactory entityManagerFactory;

    @BeforeEach
    void cleanUp() {
        bidRepository.deleteAll();
        loadRepository.deleteAll();
        carrierRepository.deleteAll();
    }

    long loadsAfter(ThrowingRunnable action) {
        try {
            action.run();
        } catch (Exception expected) {
            // every experiment throws: what matters is what got committed
        }
        return loadRepository.count();
    }

    interface ThrowingRunnable {
        void run() throws Exception;
    }

    @Test
    void checkedException() {
        assertEquals(-1, loadsAfter(experiments::saveThenThrowChecked));
    }

    @Test
    void uncheckedException() {
        assertEquals(-1, loadsAfter(experiments::saveThenThrowUnchecked));
    }

    @Test
    void checkedExceptionWithRollbackFor() {
        assertEquals(-1, loadsAfter(experiments::saveThenThrowCheckedWithRollbackFor));
    }

    @Test
    void selfInvocation() {
        assertEquals(-1, loadsAfter(experiments::callsItsOwnTransactionalMethod));
    }

    @Test
    void lazyCollectionOutsideATransaction() {
        long id = loadRepository.save(Experiments.newLoad()).getId();
        Load load = loadRepository.findById(id).orElseThrow();
        String result;
        try {
            result = "size " + load.getBids().size();
        } catch (RuntimeException e) {
            result = e.getClass().getSimpleName();
        }
        assertEquals("???", result);
    }

    @Test
    void statementsForTheNaiveLoop() {
        Carrier carrier = carrierRepository.save(new Carrier("Predictable Haulage", 44000));
        for (int i = 0; i < 5; i++) {
            Load load = loadRepository.save(Experiments.newLoad());
            bidRepository.save(new Bid(load, carrier, 9000, FixedClockConfig.NOW));
        }
        Statistics statistics = entityManagerFactory.unwrap(SessionFactory.class).getStatistics();
        statistics.clear();
        countBidsTheNaiveWay();
        assertEquals(-1, statistics.getPrepareStatementCount());
    }

    @Autowired
    org.springframework.transaction.support.TransactionTemplate transactions;

    /** 5 loads, each with 1 bid. No entity graph: the plain derived query from SB06. */
    int countBidsTheNaiveWay() {
        return transactions.execute(status -> loadRepository.findByStatusOrderByIdAsc(LoadStatus.OPEN).stream()
                .mapToInt(load -> load.getBids().size())
                .sum());
    }
}

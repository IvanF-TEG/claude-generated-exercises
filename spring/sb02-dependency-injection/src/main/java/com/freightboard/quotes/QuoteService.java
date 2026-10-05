package com.freightboard.quotes;


import java.time.Clock;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

/**
 * Asks every PricingStrategy for a price.
 * The strategies and the Clock come in through the CONSTRUCTOR. This class never creates them itself,
 * so a test can pass in fakes and Spring can pass in the real ones.
 * TODO 5a (later): make this a Spring bean with @Service.
 */
public class QuoteService {

    private final List<PricingStrategy> strategies;
    private final Clock clock;

    // TODO 3a: store both arguments. No 'new' in here!
    //          (Spring injects through the constructor automatically when there's only one. @Autowired is optional.)
    public QuoteService(List<PricingStrategy> strategies, Clock clock) {
        this.strategies = null;
        this.clock = null;
    }

    /** TODO 3b: the strategy names, sorted alphabetically. */
    public List<String> strategyNames() {
        return List.of();
    }

    /**
     * TODO 3c: one Quote per strategy, cheapest first. On a tie, sort by strategy name.
     * Every quote in one call has the SAME quotedAt: read the clock once, with clock.instant().
     */
    public List<Quote> quoteAll(QuoteRequest request) {
        return List.of();
    }

    /** TODO 3d: the first quote from quoteAll. */
    public Quote cheapest(QuoteRequest request) {
        return null;
    }

    /** TODO 3e: a quote from the strategy with this name, or Optional.empty() if there isn't one. */
    public Optional<Quote> quoteWith(String strategyName, QuoteRequest request) {
        return Optional.empty();
    }
}

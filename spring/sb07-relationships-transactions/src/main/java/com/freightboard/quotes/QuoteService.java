package com.freightboard.quotes;

import org.springframework.stereotype.Service;

import java.time.Clock;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

/**
 * Asks every PricingStrategy for a price.
 * The strategies and the Clock come in through the CONSTRUCTOR. This class never creates them itself,
 * so a test can pass in fakes and Spring can pass in the real ones.
 * SB02 step 5a: make this a Spring bean with @Service.
 */
@Service
public class QuoteService {

    private final List<PricingStrategy> strategies;
    private final Clock clock;

    // SB02 step 3a: store both arguments. No 'new' in here!
    //          (Spring injects through the constructor automatically when there's only one. @Autowired is optional.)
    public QuoteService(List<PricingStrategy> strategies, Clock clock) {
        this.strategies = List.copyOf(strategies);
        this.clock = clock;
    }

    /** SB02 step 3b: the strategy names, sorted alphabetically. */
    public List<String> strategyNames() {
        return strategies.stream().map(PricingStrategy::name).sorted().toList();
    }

    /**
     * SB02 step 3c: one Quote per strategy, cheapest first. On a tie, sort by strategy name.
     * Every quote in one call has the SAME quotedAt: read the clock once, with clock.instant().
     */
    public List<Quote> quoteAll(QuoteRequest request) {
        var now = clock.instant();
        return strategies.stream()
                .map(s -> new Quote(s.name(), s.pricePence(request), now))
                .sorted(Comparator.comparingLong(Quote::pricePence).thenComparing(Quote::strategy))
                .toList();
    }

    /** SB02 step 3d: the first quote from quoteAll. */
    public Quote cheapest(QuoteRequest request) {
        return quoteAll(request).getFirst();
    }

    /** SB02 step 3e: a quote from the strategy with this name, or Optional.empty() if there isn't one. */
    public Optional<Quote> quoteWith(String strategyName, QuoteRequest request) {
        return strategies.stream()
                .filter(s -> s.name().equals(strategyName))
                .findFirst()
                .map(s -> new Quote(s.name(), s.pricePence(request), clock.instant()));
    }
}

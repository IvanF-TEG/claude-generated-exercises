package com.freightboard.quotes;

import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

// This is the pay-off of constructor injection: we hand QuoteService FAKE strategies and a FIXED clock,
// so every answer is predictable. No Spring, no real pricing rules, no "what time is it now?".
class QuoteServiceTest {

    static final Instant NINE_AM = Instant.parse("2026-01-05T09:00:00Z");
    static final Clock FIXED = Clock.fixed(NINE_AM, ZoneOffset.UTC);
    static final QuoteRequest REQUEST = new QuoteRequest("LS1", "M1", 70, 1500);

    static PricingStrategy fake(String name, long price) {
        return new PricingStrategy() {
            @Override
            public String name() {
                return name;
            }

            @Override
            public long pricePence(QuoteRequest request) {
                return price;
            }
        };
    }

    final QuoteService service = new QuoteService(
            List.of(fake("zeta", 500), fake("alpha", 900), fake("beta", 500)), FIXED);

    @Test
    void todo3aKeepsItsOwnCopyOfTheList() {
        List<PricingStrategy> mine = new ArrayList<>(List.of(fake("alpha", 1)));
        QuoteService s = new QuoteService(mine, FIXED);
        mine.add(fake("beta", 2));
        assertEquals(List.of("alpha"), s.strategyNames());
    }

    @Test
    void todo3bNamesAreSorted() {
        assertEquals(List.of("alpha", "beta", "zeta"), service.strategyNames());
    }

    @Test
    void todo3cCheapestFirstThenByName() {
        List<Quote> expected = List.of(
                new Quote("beta", 500, NINE_AM),
                new Quote("zeta", 500, NINE_AM),
                new Quote("alpha", 900, NINE_AM));
        assertEquals(expected, service.quoteAll(REQUEST));
    }

    @Test
    void todo3dCheapest() {
        assertEquals(new Quote("beta", 500, NINE_AM), service.cheapest(REQUEST));
    }

    @Test
    void todo3eQuoteWithKnownStrategy() {
        assertEquals(Optional.of(new Quote("alpha", 900, NINE_AM)), service.quoteWith("alpha", REQUEST));
    }

    @Test
    void todo3eQuoteWithUnknownStrategy() {
        assertTrue(service.quoteWith("teleport", REQUEST).isEmpty());
    }
}

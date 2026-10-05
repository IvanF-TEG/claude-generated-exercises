package com.freightboard.quotes;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ActiveProfiles("promo")
class PromoProfileTest {

    @Autowired
    QuoteService quoteService;

    static final QuoteRequest REQUEST = new QuoteRequest("LS1", "M1", 70, 1500);

    @Test
    void promoStrategyExists() {
        assertEquals(List.of("distance", "promo", "weight-band"), quoteService.strategyNames());
    }

    @Test
    void twentyPercentOffDistance() {
        assertEquals(8520, quoteService.quoteWith("promo", REQUEST).orElseThrow().pricePence());
    }
}

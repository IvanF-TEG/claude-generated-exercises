package com.freightboard.quotes;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import java.time.Clock;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

// TODO 5: the same object graph as ManualWiring, but built by Spring.
// The ApplicationContext is Spring's "container": the map of every bean it created.
@SpringBootTest
class SpringWiringTest {

    @Autowired
    ApplicationContext context;

    @Test
    void bothStrategiesAreBeans() {
        // By default a scanned bean is named after its class, with a lower-case first letter.
        assertEquals(Set.of("distancePricing", "weightBandPricing"),
                context.getBeansOfType(PricingStrategy.class).keySet());
    }

    @Test
    void quoteServiceReceivedEveryStrategy() {
        QuoteService service = context.getBean(QuoteService.class);
        assertEquals(List.of("distance", "weight-band"), service.strategyNames());
    }

    @Test
    void clockIsASystemUtcClock() {
        Clock clock = context.getBean(Clock.class);
        assertEquals(ZoneOffset.UTC, clock.getZone());
    }

    @Test
    void theControllerGotTheSameServiceInstance() {
        // Beans are SINGLETONS by default: one shared instance for the whole application.
        assertSame(context.getBean(QuoteService.class), context.getBean(QuoteService.class));
    }
}

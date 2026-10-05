package com.freightboard.quotes;

import com.freightboard.FreightBoardApplication;
import org.junit.jupiter.api.Test;
import org.springframework.boot.Banner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

import static org.junit.jupiter.api.Assertions.assertEquals;

// PREDICTIONS: finish TODOs 1-5 first. Then replace each -1 with what you expect, and only THEN run.
// Keep a comment on any you got wrong. Each helper starts the real application (without a web server) with the
// given profiles and command-line arguments, then reads one value.
class PredictionsTest {

    static long pencePerKm(String[] profiles, String... args) {
        try (ConfigurableApplicationContext context = start(profiles, args)) {
            return context.getBean(PricingProperties.class).distance().pencePerKm();
        }
    }

    static long promoPrice(String[] profiles, String... args) {
        try (ConfigurableApplicationContext context = start(profiles, args)) {
            return context.getBean(PromoPricing.class).pricePence(new QuoteRequest("LS1", "M1", 70, 1500));
        }
    }

    static ConfigurableApplicationContext start(String[] profiles, String... args) {
        return new SpringApplicationBuilder(FreightBoardApplication.class)
                .web(WebApplicationType.NONE)
                .bannerMode(Banner.Mode.OFF)
                .profiles(profiles)
                .run(args);
    }

    static String[] profiles(String... names) {
        return names;
    }

    @Test
    void noProfile() {
        assertEquals(-1, pencePerKm(profiles()));
    }

    @Test
    void prodProfile() {
        assertEquals(-1, pencePerKm(profiles("prod")));
    }

    @Test
    void prodProfilePlusACommandLineArgument() {
        assertEquals(-1, pencePerKm(profiles("prod"), "--freightboard.pricing.distance.pence-per-km=150"));
    }

    @Test
    void camelCaseOnTheCommandLine() {
        // "relaxed binding": does pencePerKm find the pence-per-km property?
        assertEquals(-1, pencePerKm(profiles(), "--freightboard.pricing.distance.pencePerKm=120"));
    }

    @Test
    void promoOnly() {
        // 10650p before any discount
        assertEquals(-1, promoPrice(profiles("promo")));
    }

    @Test
    void promoThenProd() {
        // Both files set freightboard.promo.discount-percent. prod ALSO changes the distance rates.
        assertEquals(-1, promoPrice(profiles("promo", "prod")));
    }

    @Test
    void prodThenPromo() {
        assertEquals(-1, promoPrice(profiles("prod", "promo")));
    }
}

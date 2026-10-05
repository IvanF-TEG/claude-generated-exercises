package com.freightboard.bids;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;

// For slice tests: @DataJpaTest doesn't load PricingConfig, so it has no Clock bean unless we add one.
@TestConfiguration
public class FixedClockConfig {

    public static final Instant NOW = Instant.parse("2031-02-01T09:00:00Z");

    @Bean
    Clock clock() {
        return Clock.fixed(NOW, ZoneOffset.UTC);
    }
}

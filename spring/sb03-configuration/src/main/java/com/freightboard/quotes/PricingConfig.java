package com.freightboard.quotes;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;

// TODO 1b: add @EnableConfigurationProperties(PricingProperties.class). This makes a PricingProperties bean,
//          filled in from the configuration files, that anything can inject.
@Configuration
public class PricingConfig {

    @Bean
    public Clock clock() {
        return Clock.systemUTC();
    }
}

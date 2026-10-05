package com.freightboard.quotes;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import java.time.Clock;

// SB03 step 1b: add @EnableConfigurationProperties(PricingProperties.class). This makes a PricingProperties bean,
//          filled in from the configuration files, that anything can inject.
@Configuration
@EnableConfigurationProperties(PricingProperties.class)
public class PricingConfig {

    @Bean
    public Clock clock() {
        return Clock.systemUTC();
    }
}

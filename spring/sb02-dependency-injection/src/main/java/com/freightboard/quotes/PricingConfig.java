package com.freightboard.quotes;


/**
 * TODO 5b: make this a @Configuration class with a @Bean method that returns Clock.systemUTC().
 * You can't put @Component on java.time.Clock (it isn't your class), so a @Bean method is how you
 * hand Spring an object that it can't create by scanning.
 */
public class PricingConfig {
}

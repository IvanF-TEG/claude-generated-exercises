package com.freightboard.quotes;

// GIVEN. QuoteService depends on this INTERFACE, never on a concrete class. That's what makes the strategies swappable.
public interface PricingStrategy {

    /** A short, unique, lower-case name such as "distance". */
    String name();

    long pricePence(QuoteRequest request);
}

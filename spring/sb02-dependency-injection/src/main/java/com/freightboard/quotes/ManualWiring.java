package com.freightboard.quotes;


/**
 * Dependency injection WITHOUT Spring: build the object graph by hand.
 * This is exactly what Spring does for you at start-up, for every bean in the application.
 */
public final class ManualWiring {

    private ManualWiring() {
    }

    /**
     * TODO 4: build a QuoteService with both strategies and the system UTC clock, using 'new' and nothing else.
     */
    public static QuoteService quoteService() {
        return null;
    }

    /** Try it: run this main method. No Spring, no web server, just objects. */
    public static void main(String[] args) {
        QuoteService service = quoteService();
        service.quoteAll(new QuoteRequest("LS1", "M1", 70, 1500)).forEach(System.out::println);
    }
}

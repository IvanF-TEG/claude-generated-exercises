package com.freightboard.quotes;


/**
 * TODO 6: a controller that gets a QuoteService through constructor injection and exposes:
 *   POST /api/quotes             -> every quote (the quoteAll list)
 *   POST /api/quotes/cheapest    -> the cheapest quote
 *   POST /api/quotes/{strategy}  -> the quote from one strategy, or 404 Not Found if there's no such strategy
 * All three take a QuoteRequest as the JSON body. Keep the controller thin: no pricing logic in here.
 */
public class QuoteController {
}

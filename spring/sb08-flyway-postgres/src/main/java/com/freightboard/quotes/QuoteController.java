package com.freightboard.quotes;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * SB02 step 6: a controller that gets a QuoteService through constructor injection and exposes:
 *   POST /api/quotes             -> every quote (the quoteAll list)
 *   POST /api/quotes/cheapest    -> the cheapest quote
 *   POST /api/quotes/{strategy}  -> the quote from one strategy, or 404 Not Found if there's no such strategy
 * All three take a QuoteRequest as the JSON body. Keep the controller thin: no pricing logic in here.
 */
@RestController
public class QuoteController {

    private final QuoteService quoteService;

    public QuoteController(QuoteService quoteService) {
        this.quoteService = quoteService;
    }

    @PostMapping("/api/quotes")
    public List<Quote> all(@RequestBody QuoteRequest request) {
        return quoteService.quoteAll(request);
    }

    @PostMapping("/api/quotes/cheapest")
    public Quote cheapest(@RequestBody QuoteRequest request) {
        return quoteService.cheapest(request);
    }

    @PostMapping("/api/quotes/{strategy}")
    public ResponseEntity<Quote> one(@PathVariable String strategy, @RequestBody QuoteRequest request) {
        return ResponseEntity.of(quoteService.quoteWith(strategy, request));
    }
}

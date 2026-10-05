package com.freightboard.bids;

import com.freightboard.loads.LoadStatus;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

// GIVEN. Thin, like every controller: all the rules live in BidService.
@RestController
public class BidController {

    private final BidService bidService;

    public BidController(BidService bidService) {
        this.bidService = bidService;
    }

    @PostMapping("/api/loads/{loadId}/bids")
    @ResponseStatus(HttpStatus.CREATED)
    public BidResponse place(@PathVariable long loadId, @Valid @RequestBody BidRequest request) {
        return bidService.placeBid(loadId, request);
    }

    @GetMapping("/api/loads/{loadId}/bids")
    public List<BidResponse> forLoad(@PathVariable long loadId) {
        return bidService.bidsForLoad(loadId);
    }

    @PostMapping("/api/bids/{bidId}/accept")
    public BidResponse accept(@PathVariable long bidId) {
        return bidService.acceptBid(bidId);
    }

    @GetMapping("/api/loads/with-bids")
    public List<LoadBidSummary> summaries(@RequestParam(defaultValue = "OPEN") LoadStatus status) {
        return bidService.summaries(status);
    }
}

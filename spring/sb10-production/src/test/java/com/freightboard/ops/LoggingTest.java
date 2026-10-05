package com.freightboard.ops;

import com.freightboard.bids.BidRequest;
import com.freightboard.bids.BidResponse;
import com.freightboard.bids.BidService;
import com.freightboard.carriers.Carrier;
import com.freightboard.loads.Load;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// TODO 4 + 5c: OutputCaptureExtension records everything written to the console during a test.
@SpringBootTest
@AutoConfigureMockMvc
@Import(BidTestData.class)
@ExtendWith(OutputCaptureExtension.class)
class LoggingTest {

    @Autowired
    BidService bidService;

    @Autowired
    BidTestData data;

    @Autowired
    MockMvc mvc;

    @Test
    void placingAndAcceptingAreLogged(CapturedOutput output) {
        Load load = data.load();
        Carrier carrier = data.carrier();
        BidResponse bid = bidService.placeBid(load.getId(), carrier.getUsername(), new BidRequest(9000));
        bidService.acceptBid(bid.id(), load.getShipper());

        String placed = "Bid " + bid.id() + " placed on load " + load.getId() + " by carrier " + carrier.getId() + " for 9000p";
        String accepted = "Bid " + bid.id() + " accepted on load " + load.getId() + ", 0 other bid(s) rejected";
        assertTrue(output.getOut().contains(placed), "missing: " + placed);
        assertTrue(output.getOut().contains(accepted), "missing: " + accepted);
    }

    @Test
    void noUsernamesInTheLog(CapturedOutput output) {
        Load load = data.load();
        Carrier carrier = data.carrier();
        bidService.placeBid(load.getId(), carrier.getUsername(), new BidRequest(9000));
        assertFalse(output.getOut().contains(carrier.getUsername()), "the log contains a username");
        assertFalse(output.getOut().contains(carrier.getName()), "the log contains the carrier's name");
    }

    @Test
    void theRequestIdIsOnTheLine(CapturedOutput output) throws Exception {
        Load load = data.load();
        Carrier carrier = data.carrier();
        mvc.perform(post("/api/loads/" + load.getId() + "/bids")
                        .with(user(carrier.getUsername()).roles("CARRIER"))
                        .header("X-Request-Id", "trace-me-42")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"amountPence\": 7000}"))
                .andExpect(status().isCreated());
        String line = output.getOut().lines().filter(l -> l.contains("for 7000p")).findFirst().orElse("");
        assertTrue(line.contains("[trace-me-42]"), "expected [trace-me-42] on the log line, got: " + line);
    }
}

package com.freightboard.bids;

import com.freightboard.carriers.CarrierNotFoundException;
import com.freightboard.loads.LoadStatus;
import com.freightboard.security.SecurityConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BidController.class)
@Import(SecurityConfig.class)
class BidControllerTest {

    static final BidResponse BID = new BidResponse(5, 7, 3, "Pennine Haulage", 9000, BidStatus.PENDING, FixedClockConfig.NOW);

    @Autowired
    MockMvc mvc;

    @MockitoBean
    BidService bidService;

    @Test
    @WithMockUser(username = "pennine", roles = "CARRIER")
    void todo6bPlaceAsTheLoggedInCarrier() throws Exception {
        given(bidService.placeBid(7, "pennine", new BidRequest(9000))).willReturn(BID);
        mvc.perform(post("/api/loads/7/bids").contentType(MediaType.APPLICATION_JSON).content("""
                        {"amountPence": 9000}
                        """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.carrierName").value("Pennine Haulage"))
                .andExpect(jsonPath("$.placedAt").value("2031-02-01T09:00:00Z"));
    }

    @Test
    @WithMockUser(username = "shipper", roles = "SHIPPER")
    void todo6bAcceptAsTheLoggedInShipper() throws Exception {
        given(bidService.acceptBid(5, "shipper")).willReturn(BID);
        mvc.perform(post("/api/bids/5/accept")).andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "pennine", roles = "CARRIER")
    void invalidBidBodyIs400() throws Exception {
        mvc.perform(post("/api/loads/7/bids").contentType(MediaType.APPLICATION_JSON).content("""
                        {"amountPence": 0}
                        """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.amountPence[0]").value("must be greater than 0"));
    }

    @Test
    @WithMockUser
    void summariesDefaultToOpen() throws Exception {
        given(bidService.summaries(LoadStatus.OPEN)).willReturn(List.of(new LoadBidSummary(7, "LS1", "M1", 0, null)));
        mvc.perform(get("/api/loads/with-bids"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].lowestPendingBidPence").isEmpty());
    }

    @Test
    @WithMockUser(username = "pennine", roles = "CARRIER")
    void carrierNotFound() throws Exception {
        given(bidService.placeBid(anyLong(), any(), any())).willThrow(new CarrierNotFoundException(3));
        mvc.perform(post("/api/loads/7/bids").contentType(MediaType.APPLICATION_JSON).content("""
                        {"amountPence": 9000}
                        """))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.title").value("Carrier not found"));
    }

    @Test
    @WithMockUser(username = "shipper", roles = "SHIPPER")
    void bidNotFound() throws Exception {
        given(bidService.acceptBid(99, "shipper")).willThrow(new BidNotFoundException(99));
        mvc.perform(post("/api/bids/99/accept"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.title").value("Bid not found"));
    }

    @Test
    @WithMockUser(username = "shipper", roles = "SHIPPER")
    void invalidBid() throws Exception {
        given(bidService.acceptBid(5, "shipper")).willThrow(new InvalidBidException("Bid 5 is REJECTED, not PENDING"));
        mvc.perform(post("/api/bids/5/accept"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.title").value("Invalid bid"));
    }
}

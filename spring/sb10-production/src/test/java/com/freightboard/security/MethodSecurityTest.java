package com.freightboard.security;

import com.freightboard.loads.LoadNotFoundException;
import com.freightboard.loads.LoadService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.test.context.support.WithMockUser;

import static org.junit.jupiter.api.Assertions.assertThrows;

// SB09 step 1b: calling the SERVICE directly, with no HTTP request and no URL rules in the way.
@SpringBootTest
class MethodSecurityTest {

    @Autowired
    LoadService loadService;

    @Test
    @WithMockUser(roles = "SHIPPER")
    void shipperCantDeleteEvenWithoutHttp() {
        assertThrows(AccessDeniedException.class, () -> loadService.delete(999));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void adminGetsPastTheCheck() {
        // past the security check, so the normal "no such load" rule runs
        assertThrows(LoadNotFoundException.class, () -> loadService.delete(999));
    }
}

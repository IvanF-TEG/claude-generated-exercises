package com.freightboard;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

// Starts the whole application once. If the context can't start (a broken bean, bad config), this fails first.
@SpringBootTest
class FreightBoardApplicationTests {

    @Test
    void contextLoads() {
    }
}

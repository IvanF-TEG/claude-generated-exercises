package com.freightboard;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// GIVEN. @SpringBootApplication = @Configuration + @EnableAutoConfiguration + @ComponentScan.
// Component scanning starts from THIS package, so every class in com.freightboard.* is found automatically.
@SpringBootApplication
public class FreightBoardApplication {

    public static void main(String[] args) {
        SpringApplication.run(FreightBoardApplication.class, args);
    }
}

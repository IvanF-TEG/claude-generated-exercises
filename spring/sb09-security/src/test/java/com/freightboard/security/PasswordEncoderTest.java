package com.freightboard.security;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PasswordEncoderTest {

    final PasswordEncoder encoder = new SecurityConfig().passwordEncoder();

    @Test
    void todo2aHashesWithBcryptAndSaysSo() {
        String hash = encoder.encode("freight123");
        assertTrue(hash.startsWith("{bcrypt}$2"), hash);
        assertFalse(hash.contains("freight123"));
    }

    @Test
    void todo2aCanCheckAPassword() {
        String hash = encoder.encode("freight123");
        assertTrue(encoder.matches("freight123", hash));
        assertFalse(encoder.matches("Freight123", hash));
    }

    @Test
    void todo2aSamePasswordDifferentHash() {
        // bcrypt adds a random SALT, so equal passwords don't give equal hashes
        assertNotEquals(encoder.encode("freight123"), encoder.encode("freight123"));
    }
}

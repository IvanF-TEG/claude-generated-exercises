package com.freightboard.loads;

import java.security.SecureRandom;

/**
 * GIVEN. References for NEW loads: "FB-" + 6 random letters/digits (FB-7K2Q9X). The ids aren't known until the
 * row is inserted, so new loads can't use the FB-000042 form that V3 gives to existing rows. They're random, so
 * they can't be guessed, and they never contain an id-shaped number that could clash with a backfilled one.
 */
public final class LoadReference {

    private static final String ALPHABET = "ABCDEFGHJKLMNPQRSTUVWXYZ";
    private static final String ALPHABET_AND_DIGITS = ALPHABET + "23456789";
    private static final SecureRandom RANDOM = new SecureRandom();

    private LoadReference() {
    }

    /** Always contains at least one letter, so it can never look like a backfilled FB-000042. */
    public static String next() {
        StringBuilder sb = new StringBuilder("FB-");
        sb.append(ALPHABET.charAt(RANDOM.nextInt(ALPHABET.length())));
        for (int i = 0; i < 5; i++) {
            sb.append(ALPHABET_AND_DIGITS.charAt(RANDOM.nextInt(ALPHABET_AND_DIGITS.length())));
        }
        return sb.toString();
    }
}

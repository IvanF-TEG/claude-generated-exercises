package com.freightboard.postcodes;

import java.util.Set;

/**
 * Facts about a UK outward code (the first half of a postcode, e.g. "LS1" in "LS1 4AP").
 * area   = the leading letters ("LS", "M", "EC")
 * london = true if the area is one of the London areas below
 */
public record PostcodeInfo(String outwardCode, String area, boolean london) {

    private static final Set<String> LONDON_AREAS = Set.of("E", "EC", "N", "NW", "SE", "SW", "W", "WC");

    /** One or two letters, a digit, then optionally one more letter or digit: LS1, M1, EC1A, SW19. */
    public static final String OUTWARD_CODE_PATTERN = "[A-Z]{1,2}[0-9][A-Z0-9]?";

    /**
     * SB01 step 3a: build a PostcodeInfo from raw user input.
     *   - trim and upper-case it first: " ls1 " -> "LS1"
     *   - if it doesn't match OUTWARD_CODE_PATTERN, throw IllegalArgumentException("not an outward code: " + raw)
     *   - area is the letters before the first digit
     */
    public static PostcodeInfo from(String raw) {
        String code = raw.trim().toUpperCase();
        if (!code.matches(OUTWARD_CODE_PATTERN)) {
            throw new IllegalArgumentException("not an outward code: " + raw);
        }
        String area = code.replaceAll("[0-9].*", "");
        return new PostcodeInfo(code, area, LONDON_AREAS.contains(area));
    }
}

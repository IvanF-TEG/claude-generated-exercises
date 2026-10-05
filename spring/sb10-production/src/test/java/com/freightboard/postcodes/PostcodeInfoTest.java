package com.freightboard.postcodes;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

// A plain unit test: no Spring at all. Keep your logic in plain classes and it stays this easy to test.
class PostcodeInfoTest {

    @ParameterizedTest
    @CsvSource({
            "LS1,    LS1,  LS, false",
            "' m1 ', M1,   M,  false",
            "ec1a,   EC1A, EC, true",
            "SW19,   SW19, SW, true",
            "E1,     E1,   E,  true",
            "EH1,    EH1,  EH, false",
    })
    void parsesOutwardCodes(String raw, String code, String area, boolean london) {
        assertEquals(new PostcodeInfo(code, area, london), PostcodeInfo.from(raw));
    }

    @ParameterizedTest
    @ValueSource(strings = {"123", "LSX", "L", "LS1 4AP", "ABC1", ""})
    void rejectsInvalidCodes(String raw) {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> PostcodeInfo.from(raw));
        assertEquals("not an outward code: " + raw, e.getMessage());
    }
}

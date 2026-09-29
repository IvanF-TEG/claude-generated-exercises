package ex14;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

// GIVEN: no need to edit. Predict each result in PredictionsTest BEFORE running it.
public class Predictions {

    static String putReturnsThePreviousValue() {
        Map<String, Integer> counts = new HashMap<>();
        Integer first = counts.put("LS", 3);
        Integer second = counts.put("LS", 5);
        return first + " " + second + " " + counts.get("LS");
    }

    static String missingKeys() {
        Map<String, Integer> counts = new HashMap<>();
        return counts.get("XX") + " " + counts.getOrDefault("XX", 0);
    }

    static String unboxingAMissingValue() {
        Map<String, Integer> counts = new HashMap<>();
        try {
            int n = counts.get("XX");
            return "got " + n;
        } catch (NullPointerException e) {
            return "NullPointerException";
        }
    }

    static String mergeReturningNullRemovesTheKey() {
        Map<String, Integer> counts = new HashMap<>();
        counts.put("LS", 3);
        counts.merge("LS", 1, (oldValue, newValue) -> null);
        return counts.containsKey("LS") + " " + counts.size();
    }

    /** A MUTABLE key class, with equals and hashCode based on a field that can change. Don't do this! */
    static final class Tag {
        String code;

        Tag(String code) {
            this.code = code;
        }

        @Override
        public boolean equals(Object other) {
            return other instanceof Tag that && code.equals(that.code);
        }

        @Override
        public int hashCode() {
            return code.hashCode();
        }
    }

    static String mutatedKeyGetsLost() {
        Map<Tag, String> owners = new HashMap<>();
        Tag tag = new Tag("A");
        owners.put(tag, "Priya");
        tag.code = "B";   // changing the key AFTER it's in the map
        return owners.containsKey(tag) + " " + owners.containsKey(new Tag("A")) + " " + owners.size();
    }

    static String linkedHashMapRePutKeepsItsPlace() {
        Map<String, Integer> m = new LinkedHashMap<>();
        m.put("a", 1);
        m.put("b", 2);
        m.put("c", 3);
        m.put("a", 99);
        return m.keySet() + " " + m.values();
    }
}

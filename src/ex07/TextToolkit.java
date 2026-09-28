package ex07;

import java.util.Arrays;

// Strings in Java are IMMUTABLE objects: every "modifying" method RETURNS A NEW String.
//
//   s.length()                  // a method, so it needs brackets (unlike array.length)
//   s.charAt(i)                 // a char, e.g. 'a'. There's no s[i]!
//   s.substring(start, end)     // end is EXCLUSIVE, like Python's s[start:end]
//   s.indexOf("x")              // -1 if not found
//   s.toLowerCase(), s.trim(), s.replace("a", "b"), s.contains("x"), s.isEmpty()
//   s.split("\\s+")             // split on a REGEX; "\\s+" = one or more whitespace characters
//   s.toCharArray()             // char[]
//   s.equals(t)                 // compare CONTENTS. Never use == on Strings.
//
//   Character.isLetter(c), Character.isDigit(c), Character.toUpperCase(c)
//
//   StringBuilder sb = new StringBuilder();  // a MUTABLE string, for building text in loops
//   sb.append("x").append('y');  sb.reverse();  sb.length();  sb.toString();
public class TextToolkit {

    /** True if s reads the same backwards, ignoring case and anything that isn't a letter. "" counts as a palindrome. */
    static boolean isPalindrome(String s) {
        // TODO 1: build a cleaned-up lowercase version with a StringBuilder, then compare it with its reverse
        return false;
    }

    /** Number of vowels (a, e, i, o, u), in either case. */
    static int countVowels(String s) {
        // TODO 2: loop with charAt. A neat trick: "aeiou".indexOf(c) >= 0
        return -1;
    }

    /** Reverses the ORDER of the words, collapsing extra whitespace: "  the quick  brown fox " -> "fox brown quick the". */
    static String reverseWords(String sentence) {
        // TODO 3: trim, split on "\\s+", then rebuild backwards with a StringBuilder
        return null;
    }

    /**
     * Shifts each letter by 'shift' places in the alphabet, wrapping round and keeping its case.
     * Non-letters are unchanged. Shift may be negative or bigger than 26.
     * caesarShift("Hello, World!", 3) -> "Khoor, Zruog!"
     */
    static String caesarShift(String text, int shift) {
        // TODO 4: chars ARE numbers: (char) ('a' + (c - 'a' + s) % 26)
        //         Watch out: in Java, -1 % 26 == -1 (not 25 as in Python). Normalise shift first!
        return null;
    }

    /**
     * Run-length encoding: "aaabccdddd" -> "a3b1c2d4".
     * If the encoded version ISN'T shorter than the original, return the original unchanged ("abc" -> "abc").
     */
    static String compress(String s) {
        // TODO 5
        return null;
    }

    /**
     * Converts words separated by spaces, underscores or hyphens into Java camelCase.
     * "hello world from java" -> "helloWorldFromJava"; "  Parcel_delivery-STATUS " -> "parcelDeliveryStatus"
     */
    static String toCamelCase(String s) {
        // TODO 6: split on the regex "[\\s_-]+" (any run of whitespace, _ or -)
        return null;
    }

    /** True if a and b use exactly the same letters, ignoring spaces and case. "Dormitory" / "Dirty room" -> true. */
    static boolean isAnagram(String a, String b) {
        // TODO 7: remove spaces, lowercase, toCharArray(), Arrays.sort(...), then Arrays.equals(...)
        return false;
    }

    // ---------------------------------------------------------------
    // Tests: no need to edit, apart from the predictions.
    // ---------------------------------------------------------------
    public static void main(String[] args) {
        check("isPalindrome(\"A man, a plan, a canal: Panama\")", isPalindrome("A man, a plan, a canal: Panama"), true);
        check("isPalindrome(\"Hello\")", isPalindrome("Hello"), false);
        check("isPalindrome(\"\")", isPalindrome(""), true);
        check("countVowels(\"Programming in Java\")", countVowels("Programming in Java"), 6);
        check("countVowels(\"RHYTHM\")", countVowels("RHYTHM"), 0);
        check("reverseWords(\"  the quick  brown fox \")", reverseWords("  the quick  brown fox "), "fox brown quick the");
        check("reverseWords(\"solo\")", reverseWords("solo"), "solo");
        check("caesarShift(\"Hello, World!\", 3)", caesarShift("Hello, World!", 3), "Khoor, Zruog!");
        check("caesarShift(\"Khoor, Zruog!\", -3)", caesarShift("Khoor, Zruog!", -3), "Hello, World!");
        check("caesarShift(\"Zebra\", 27)", caesarShift("Zebra", 27), "Afcsb");
        check("compress(\"aaabccdddd\")", compress("aaabccdddd"), "a3b1c2d4");
        check("compress(\"abc\")", compress("abc"), "abc");
        check("compress(\"\")", compress(""), "");
        check("toCamelCase(\"hello world from java\")", toCamelCase("hello world from java"), "helloWorldFromJava");
        check("toCamelCase(\"  Parcel_delivery-STATUS \")", toCamelCase("  Parcel_delivery-STATUS "), "parcelDeliveryStatus");
        check("isAnagram(\"Dormitory\", \"Dirty room\")", isAnagram("Dormitory", "Dirty room"), true);
        check("isAnagram(\"hello\", \"world\")", isAnagram("hello", "world"), false);

        // The #1 Java gotcha for Python programmers. Predict each line first!
        System.out.println("--- == vs equals ---");
        String literal = "java";
        String sameLiteral = "java";
        String built = new String("java");
        String lowered = "JAVA".toLowerCase();
        System.out.println("literal == sameLiteral: " + (literal == sameLiteral));     // prediction:
        System.out.println("literal == built: " + (literal == built));                 // prediction:
        System.out.println("literal == lowered: " + (literal == lowered));             // prediction:
        System.out.println("literal.equals(built): " + literal.equals(built));         // prediction:
        System.out.println("literal.equals(lowered): " + literal.equals(lowered));     // prediction:
        String name = "ada";
        name.toUpperCase();
        System.out.println("after name.toUpperCase(): " + name);                       // prediction:
        name = name.toUpperCase();
        System.out.println("after name = name.toUpperCase(): " + name);                // prediction:
    }

    static void check(String label, int actual, int expected) {
        System.out.println((actual == expected ? "PASS " : "FAIL ") + label + " -> got " + actual + ", expected " + expected);
    }

    static void check(String label, boolean actual, boolean expected) {
        System.out.println((actual == expected ? "PASS " : "FAIL ") + label + " -> got " + actual + ", expected " + expected);
    }

    static void check(String label, String actual, String expected) {
        System.out.println((expected.equals(actual) ? "PASS " : "FAIL ") + label + " -> got \"" + actual + "\", expected \"" + expected + "\"");
    }
}

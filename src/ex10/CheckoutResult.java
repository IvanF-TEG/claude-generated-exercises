package ex10;

// GIVEN. An enum is a type with a fixed set of named values: a type-safe replacement for
// "magic" ints or Strings. Compare values with == (each value is a single shared object).
//   CheckoutResult r = CheckoutResult.SUCCESS;
//   if (r == CheckoutResult.LIMIT_REACHED) { ... }
// Enums also work in switch statements.
public enum CheckoutResult {
    SUCCESS,
    NO_SUCH_MEMBER,
    NO_SUCH_BOOK,
    ALREADY_BORROWED,
    LIMIT_REACHED
}

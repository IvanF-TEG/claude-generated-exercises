package ex19;

// A record is a class whose whole point is to carry data. From this ONE line, Java generates:
//   a private final field per component, a constructor, accessors (code(), town(), dropMinutes()
//   with no "get"), and equals / hashCode / toString based on ALL the components.
// Compare it with the 50 lines of Item in Exercise 11.
//
// A COMPACT constructor has no parameter list. It runs BEFORE the fields are assigned, so you can
// validate the parameters, or reassign them to normalise them:
//
//   public Stop {
//       if (...) throw new IllegalArgumentException("...");
//       code = code.trim();          // assigns the PARAMETER. The field is set from it afterwards.
//   }
public record Stop(String code, String town, int dropMinutes) {

    // TODO 1: write a compact constructor that:
    //   - throws IllegalArgumentException("stop code is required") if code is null or blank
    //   - throws IllegalArgumentException("drop time cannot be negative: -5") if dropMinutes < 0
    //   - normalises code to trimmed UPPER case, so new Stop(" lds ", ...) has code() "LDS"
    //   - normalises town by trimming it (a null town is allowed to fail with a NullPointerException)
}

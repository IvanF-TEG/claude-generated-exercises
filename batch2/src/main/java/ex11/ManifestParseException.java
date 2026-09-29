package ex11;

// GIVEN: a worked example of a custom CHECKED exception. Study it before writing OverweightException.
//
// 'extends Exception'        -> checked: callers MUST catch it or declare 'throws ManifestParseException'
// 'extends RuntimeException' -> unchecked: the compiler doesn't force anyone to handle it
public class ManifestParseException extends Exception {
    private final int lineNumber;

    public ManifestParseException(int lineNumber, String problem) {
        super("line " + lineNumber + ": " + problem);   // super(message) sets what getMessage() returns
        this.lineNumber = lineNumber;
    }

    /** Use this one when you're translating a lower-level exception, so the original isn't lost. */
    public ManifestParseException(int lineNumber, String problem, Throwable cause) {
        super("line " + lineNumber + ": " + problem, cause);   // getCause() will return 'cause'
        this.lineNumber = lineNumber;
    }

    public int getLineNumber() {
        return lineNumber;
    }
}

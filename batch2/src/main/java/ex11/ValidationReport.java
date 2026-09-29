package ex11;

import java.util.List;

// GIVEN: no need to edit. The result of a lenient validation run: what was fine, and what went wrong.
public class ValidationReport {
    private final List<Item> validItems;
    private final List<String> errors;

    public ValidationReport(List<Item> validItems, List<String> errors) {
        this.validItems = validItems;
        this.errors = errors;
    }

    public List<Item> getValidItems() {
        return validItems;
    }

    public List<String> getErrors() {
        return errors;
    }

    public boolean isClean() {
        return errors.isEmpty();
    }
}

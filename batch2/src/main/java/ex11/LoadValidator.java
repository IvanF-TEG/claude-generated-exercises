package ex11;

import java.util.ArrayList;
import java.util.List;

// A manifest is a list of text lines, one item per line:   id,description,weightKg
//
//   # Morning run to Leeds          <- comment line: ignore
//   PLT-001, Pallet of tiles, 450   <- spaces around fields are allowed
//                                   <- blank line: ignore
//   PLT-002,Boiler,85
//
// Line numbers are 1-based and count EVERY line, including comments and blanks.
//
// Exception toolbox for this exercise:
//   throw new ManifestParseException(lineNumber, "problem");            // throw your own
//   try { ... } catch (NumberFormatException e) { ... }                 // catch someone else's
//   throw new ManifestParseException(lineNumber, "problem", e);         // translate, keeping the cause
//   try (LoadingBay bay = new LoadingBay(...)) { ... } finally { ... }  // try-with-resources + finally
public class LoadValidator {

    /**
     * Parses one manifest line into an Item. Fields are trimmed.
     * Throws ManifestParseException (with this line number) when:
     *   - there aren't exactly 3 fields              -> problem text "expected 3 fields but found N"
     *   - the id is blank                            -> "missing id"
     *   - the weight isn't a whole number            -> "weight is not a number: 'abc'"   (the cause must be the NumberFormatException)
     *   - the weight is zero or negative             -> "weight must be positive: -5"
     * Example: parseLine("  PLT-001 , Pallet of tiles , 450 ", 1) -> Item("PLT-001", "Pallet of tiles", 450)
     */
    public static Item parseLine(String line, int lineNumber) throws ManifestParseException {
        // 2
        String[] splitLine = line.split(",");
        ArrayList<String> fieldsArray = new ArrayList<>();
        for (String field : splitLine){
            fieldsArray.add(field.trim());
        }
        String[] fields = fieldsArray.toArray(new String[0]);
        if (fields.length != 3){
            throw new ManifestParseException(lineNumber, "expected 3 fields but found " + fields.length);
        }
        if (fields[0].isEmpty()){
            throw new ManifestParseException(lineNumber, "missing id");
        }
        try {
            int weight = Integer.parseInt(fields[2]);
            if (weight <= 0){
                throw new ManifestParseException(lineNumber, "weight must be positive: " + weight);
            }
            return new Item(fields[0], fields[1], weight);
        } catch (NumberFormatException e) {
            throw new ManifestParseException(lineNumber, "weight is not a number: '" + fields[2] + "'", e);
        }
    }

    /**
     * STRICT mode: parses every item line, skipping blank lines and lines starting with '#'
     * (after trimming). The first bad line stops everything: its exception propagates to the caller.
     */
    public static List<Item> parseManifest(List<String> lines) throws ManifestParseException {
        // 3
        List<Item> parseManifest = new ArrayList<>();
        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i).trim();
            if (line.isBlank() || line.charAt(0) == '#') {
                continue;
            }
            parseManifest.add(parseLine(line, i + 1));
        }
        return parseManifest;
    }

    /**
     * Throws OverweightException if the total weight of the items is MORE than maxKg (exactly maxKg is fine).
     */
    public static void checkWeight(List<Item> items, int maxKg) throws OverweightException {
        // 4
        int totalKg = 0;
        for (Item item : items){
            totalKg += item.getWeightKg();
        }
        if (totalKg > maxKg){
            throw new OverweightException(totalKg, maxKg);
        }
    }

    /**
     * LENIENT mode: never throws a checked exception. Bad lines are recorded and skipped, so ONE run
     * reports EVERY problem. Blank and comment lines are skipped as in parseManifest.
     *   - Each bad line adds its exception's getMessage() to the errors, e.g. "line 3: missing id"
     *   - Once all lines are done, the valid items are weight-checked. If they're overweight, add
     *     that exception's message to the errors as well (the items stay in validItems).
     * Reuse parseLine and checkWeight. Don't copy their logic.
     */
    public static ValidationReport validateAll(List<String> lines, int maxKg) {
        //  5
        ArrayList<Item> validItems = new ArrayList<>();
        ArrayList<String> errors = new ArrayList<>();
        for (int i = 0; i < lines.size(); i++){
            String line = lines.get(i);
            if (line.isBlank() || line.charAt(0) == '#'){
                continue;
            }
            try {
                validItems.add(parseLine(line, i + 1));
            } catch (ManifestParseException e) {
                errors.add(e.getMessage());
            }
        }
        try {
            checkWeight(validItems, maxKg);
        } catch (OverweightException e) {
            errors.add(e.getMessage());
        }
        return new ValidationReport(validItems, errors);
    }

    /**
     * Loads the items, in order, through a LoadingBay named bayName, and returns how many were loaded.
     *   - Open the bay with try-WITH-RESOURCES, so it's closed whether or not loading succeeds.
     *   - If the bay fills up, the IllegalStateException must still reach the caller. Do NOT catch it.
     *   - In a 'finally' block, add "summary: loaded N item(s)" to the log. It must be added
     *     whether loading succeeds or fails.
     * The tests check the exact ORDER of the log. Before running them, predict whether "close ..."
     * comes before or after "summary: ...".
     */
    public static int loadAll(String bayName, int capacityKg, List<Item> items, List<String> log) {
        //  6
        int loadCount = 0;
        try (LoadingBay bay = new LoadingBay(bayName, capacityKg, log)){
            for (Item item : items){
                bay.load(item);
                loadCount++;
            }
        } finally {
            log.add("summary: loaded " + loadCount + " item(s)");
        }
        return loadCount;
    }
}

package ex11;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

// Each @Test method is one independent test. JUnit creates a NEW instance of this class for every test,
// so tests can't leak state into each other. @Nested groups related tests in the IntelliJ test tree.
class LoadValidatorTest {

    @Nested
    @DisplayName("TODO 1: OverweightException")
    class OverweightExceptionTests {

        @Test
        void isACheckedException() {
            // Checked = a subclass of Exception but NOT of RuntimeException
            assertTrue(Exception.class.isAssignableFrom(OverweightException.class));
            assertFalse(RuntimeException.class.isAssignableFrom(OverweightException.class),
                    "OverweightException should be checked, so it must not extend RuntimeException");
        }

        @Test
        void messageAndExcess() {
            OverweightException e = new OverweightException(1250, 1000);
            assertEquals("Load of 1250 kg exceeds limit of 1000 kg by 250 kg", e.getMessage());
            assertEquals(250, e.getExcessKg());
        }
    }

    @Nested
    @DisplayName("TODO 2: parseLine")
    class ParseLineTests {

        @Test
        void parsesAndTrimsFields() throws Exception {
            Item item = LoadValidator.parseLine("  PLT-001 , Pallet of tiles , 450 ", 1);
            assertEquals(new Item("PLT-001", "Pallet of tiles", 450), item);
        }

        @Test
        void wrongFieldCount() {
            // assertThrows runs the lambda, checks it threw this type, and RETURNS the exception for inspection
            ManifestParseException e = assertThrows(ManifestParseException.class,
                    () -> LoadValidator.parseLine("PLT-001,Tiles", 3));
            assertEquals(3, e.getLineNumber());
            assertEquals("line 3: expected 3 fields but found 2", e.getMessage());
        }

        @Test
        void missingId() {
            ManifestParseException e = assertThrows(ManifestParseException.class,
                    () -> LoadValidator.parseLine("   ,Tiles,450", 7));
            assertEquals("line 7: missing id", e.getMessage());
        }

        @Test
        void weightNotANumberKeepsTheCause() {
            ManifestParseException e = assertThrows(ManifestParseException.class,
                    () -> LoadValidator.parseLine("PLT-001,Tiles, 4five0 ", 2));
            assertEquals("line 2: weight is not a number: '4five0'", e.getMessage());
            assertInstanceOf(NumberFormatException.class, e.getCause(),
                    "wrap the NumberFormatException as the cause, don't throw it away");
        }

        @Test
        void weightMustBePositive() {
            ManifestParseException e = assertThrows(ManifestParseException.class,
                    () -> LoadValidator.parseLine("PLT-001,Tiles,-5", 4));
            assertEquals("line 4: weight must be positive: -5", e.getMessage());
            assertNull(e.getCause());
            assertThrows(ManifestParseException.class, () -> LoadValidator.parseLine("PLT-001,Tiles,0", 4));
        }
    }

    @Nested
    @DisplayName("TODO 3: parseManifest")
    class ParseManifestTests {

        @Test
        void skipsCommentsAndBlankLines() throws Exception {
            List<String> lines = List.of(
                    "# Morning run to Leeds",
                    "PLT-001, Pallet of tiles, 450",
                    "   ",
                    "PLT-002,Boiler,85");
            assertEquals(List.of(new Item("PLT-001", "Pallet of tiles", 450), new Item("PLT-002", "Boiler", 85)),
                    LoadValidator.parseManifest(lines));
        }

        @Test
        void lineNumbersCountEveryLine() {
            List<String> lines = List.of("# header", "", "PLT-001,Tiles,450", "PLT-002,Boiler");
            ManifestParseException e = assertThrows(ManifestParseException.class,
                    () -> LoadValidator.parseManifest(lines));
            assertEquals(4, e.getLineNumber());
        }

        @Test
        void emptyManifestIsFine() throws Exception {
            assertEquals(List.of(), LoadValidator.parseManifest(List.of()));
        }
    }

    @Nested
    @DisplayName("TODO 4: checkWeight")
    class CheckWeightTests {
        final List<Item> items = List.of(new Item("A", "Crate", 600), new Item("B", "Crate", 400));

        @Test
        void exactlyAtTheLimitIsFine() {
            assertDoesNotThrow(() -> LoadValidator.checkWeight(items, 1000));
        }

        @Test
        void overTheLimitThrows() {
            OverweightException e = assertThrows(OverweightException.class,
                    () -> LoadValidator.checkWeight(items, 900));
            assertEquals(100, e.getExcessKg());
        }
    }

    @Nested
    @DisplayName("TODO 5: validateAll")
    class ValidateAllTests {

        @Test
        void cleanManifest() {
            ValidationReport report = LoadValidator.validateAll(List.of("A,Crate,10", "B,Crate,20"), 100);
            assertTrue(report.isClean());
            assertEquals(2, report.getValidItems().size());
        }

        @Test
        void collectsEveryProblemInOneRun() {
            List<String> lines = List.of(
                    "# Friday",
                    "A,Crate,600",
                    ",Crate,10",
                    "B,Crate,heavy",
                    "C,Crate,500",
                    "D,Crate");
            ValidationReport report = LoadValidator.validateAll(lines, 1000);
            assertEquals(List.of(new Item("A", "Crate", 600), new Item("C", "Crate", 500)), report.getValidItems());
            assertEquals(List.of(
                    "line 3: missing id",
                    "line 4: weight is not a number: 'heavy'",
                    "line 6: expected 3 fields but found 2",
                    "Load of 1100 kg exceeds limit of 1000 kg by 100 kg"), report.getErrors());
        }
    }

    @Nested
    @DisplayName("TODO 6: loadAll")
    class LoadAllTests {
        final List<Item> items = List.of(new Item("A", "Crate", 300), new Item("B", "Crate", 300), new Item("C", "Crate", 300));

        @Test
        void happyPath() {
            List<String> log = new ArrayList<>();
            assertEquals(3, LoadValidator.loadAll("Bay 3", 1000, items, log));
            assertEquals(List.of("open Bay 3", "load A", "load B", "load C", "close Bay 3", "summary: loaded 3 item(s)"), log);
        }

        @Test
        void bayIsClosedAndSummaryWrittenEvenWhenLoadingFails() {
            List<String> log = new ArrayList<>();
            assertThrows(IllegalStateException.class, () -> LoadValidator.loadAll("Bay 3", 700, items, log));
            assertEquals(List.of("open Bay 3", "load A", "load B", "reject C", "close Bay 3", "summary: loaded 2 item(s)"), log);
        }
    }

    @Nested
    @DisplayName("TODO 7: your own tests")
    class YourTests {

        @Test
        void commentLineWithLeadingSpacesIsSkipped() throws Exception {
            // 7a: prove that "   # indented comment" is skipped by parseManifest. Then delete the fail(...) line.
            List<String> lines = List.of("   # indented comment");
            assertEquals(List.of(), LoadValidator.parseManifest(lines));
        }

        @Test
        void validateAllNeverAddsAWeightErrorWhenUnderTheLimit() {
            //  7b: choose your own input. Assert on BOTH getValidItems() and getErrors().
            List<String> lines = List.of(
                    "# Friday",
                    "A,Crate,600",
                    ",Crate,10",
                    "B,Crate,heavy",
                    "C,Crate,500",
                    "D,Crate");
            ValidationReport report = LoadValidator.validateAll(lines, 1100);
            assertEquals(List.of(new Item("A", "Crate", 600), new Item("C", "Crate", 500)), report.getValidItems());
            assertEquals(List.of(
                    "line 3: missing id",
                    "line 4: weight is not a number: 'heavy'",
                    "line 6: expected 3 fields but found 2"
                    ), report.getErrors());
        }
    }
}

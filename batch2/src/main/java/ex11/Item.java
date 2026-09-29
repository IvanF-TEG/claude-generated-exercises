package ex11;

import java.util.Objects;

// GIVEN: no need to edit. One line of a loading manifest.
// (In Exercise 19 you'll see how a 'record' replaces almost all of this boilerplate.)
public final class Item {
    private final String id;
    private final String description;
    private final int weightKg;

    public Item(String id, String description, int weightKg) {
        this.id = id;
        this.description = description;
        this.weightKg = weightKg;
    }

    public String getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public int getWeightKg() {
        return weightKg;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Item that)) {
            return false;
        }
        return weightKg == that.weightKg && id.equals(that.id) && description.equals(that.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, description, weightKg);
    }

    @Override
    public String toString() {
        return id + " (" + description + ", " + weightKg + " kg)";
    }
}

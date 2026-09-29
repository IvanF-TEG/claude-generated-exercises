package ex12;

// GIVEN: no need to edit. Predict each result in PredictionsTest BEFORE running it.
// The big idea: Java decides SOME things at compile time (using the variable's declared type)
// and OTHER things at run time (using the real object). Which is which?
public class Predictions {

    static class Parcel {
        String name = "parcel";

        String label() {
            return "parcel";
        }

        static String category() {
            return "general";
        }
    }

    static class FragileParcel extends Parcel {
        String name = "fragile";          // a second, separate field that HIDES Parcel.name

        @Override
        String label() {
            return "fragile";
        }

        static String category() {        // static methods can't be overridden, only hidden
            return "fragile goods";
        }
    }

    static String handle(Parcel p) {
        return "handle parcel";
    }

    static String handle(FragileParcel p) {
        return "handle fragile";
    }

    static String overloadingUsesTheDeclaredType() {
        Parcel p = new FragileParcel();
        return handle(p);
    }

    static String overridingUsesTheRealObject() {
        Parcel p = new FragileParcel();
        return p.label();
    }

    static String fieldsAreNotPolymorphic() {
        Parcel p = new FragileParcel();
        return p.name;
    }

    @SuppressWarnings("static-access")
    static String staticMethodsAreNotPolymorphic() {
        Parcel p = new FragileParcel();
        return p.category();
    }

    static class Base {
        final String log;

        Base() {
            log = describe();              // calling an overridable method from a constructor...
        }

        String describe() {
            return "base";
        }
    }

    static class Child extends Base {
        private String colour = "red";

        @Override
        String describe() {
            return "child " + colour;
        }
    }

    static String constructorCallsOverriddenMethod() {
        return new Child().log;
    }
}

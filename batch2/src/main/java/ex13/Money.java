package ex13;

// GIVEN: no need to edit.
public final class Money {
    private Money() {
        // a 'utility class': only static methods, so nobody should ever create one
    }

    /** 1234 -> "£12.34", 5 -> "£0.05", -300 -> "-£3.00" */
    public static String format(long pence) {
        String sign = pence < 0 ? "-" : "";
        long abs = Math.abs(pence);
        return String.format("%s£%d.%02d", sign, abs / 100, abs % 100);
    }
}

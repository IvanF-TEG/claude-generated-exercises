package ex13;

import java.time.DayOfWeek;

/** TODO 3a: "Weekend surcharge", +15%, applies on SATURDAY and SUNDAY. */
public class WeekendSurcharge extends PercentageRule {

    public WeekendSurcharge() {
        super("TODO", 0);
    }

    @Override
    protected boolean appliesTo(Quote quote) {
        return false;
    }
}

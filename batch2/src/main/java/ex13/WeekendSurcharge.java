package ex13;

import java.time.DayOfWeek;

/** 3a: "Weekend surcharge", +15%, applies on SATURDAY and SUNDAY. */
public class WeekendSurcharge extends PercentageRule {

    public WeekendSurcharge() {
        super("Weekend surcharge",  15);
    }

    @Override
    protected boolean appliesTo(Quote quote) {
        return (quote.getDay().equals(DayOfWeek.SATURDAY) || quote.getDay().equals(DayOfWeek.SUNDAY));
    }
}

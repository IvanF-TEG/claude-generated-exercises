package ex13;

/** TODO 3b: "Loyalty discount", -10%, applies to loyal customers. */
public class LoyaltyDiscount extends PercentageRule {

    public LoyaltyDiscount() {
        super("TODO", 0);
    }

    @Override
    protected boolean appliesTo(Quote quote) {
        return false;
    }
}

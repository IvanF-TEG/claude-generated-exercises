package ex13;

/** 3b: "Loyalty discount", -10%, applies to loyal customers. */
public class LoyaltyDiscount extends PercentageRule {

    public LoyaltyDiscount() {
        super("Loyalty discount", -10);
    }

    @Override
    protected boolean appliesTo(Quote quote) {
        return quote.isLoyalCustomer();
    }
}

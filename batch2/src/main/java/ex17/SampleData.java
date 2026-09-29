package ex17;

import java.util.List;

// GIVEN: no need to edit. Last Tuesday's deliveries. The tests use exactly this data.
public class SampleData {

    public static List<Delivery> tuesday() {
        return List.of(
                //           id     carrier     town       kg   km   on time?
                new Delivery("D01", "Swift", "Leeds", 120, 45, true),
                new Delivery("D02", "Northern", "York", 80, 60, false),
                new Delivery("D03", "Swift", "Hull", 300, 95, false),
                new Delivery("D04", "Apex", "Leeds", 45, 45, true),
                new Delivery("D05", "Swift", "York", 210, 60, false),
                new Delivery("D06", "Northern", "Leeds", 95, 40, true),
                new Delivery("D07", "Swift", "Bristol", 150, 310, false),
                new Delivery("D08", "Apex", "Hull", 300, 90, true),
                new Delivery("D09", "Northern", "Bristol", 60, 320, true),
                new Delivery("D10", "Swift", "Leeds", 75, 50, false));
    }
}

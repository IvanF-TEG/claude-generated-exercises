package ex12;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// The Fleet holds a List<Vehicle>, yet it can contain Vans, Trucks and Hgvs.
// This is POLYMORPHISM: when you call v.costFor(km) on each element, Java runs the version
// belonging to the object's REAL class, whatever the variable's declared type.
//
// Rule of thumb for this class: don't use instanceof to decide how much something costs or what it
// can carry. Let the objects answer for themselves. Use instanceof only for questions that
// genuinely belong to one subtype (6).
public class Fleet {

    // 5: a private final list of vehicles, plus the four methods below.

    private final List<Vehicle> fleetList = new ArrayList<>();

    /** Adds the vehicle unless one with the same registration is already in the fleet. */
    public boolean add(Vehicle vehicle) {
        for (Vehicle v : fleetList){
            if (v.getRegistration().equals(vehicle.getRegistration())){
                return false;
            }
        }
        return fleetList.add(vehicle);
    }

    public int totalPayloadKg() {
        int totalPayloadKg = 0;
        for (Vehicle vehicle : fleetList){
            totalPayloadKg += vehicle.getMaxPayloadKg();
        }
        return totalPayloadKg;
    }

    /** Every vehicle whose canCarry(kg) is true, in the order they were added. */
    public List<Vehicle> ableToCarry(int kg) {
        List<Vehicle> ableToCarry = new ArrayList<>();
        for (Vehicle vehicle : fleetList){
            if (vehicle.canCarry(kg)) {
                ableToCarry.add(vehicle);
            }
        }
        return ableToCarry;
    }

    /** describe() of every vehicle, in the order they were added. */
    public List<String> describeAll() {
        List<String> describeAll = new ArrayList<>();
        for (Vehicle vehicle : fleetList){
            describeAll.add(vehicle.describe());
        }
        return describeAll;
    }

    // 6: the three methods below.

    /**
     * The vehicle that can carry kg and has the lowest costFor(km). If two cost the same, the one
     * added first wins. Returns null if no vehicle can carry the load.
     */
    public Vehicle cheapestFor(int kg, int km) {
        List<Vehicle> ableToCarry = ableToCarry(kg);
        if (ableToCarry.isEmpty()){
            return null;
        }
        Vehicle cheapest = ableToCarry.getFirst();
        for (Vehicle vehicle : ableToCarry){
            if (vehicle.costFor(km) < cheapest.costFor(km)){
                cheapest = vehicle;
            }
        }
        return cheapest;
    }

    /** How many vehicles are Vans that are refrigerated. Use a pattern-matching instanceof. */
    public int refrigeratedVanCount() {
        int refrigeratedVanCount = 0;
        for (Vehicle vehicle : fleetList){
            if (vehicle instanceof Van van){
                refrigeratedVanCount += (van.isRefrigerated()) ? 1 : 0;
            }
        }
        return refrigeratedVanCount;
    }

    /** Total axles across every Truck in the fleet. Think about whether an Hgv counts! */
    public int totalAxles() {
        int totalAxles = 0;
        for (Vehicle vehicle : fleetList){
            if (vehicle instanceof Truck truck){
                totalAxles += truck.getAxles();
            }
        }
        return totalAxles;
    }
}

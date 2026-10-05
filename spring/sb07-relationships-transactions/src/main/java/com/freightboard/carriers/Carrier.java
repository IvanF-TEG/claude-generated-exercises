package com.freightboard.carriers;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

// GIVEN. A haulage company that bids on loads. maxWeightKg is the most its vehicles can carry.
@Entity
@Table(name = "carriers")
public class Carrier {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private int maxWeightKg;

    protected Carrier() {
    }

    public Carrier(String name, int maxWeightKg) {
        this.name = name;
        this.maxWeightKg = maxWeightKg;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getMaxWeightKg() {
        return maxWeightKg;
    }
}

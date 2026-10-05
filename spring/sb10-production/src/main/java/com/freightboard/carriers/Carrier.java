package com.freightboard.carriers;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

// GIVEN. A haulage company that bids on loads. maxWeightKg is the most its vehicles can carry.
// SB09: username is the login that bids for this carrier (null = nobody can).
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

    @Column(unique = true, length = 50)
    private String username;

    protected Carrier() {
    }

    public Carrier(String name, int maxWeightKg, String username) {
        this.name = name;
        this.maxWeightKg = maxWeightKg;
        this.username = username;
    }

    public Carrier(String name, int maxWeightKg) {
        this(name, maxWeightKg, null);
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

    public String getUsername() {
        return username;
    }
}

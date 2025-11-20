package com.recipeplanner.model;

import java.util.Objects;

/**
 * Represents a measurement quantity with amount and unit.
 * Used for ingredient quantities in recipes and grocery lists.
 * 
 * Demonstrates value object pattern and immutability concepts.
 * 
 * @author Recipe Planner Team
 * @version 1.0
 */
public class Measurement {
    private double amount;
    private String unit;

    /**
     * Default constructor for Measurement.
     */
    public Measurement() {
        this.amount = 0.0;
        this.unit = "unit";
    }

    /**
     * Constructor with amount and unit.
     * 
     * @param amount The numeric quantity
     * @param unit The unit of measurement (e.g., "g", "kg", "ml", "l", "pcs")
     */
    public Measurement(double amount, String unit) {
        this.amount = amount;
        this.unit = unit != null ? unit : "unit";
    }

    // Getters and Setters

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    /**
     * Adds the specified amount to this measurement.
     * Assumes the units are compatible (conversion should be done before calling).
     * 
     * @param additionalAmount The amount to add
     */
    public void add(double additionalAmount) {
        this.amount += additionalAmount;
    }

    /**
     * Creates a deep copy of this measurement.
     * 
     * @return A new Measurement object with the same values
     */
    public Measurement copy() {
        return new Measurement(this.amount, this.unit);
    }

    /**
     * Checks if this measurement has a valid amount.
     * 
     * @return true if amount is greater than 0
     */
    public boolean hasAmount() {
        return amount > 0;
    }

    /**
     * Normalizes the unit to lowercase for consistent comparisons.
     * 
     * @return Normalized unit string
     */
    public String getNormalizedUnit() {
        return unit != null ? unit.toLowerCase() : "unit";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Measurement that = (Measurement) o;
        return Double.compare(that.amount, amount) == 0 && 
               Objects.equals(unit, that.unit);
    }

    @Override
    public int hashCode() {
        return Objects.hash(amount, unit);
    }

    @Override
    public String toString() {
        return String.format("%.2f %s", amount, unit);
    }

    /**
     * Returns a formatted string with better precision control.
     * 
     * @return Formatted measurement string
     */
    public String toFormattedString() {
        if (amount == (long) amount) {
            return String.format("%d %s", (long) amount, unit);
        } else {
            return String.format("%.2f %s", amount, unit);
        }
    }
}

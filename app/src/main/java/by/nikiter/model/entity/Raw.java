package by.nikiter.model.entity;

import by.nikiter.model.Unit;

import java.util.Objects;

public class Raw {

    private String name;
    private double cost;
    private int quantity;
    private Unit unit;

    public Raw(String name, double cost, int quantity, Unit unit) {
        this.name = name;
        this.cost = cost;
        this.quantity = quantity;
        this.unit = unit;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Unit getUnit() {
        return unit;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Raw raw = (Raw) o;
        return Double.compare(raw.cost, cost) == 0 &&
                quantity == raw.quantity &&
                Objects.equals(name, raw.name) &&
                unit == raw.unit;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, cost, quantity, unit);
    }

    @Override
    public String toString() {
        return name;
    }
}

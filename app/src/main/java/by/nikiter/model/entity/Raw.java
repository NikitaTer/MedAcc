package by.nikiter.model.entity;

import by.nikiter.model.Unit;

import java.util.Objects;

public class Raw {

    private String name;
    private double cost;
    private Unit unit;

    public Raw(String name, double cost, Unit unit) {
        this.name = name;
        this.cost = cost;
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

        if (Double.compare(raw.cost, cost) != 0) return false;
        if (name != null ? !name.equals(raw.name) : raw.name != null) return false;
        return unit == raw.unit;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = name != null ? name.hashCode() : 0;
        temp = Double.doubleToLongBits(cost);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (unit != null ? unit.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return name;
    }
}

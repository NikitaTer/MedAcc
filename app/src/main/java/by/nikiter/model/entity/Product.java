package by.nikiter.model.entity;

import by.nikiter.model.Unit;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Product {
    private String name;
    private Unit unit;
    private int quantity;
    private List<Raw> raws = new ArrayList<>();
    private double salary = 0.0;
    private double cost = 0.0;

    public Product(String name, Unit unit, int quantity) {
        this.name = name;
        this.unit = unit;
        this.quantity = quantity;
    }

    public void addRaw(Raw raw) {
        raws.add(raw);
    }

    public void deleteRaw(Raw raw) {
        raws.remove(raw);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Unit getUnit() {
        return unit;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public List<Raw> getRaws() {
        return raws;
    }

    public void setRaws(List<Raw> raws) {
        this.raws = raws;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return quantity == product.quantity &&
                Double.compare(product.salary, salary) == 0 &&
                Double.compare(product.cost, cost) == 0 &&
                Objects.equals(name, product.name) &&
                unit == product.unit &&
                Objects.equals(raws, product.raws);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, unit, quantity, raws, salary, cost);
    }

    @Override
    public String toString() {
        return name;
    }
}

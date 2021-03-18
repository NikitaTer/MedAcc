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
    private List<Employee> employees = new ArrayList<>();

    public Product(String name, Unit unit, int quantity) {
        this.name = name;
        this.unit = unit;
        this.quantity = quantity;
    }

    public void addRaw(Raw raw) {
        raws.add(raw);
    }

    public void addEmployee(Employee employee) {
        employees.add(employee);
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

    public List<Employee> getEmployees() {
        return employees;
    }

    public void setEmployees(List<Employee> employees) {
        this.employees = employees;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return quantity == product.quantity &&
                Objects.equals(name, product.name) &&
                unit == product.unit &&
                Objects.equals(raws, product.raws) &&
                Objects.equals(employees, product.employees);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, unit, quantity, raws, employees);
    }

    @Override
    public String toString() {
        return name;
    }
}

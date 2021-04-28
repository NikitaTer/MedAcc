package by.nikiter.model.entity;

import java.util.*;

public class Product {
    private String name;
    private Unit unit;
    private int quantity;
    private double salary = 0.0;

    private List<PackagingUnit> packagingUnits = new ArrayList<>();
    private transient Map<Raw, Integer> raws = new HashMap<>();

    public Product(String name, Unit unit, int quantity) {
        this.name = name;
        this.unit = unit;
        this.quantity = quantity;
    }

    public void addRaw(Raw raw, int quantity) {
        raws.put(raw, quantity);
    }

    public void deleteRaw(Raw raw) {
        raws.remove(raw);
    }

    public void addPackaging(PackagingUnit packaging) {
        packagingUnits.add(packaging);
    }

    public void deletePackaging(PackagingUnit packaging) {
        packagingUnits.remove(packaging);
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

    public Map<Raw, Integer> getRaws() {
        return raws;
    }

    public void setRaws(Map<Raw, Integer> raws) {
        this.raws = raws;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public List<PackagingUnit> getPackagingUnits() {
        return packagingUnits;
    }

    public void setPackagingUnits(List<PackagingUnit> packagingUnits) {
        this.packagingUnits = packagingUnits;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Product product = (Product) o;

        if (quantity != product.quantity) return false;
        if (Double.compare(product.salary, salary) != 0) return false;
        if (name != null ? !name.equals(product.name) : product.name != null) return false;
        if (unit != product.unit) return false;
        if (packagingUnits != null ? !packagingUnits.equals(product.packagingUnits) : product.packagingUnits != null)
            return false;
        return raws != null ? raws.equals(product.raws) : product.raws == null;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = name != null ? name.hashCode() : 0;
        result = 31 * result + (unit != null ? unit.hashCode() : 0);
        result = 31 * result + quantity;
        temp = Double.doubleToLongBits(salary);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (packagingUnits != null ? packagingUnits.hashCode() : 0);
        result = 31 * result + (raws != null ? raws.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Product{" +
                "name='" + name + '\'' +
                ", unit=" + unit +
                ", quantity=" + quantity +
                ", salary=" + salary +
                ", packagingUnits=" + packagingUnits +
                ", raws=" + raws +
                '}';
    }
}

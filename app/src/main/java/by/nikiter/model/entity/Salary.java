package by.nikiter.model.entity;

import by.nikiter.model.Currency;

import java.util.Objects;

public class Salary {

    private double value;
    private Currency currency;

    public Salary(double value, Currency currency) {
        this.value = value;
        this.currency = currency;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Salary salary = (Salary) o;
        return Double.compare(salary.value, value) == 0 &&
                currency == salary.currency;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value, currency);
    }
}

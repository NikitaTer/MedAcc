package by.nikiter.model.entity;

import by.nikiter.model.entity.Raw;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

public class ProductGridPane extends GridPane {

    private boolean shouldUpdate = false;
    private TableView<Raw> rawTable = null;
    private TextField salaryField = null;
    private TextField rawsCostField = null;
    private TextField totalCostField = null;
    private TextField profitPercentField = null;
    private TextField profitNumberField = null;
    private TextField productCostField = null;

    public boolean shouldUpdate() {
        return shouldUpdate;
    }

    public void setShouldUpdate(boolean shouldUpdate) {
        this.shouldUpdate = shouldUpdate;
    }

    public TableView<Raw> getRawTable() {
        return rawTable;
    }

    public void setRawTable(TableView<Raw> rawTable) {
        this.rawTable = rawTable;
    }

    public TextField getSalaryField() {
        return salaryField;
    }

    public void setSalaryField(TextField salaryField) {
        this.salaryField = salaryField;
    }

    public TextField getRawsCostField() {
        return rawsCostField;
    }

    public void setRawsCostField(TextField rawsCostField) {
        this.rawsCostField = rawsCostField;
    }

    public TextField getTotalCostField() {
        return totalCostField;
    }

    public void setTotalCostField(TextField totalCostField) {
        this.totalCostField = totalCostField;
    }

    public TextField getProfitPercentField() {
        return profitPercentField;
    }

    public void setProfitPercentField(TextField profitPercentField) {
        this.profitPercentField = profitPercentField;
    }

    public TextField getProfitNumberField() {
        return profitNumberField;
    }

    public void setProfitNumberField(TextField profitNumberField) {
        this.profitNumberField = profitNumberField;
    }

    public TextField getProductCostField() {
        return productCostField;
    }

    public void setProductCostField(TextField productCostField) {
        this.productCostField = productCostField;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public String toString() {
        return super.toString();
    }
}

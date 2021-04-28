package by.nikiter.model.entity;

import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

import java.util.Map;

public class ProductGridPane extends GridPane {

    private TableView<Map.Entry<Raw,Integer>> rawTable = null;
    private TableView<PackagingUnit> packagingTable = null;

    private Label productNameLabel = null;
    private Label productQuantityLabel = null;
    private Label productUnitLabel = null;

    private TextField salaryField = null;

    private TextField rawsCostField = null;
    private TextField totalCostField = null;
    private TextField profitPercentField = null;

    private CheckBox profitPercentBox = null;

    public TableView<Map.Entry<Raw,Integer>> getRawTable() {
        return rawTable;
    }

    public void setRawTable(TableView<Map.Entry<Raw,Integer>> rawTable) {
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

    public CheckBox getProfitPercentBox() {
        return profitPercentBox;
    }

    public void setProfitPercentBox(CheckBox profitPercentBox) {
        this.profitPercentBox = profitPercentBox;
    }

    public Label getProductNameLabel() {
        return productNameLabel;
    }

    public void setProductNameLabel(Label productNameLabel) {
        this.productNameLabel = productNameLabel;
    }

    public Label getProductQuantityLabel() {
        return productQuantityLabel;
    }

    public void setProductQuantityLabel(Label productQuantityLabel) {
        this.productQuantityLabel = productQuantityLabel;
    }

    public Label getProductUnitLabel() {
        return productUnitLabel;
    }

    public void setProductUnitLabel(Label productUnitLabel) {
        this.productUnitLabel = productUnitLabel;
    }

    public TableView<PackagingUnit> getPackagingTable() {
        return packagingTable;
    }

    public void setPackagingTable(TableView<PackagingUnit> packagingTable) {
        this.packagingTable = packagingTable;
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

package by.nikiter.model.entity;

import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

public class ProductGridPane extends GridPane {

    private TableView<Raw> rawTable = null;

    private Label productNameLabel = null;
    private Label productQuantityLabel = null;
    private Label productUnitLabel = null;

    private TextField salaryField = null;

    private TextField rawsCostField = null;
    private TextField totalCostField = null;
    private TextField profitPercentField = null;
    private TextField profitNumberField = null;
    private TextField productCostField = null;

    private RadioButton profitPercentRadio = null;
    private RadioButton profitNumberRadio = null;
    private RadioButton productCostRadio = null;

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

    public RadioButton getProfitPercentRadio() {
        return profitPercentRadio;
    }

    public void setProfitPercentRadio(RadioButton profitPercentRadio) {
        this.profitPercentRadio = profitPercentRadio;
    }

    public RadioButton getProfitNumberRadio() {
        return profitNumberRadio;
    }

    public void setProfitNumberRadio(RadioButton profitNumberRadio) {
        this.profitNumberRadio = profitNumberRadio;
    }

    public RadioButton getProductCostRadio() {
        return productCostRadio;
    }

    public void setProductCostRadio(RadioButton productCostRadio) {
        this.productCostRadio = productCostRadio;
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

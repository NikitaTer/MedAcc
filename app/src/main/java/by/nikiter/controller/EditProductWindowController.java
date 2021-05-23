package by.nikiter.controller;

import by.nikiter.model.Repo;
import by.nikiter.model.entity.Unit;
import by.nikiter.model.entity.Product;
import by.nikiter.util.JsonFileUtil;
import by.nikiter.util.PropManager;
import by.nikiter.util.Regexp;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class EditProductWindowController implements Initializable {

    private Stage stage;
    private Product product;

    @FXML
    private TextField productNameField;

    @FXML
    private TextField quantityField;

    @FXML
    private ComboBox<Unit> unitBox;

    @FXML
    private Label errorLabel;

    @FXML
    private Button editButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ControllersManager.getInstance().setEditProductWindowController(this);

        editButton.setOnAction(e -> {
            if (editProduct()) {
                JsonFileUtil.saveAllProducts();
                ControllersManager.getInstance().getMainWindowController().updateCurrentGrid(false);
                stage.close();
            }
        });

    }

    public void setStage(Stage stage) {
        this.stage = stage;

        stage.setOnCloseRequest(event -> ControllersManager.getInstance().setEditProductWindowController(null));
    }

    public void setProduct(Product product) {
        this.product = product;

        productNameField.setText(product.getName());
        quantityField.setText(String.valueOf(product.getQuantity()));

        if (product.getPackagingUnits().isEmpty()) {
            unitBox.setItems(FXCollections.observableArrayList(Unit.values()));
        } else {
            switch (product.getUnit()) {
                case PIECE:
                    unitBox.setItems(FXCollections.observableArrayList(Unit.PIECE));
                    break;

                case MILLIGRAM:

                case GRAM:

                case KILOGRAM:
                    unitBox.setItems(FXCollections.observableArrayList(Unit.getGramsUnits()));
                    break;

                case MILLILITER:

                case LITER:
                    unitBox.setItems(FXCollections.observableArrayList(Unit.getLitersUnits()));
                    break;

                default:
                    unitBox.setItems(FXCollections.observableArrayList(Unit.values()));
                    break;
            }
        }
        unitBox.setValue(product.getUnit());
    }

    private boolean editProduct() {
        productNameField.getStyleClass().remove("error");
        quantityField.getStyleClass().remove("error");

        String prodName = productNameField.getText().trim().replaceAll(Regexp.DOUBLE_SPACE, " ");
        String prodQuantity = quantityField.getText().trim();

        if (prodName.isEmpty()) {
            errorLabel.setText(PropManager.getLabel("add_pd.error.empty_text"));
            errorLabel.setVisible(true);
            productNameField.getStyleClass().add("error");
            return false;
        }

        for (Product p : Repo.getInstance().getProducts()) {
            if (p.getName().equals(prodName) && p != product) {
                errorLabel.setText(PropManager.getLabel("add_pd.error.dup_prod"));
                errorLabel.setVisible(true);
                productNameField.getStyleClass().add("error");
                return false;
            }
        }

        if (!prodQuantity.matches(Regexp.DOUBLE)) {
            errorLabel.setText(PropManager.getLabel("add_pd.error.wrong_format"));
            errorLabel.setVisible(true);
            quantityField.getStyleClass().add("error");
            return false;
        }

        product.setName(prodName);
        product.setUnit(unitBox.getValue());
        product.setQuantity(Double.parseDouble(prodQuantity));
        return true;
    }
}

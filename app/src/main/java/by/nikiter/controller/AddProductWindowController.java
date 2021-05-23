package by.nikiter.controller;

import by.nikiter.model.Repo;
import by.nikiter.model.entity.Unit;
import by.nikiter.model.entity.Product;

import by.nikiter.util.PropManager;
import by.nikiter.util.Regexp;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class AddProductWindowController implements Initializable {

    private Stage stage;

    @FXML
    private TextField productNameField;

    @FXML
    private TextField quantityField;

    @FXML
    private ComboBox<Unit> unitBox;

    @FXML
    private Label errorLabel;

    @FXML
    private Button addButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ControllersManager.getInstance().setAddProductWindowController(this);

        unitBox.setItems(FXCollections.observableArrayList(Unit.values()));
        unitBox.setValue(Unit.PIECE);

        addButton.setOnAction(e -> {
            if (addProduct()) {
                stage.close();
            }
        });
    }

    public void setStage(Stage stage) {
        this.stage = stage;

        stage.setOnCloseRequest(e -> ControllersManager.getInstance().setAddProductWindowController(null));
    }

    private boolean addProduct() {
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

        for (Product product : Repo.getInstance().getProducts()) {
            if (product.getName().equals(prodName)) {
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

        Product product = new Product(prodName, unitBox.getValue(), Double.parseDouble(prodQuantity));
        Repo.getInstance().addProduct(product);
        return true;
    }
}

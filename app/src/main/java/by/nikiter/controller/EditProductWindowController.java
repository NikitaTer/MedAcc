package by.nikiter.controller;

import by.nikiter.model.Repo;
import by.nikiter.model.Unit;
import by.nikiter.model.entity.Product;
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

        unitBox.setItems(FXCollections.observableArrayList(Unit.values()));
        unitBox.setValue(Unit.PIECE);

        editButton.setOnAction(e -> {
            if (editProduct()) {
                stage.close();
            }
        });

    }

    public void setStage(Stage stage) {
        this.stage = stage;

        stage.setOnCloseRequest(event -> ControllersManager.getInstance().setEditProductWindowController(null));
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

        if (!prodQuantity.matches(Regexp.DECIMAL)) {
            errorLabel.setText(PropManager.getLabel("add_pd.error.wrong_format"));
            errorLabel.setVisible(true);
            quantityField.getStyleClass().add("error");
            return false;
        }

        Product product = new Product(prodName, unitBox.getValue(), Integer.parseInt(prodQuantity));
        Repo.getInstance().addProduct(product);
        return true;
    }
}

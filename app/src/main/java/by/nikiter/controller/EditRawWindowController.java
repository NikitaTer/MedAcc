package by.nikiter.controller;

import by.nikiter.model.entity.Product;
import by.nikiter.model.entity.Raw;
import by.nikiter.util.JsonFileUtil;
import by.nikiter.util.Regexp;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;

public class EditRawWindowController implements Initializable {

    private Stage stage;
    private Map.Entry<Raw,Double> raw;
    private Product product;

    @FXML
    private TextField quantityField;

    @FXML
    private Button editButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        ControllersManager.getInstance().setEditRawWindowController(this);

        editButton.setOnAction(event -> {
            if (editRaw()) {
                JsonFileUtil.saveAllProducts();
                ControllersManager.getInstance().getMainWindowController().updateCurrentGrid(false);
                stage.close();
            }
        });
    }

    public void setStage(Stage stage) {
        this.stage = stage;

        stage.setOnCloseRequest(event -> ControllersManager.getInstance().setEditRawWindowController(null));
    }

    public void setProdRaw(Product product, Map.Entry<Raw,Double> raw) {
        this.product = product;
        this.raw = raw;
        quantityField.setText(Double.toString(raw.getValue()));
    }

    private boolean editRaw() {
        quantityField.getStyleClass().remove("error");

        String rawQuantity = quantityField.getText().trim();

        if (!rawQuantity.matches(Regexp.DOUBLE)) {
            quantityField.getStyleClass().add("error");
            return false;
        }

        product.getRaws().replace(raw.getKey(),Double.parseDouble(quantityField.getText()));

        return true;
    }
}

package by.nikiter.controller;

import by.nikiter.model.Repo;
import by.nikiter.model.Unit;
import by.nikiter.model.entity.Product;
import by.nikiter.model.entity.Raw;
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

public class AddRawWindowController implements Initializable {

    private Stage stage;

    @FXML
    private TextField rawNameField;

    @FXML
    private TextField quantityField;

    @FXML
    private ComboBox<Unit> unitBox;

    @FXML
    private TextField costField;

    @FXML
    private Label errorLabel;

    @FXML
    private Button addButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ControllersManager.getInstance().setAddRawWindowController(this);

        unitBox.setItems(FXCollections.observableArrayList(Unit.values()));
        unitBox.setValue(Unit.PIECE);

        addButton.setOnAction(event -> {
            if (addRaw()) {
                ControllersManager.getInstance().getMainWindowController().updateCurrentTable();
                stage.close();
            }
        });
    }

    public void setStage(Stage stage) {
        this.stage = stage;

        stage.setOnCloseRequest(e -> ControllersManager.getInstance().setAddRawWindowController(null));
    }

    private boolean addRaw() {
        String rawName = rawNameField.getText().trim().replaceAll(Regexp.DOUBLE_SPACE, " ");
        String rawQuantity = quantityField.getText().trim();
        String rawCost = costField.getText().trim();

        if (rawName.isEmpty()) {
            errorLabel.setText(PropManager.getLabel("add_raw.error.empty_text"));
            errorLabel.setVisible(true);
            return false;
        }

        for (Raw raw : Repo.getInstance().getCurrentProduct().getRaws()) {
            if (raw.getName().equals(rawName)) {
                errorLabel.setText(PropManager.getLabel("add_raw.error.dup_raw"));
                errorLabel.setVisible(true);
                return false;
            }
        }

        if (!rawQuantity.matches(Regexp.DECIMAL)) {
            errorLabel.setText(PropManager.getLabel("add_raw.error.wrong_format_quantity"));
            errorLabel.setVisible(true);
            return false;
        }

        if (!rawCost.matches(Regexp.DOUBLE)) {
            errorLabel.setText(PropManager.getLabel("add_raw.error.wrong_format_cost"));
            errorLabel.setVisible(true);
            return false;
        } else if (!rawCost.contains(".")) {
            rawCost = rawCost + ".0";
        }

        Raw raw = new Raw(rawName,Double.parseDouble(rawCost),Integer.parseInt(rawQuantity),unitBox.getValue());
        Repo.getInstance().addRawToCurrent(raw);

        return true;
    }
}

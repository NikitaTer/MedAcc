package by.nikiter.controller;

import by.nikiter.model.Repo;
import by.nikiter.model.Unit;
import by.nikiter.model.entity.Raw;
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

//todo: EditRawWindowController
public class EditRawWindowController implements Initializable {

    private Stage stage;
    private Raw raw;

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
    private Button editButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        ControllersManager.getInstance().setEditRawWindowController(this);

        unitBox.setItems(FXCollections.observableArrayList(Unit.values()));
        unitBox.setValue(Unit.PIECE);

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

    public void setRaw(Raw raw) {
        this.raw = raw;

        rawNameField.setText(raw.getName());
        costField.setText(String.valueOf(raw.getCost()));
        quantityField.setText(String.valueOf(raw.getQuantity()));
        unitBox.setValue(raw.getUnit());
    }

    private boolean editRaw() {
        rawNameField.getStyleClass().remove("error");
        quantityField.getStyleClass().remove("error");
        costField.getStyleClass().remove("error");

        String rawName = rawNameField.getText().trim().replaceAll(Regexp.DOUBLE_SPACE, " ");
        String rawQuantity = quantityField.getText().trim();
        String rawCost = costField.getText().trim();

        if (rawName.isEmpty()) {
            errorLabel.setText(PropManager.getLabel("add_raw.error.empty_text"));
            errorLabel.setVisible(true);
            rawNameField.getStyleClass().add("error");
            return false;
        }

        for (Raw r : Repo.getInstance().getCurrentProduct().getRaws()) {
            if (r.getName().equals(rawName) && r != raw) {
                errorLabel.setText(PropManager.getLabel("add_raw.error.dup_raw"));
                errorLabel.setVisible(true);
                rawNameField.getStyleClass().add("error");
                return false;
            }
        }

        if (!rawQuantity.matches(Regexp.DECIMAL)) {
            errorLabel.setText(PropManager.getLabel("add_raw.error.wrong_format_quantity"));
            errorLabel.setVisible(true);
            quantityField.getStyleClass().add("error");
            return false;
        }

        if (!rawCost.matches(Regexp.DOUBLE)) {
            errorLabel.setText(PropManager.getLabel("add_raw.error.wrong_format_cost"));
            errorLabel.setVisible(true);
            costField.getStyleClass().add("error");
            return false;
        } else if (!rawCost.contains(".")) {
            rawCost = rawCost + ".0";
        }

        raw.setName(rawName);
        raw.setCost(Double.parseDouble(rawCost));
        raw.setQuantity(Integer.parseInt(rawQuantity));
        raw.setUnit(unitBox.getValue());

        return true;
    }
}

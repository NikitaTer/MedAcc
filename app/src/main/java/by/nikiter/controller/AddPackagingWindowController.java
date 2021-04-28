package by.nikiter.controller;

import by.nikiter.model.Repo;
import by.nikiter.model.entity.PackagingUnit;
import by.nikiter.model.entity.Unit;
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

public class AddPackagingWindowController implements Initializable {

    private Stage stage;

    @FXML
    private TextField nameField;

    @FXML
    private TextField productQuantityField;

    @FXML
    private ComboBox<Unit> unitBox;

    @FXML
    private TextField quantityInBoxField;

    @FXML
    private TextField addExpPieceField;

    @FXML
    private TextField addExpSetField;

    @FXML
    private Label errorLabel;

    @FXML
    private Button addButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ControllersManager.getInstance().setAddPackagingWindowController(this);

        switch (Repo.getInstance().getCurrentProduct().getUnit()) {
            case PIECE:
                unitBox.setItems(FXCollections.observableArrayList(Unit.PIECE));
                unitBox.setValue(Unit.PIECE);
                break;

            case MILLIGRAM:

            case GRAM:

            case KILOGRAM:
                unitBox.setItems(FXCollections.observableArrayList(Unit.getGramsUnits()));
                unitBox.setValue(Unit.MILLIGRAM);
                break;

            case MILLILITER:

            case LITER:
                unitBox.setItems(FXCollections.observableArrayList(Unit.getLitersUnits()));
                unitBox.setValue(Unit.MILLILITER);
                break;

            default:
                unitBox.setItems(FXCollections.observableArrayList(Unit.values()));
                unitBox.setValue(Unit.PIECE);
                break;
        }

        addButton.setOnAction(event -> {
            if (addPackaging()) {
                stage.close();
            }
        });
    }

    public void setStage(Stage stage) {
        this.stage = stage;

        stage.setOnCloseRequest(event -> ControllersManager.getInstance().setAddPackagingWindowController(null));
    }

    private boolean addPackaging() {
        nameField.getStyleClass().remove("error");
        productQuantityField.getStyleClass().remove("error");
        addExpPieceField.getStyleClass().remove("error");
        quantityInBoxField.getStyleClass().remove("error");
        addExpSetField.getStyleClass().remove("error");

        String name = nameField.getText().trim().replaceAll(Regexp.DOUBLE_SPACE, " ");
        String productQuantity = productQuantityField.getText().trim();
        String addExpPiece = addExpPieceField.getText().trim();
        String quantityInBox = quantityInBoxField.getText().trim();
        String addExpSet = addExpSetField.getText().trim();

        if (name.isEmpty()) {
            errorLabel.setText(PropManager.getLabel("add_pack.error.empty"));
            errorLabel.setVisible(true);
            nameField.getStyleClass().add("error");
            return false;
        }

        if (productQuantity.isEmpty()) {
            errorLabel.setText(PropManager.getLabel("add_pack.error.empty"));
            errorLabel.setVisible(true);
            productQuantityField.getStyleClass().add("error");
            return false;
        }

        if (addExpPiece.isEmpty()) {
            errorLabel.setText(PropManager.getLabel("add_pack.error.empty"));
            errorLabel.setVisible(true);
            addExpPieceField.getStyleClass().add("error");
            return false;
        }

        if (quantityInBox.isEmpty()) {
            errorLabel.setText(PropManager.getLabel("add_pack.error.empty"));
            errorLabel.setVisible(true);
            quantityInBoxField.getStyleClass().add("error");
            return false;
        }

        if (addExpSet.isEmpty()) {
            errorLabel.setText(PropManager.getLabel("add_pack.error.empty"));
            errorLabel.setVisible(true);
            addExpSetField.getStyleClass().add("error");
            return false;
        }

        if (!productQuantity.matches(Regexp.DECIMAL)) {
            errorLabel.setText(PropManager.getLabel("add_pack.error.product_quantity"));
            errorLabel.setVisible(true);
            productQuantityField.getStyleClass().add("error");
            return false;
        }

        if (!addExpPiece.matches(Regexp.DOUBLE)) {
            errorLabel.setText(PropManager.getLabel("add_pack.error.add_exp_piece"));
            errorLabel.setVisible(true);
            addExpPieceField.getStyleClass().add("error");
            return false;
        }

        if (!quantityInBox.matches(Regexp.DECIMAL)) {
            errorLabel.setText(PropManager.getLabel("add_pack.error.quantity_in_box"));
            errorLabel.setVisible(true);
            quantityInBoxField.getStyleClass().add("error");
            return false;
        }

        if (!addExpSet.matches(Regexp.DOUBLE)) {
            errorLabel.setText(PropManager.getLabel("add_pack.error.add_exp_set"));
            errorLabel.setVisible(true);
            addExpSetField.getStyleClass().add("error");
            return false;
        }

        PackagingUnit packaging = new PackagingUnit(
                name,
                Integer.parseInt(productQuantity),
                unitBox.getValue(),
                Double.parseDouble(addExpPiece),
                Integer.parseInt(quantityInBox),
                Double.parseDouble(addExpSet)
        );
        Repo.getInstance().addPackagingToCurrent(packaging);
        ControllersManager.getInstance().getMainWindowController().updateCurrentGrid(false);
        return true;
    }
}

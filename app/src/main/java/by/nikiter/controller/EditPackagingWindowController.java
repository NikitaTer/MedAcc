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

public class EditPackagingWindowController implements Initializable {

    private Stage stage = null;
    private PackagingUnit packaging = null;

    @FXML
    private TextField nameField;

    @FXML
    private TextField productQuantityField;

    @FXML
    private TextField quantityInBoxField;

    @FXML
    private TextField addExpPieceField;

    @FXML
    private TextField addExpSetField;

    @FXML
    private TextField piecePriceField;

    @FXML
    private TextField setPriceField;

    @FXML
    private ComboBox<Unit> unitBox;

    @FXML
    private Label errorLabel;

    @FXML
    private Button editButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        editButton.setOnAction(event -> {
            if (editPackaging()) {
                stage.close();
            }
        });
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setPackaging(PackagingUnit packaging) {
        this.packaging = packaging;

        nameField.setText(packaging.getName());
        productQuantityField.setText(String.valueOf(packaging.getProductQuantity()));
        quantityInBoxField.setText(String.valueOf(packaging.getQuantityInBox()));
        addExpPieceField.setText(String.valueOf(packaging.getAddExpPiece()));
        addExpSetField.setText(String.valueOf(packaging.getAddExpSet()));
        piecePriceField.setText(String.valueOf(packaging.getPiecePrice()));
        setPriceField.setText(String.valueOf(packaging.getSetPrice()));

        switch (packaging.getUnit()) {
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
        unitBox.setValue(packaging.getUnit());
    }

    private boolean editPackaging() {
        nameField.getStyleClass().remove("error");
        productQuantityField.getStyleClass().remove("error");
        addExpPieceField.getStyleClass().remove("error");
        quantityInBoxField.getStyleClass().remove("error");
        addExpSetField.getStyleClass().remove("error");
        piecePriceField.getStyleClass().remove("error");
        setPriceField.getStyleClass().remove("error");

        String name = nameField.getText().trim().replaceAll(Regexp.DOUBLE_SPACE, " ");
        String productQuantity = productQuantityField.getText().trim();
        String addExpPiece = addExpPieceField.getText().trim();
        String quantityInBox = quantityInBoxField.getText().trim();
        String addExpSet = addExpSetField.getText().trim();
        String piecePrice = piecePriceField.getText().trim();
        String setPrice = setPriceField.getText().trim();

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

        if (piecePrice.isEmpty()) {
            errorLabel.setText(PropManager.getLabel("add_pack.error.empty"));
            errorLabel.setVisible(true);
            piecePriceField.getStyleClass().add("error");
            return false;
        }

        if (setPrice.isEmpty()) {
            errorLabel.setText(PropManager.getLabel("add_pack.error.empty"));
            errorLabel.setVisible(true);
            setPriceField.getStyleClass().add("error");
            return false;
        }

        if (!productQuantity.matches(Regexp.DOUBLE)) {
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

        if (!piecePrice.matches(Regexp.DOUBLE)) {
            errorLabel.setText(PropManager.getLabel("edit_pack.error.price"));
            errorLabel.setVisible(true);
            piecePriceField.getStyleClass().add("error");
            return false;
        }

        if (!setPrice.matches(Regexp.DOUBLE)) {
            errorLabel.setText(PropManager.getLabel("edit_pack.error.price"));
            errorLabel.setVisible(true);
            setPriceField.getStyleClass().add("error");
            return false;
        }

        packaging.setName(name);
        packaging.setProductQuantity(Double.parseDouble(productQuantity));
        packaging.setQuantityInBox(Integer.parseInt(quantityInBox));
        packaging.setAddExpPiece(Double.parseDouble(addExpPiece));
        packaging.setAddExpSet(Double.parseDouble(addExpSet));
        packaging.setPiecePrice(Double.parseDouble(piecePrice));
        packaging.setSetPrice(Double.parseDouble(setPrice));
        packaging.setUnit(unitBox.getValue());

        ControllersManager.getInstance().getMainWindowController().updateCurrentGrid(false,packaging);
        return true;
    }
}

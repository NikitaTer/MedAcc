package by.nikiter.controller;

import by.nikiter.model.Repo;
import by.nikiter.model.Unit;
import by.nikiter.model.entity.Raw;
import by.nikiter.util.JsonFileUtil;
import by.nikiter.util.PropManager;
import by.nikiter.util.Regexp;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class NewRawWindowController implements Initializable {

    private Stage stage;

    private final static double LIST_WIDTH = 465.0;
    private final static double LIST_HEIGHT = 426.0;
    private final static double ADD_WIDTH = 340.0;
    private final static double ADD_HEIGHT = 222.0;

    private Raw editableRaw = null;

    @FXML
    private StackPane stackPane;

    @FXML
    private VBox addVBox;

    @FXML
    private TextField rawNameField;

    @FXML
    private TextField costField;

    @FXML
    private TextField searchField;

    @FXML
    private ComboBox<Unit> unitBox;

    @FXML
    private Label errorLabel;

    @FXML
    private Button addButton;

    @FXML
    private VBox listVBox;

    @FXML
    private ListView<Raw> rawList;

    @FXML
    private Button newButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ControllersManager.getInstance().setNewRawWindowController(this);

        //set up search
        FilteredList<Raw> filteredList = new FilteredList<>(Repo.getInstance().getRaws());
        searchField.textProperty().addListener(((observable, oldValue, newValue) -> {
            filteredList.setPredicate(raw -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                return raw.getName().toLowerCase().contains(newValue.toLowerCase());
            });
        }));
        SortedList<Raw> sortedList = new SortedList<>(filteredList);

        //set up list
        rawList.setItems(sortedList);
        rawList.setCellFactory( param -> {
            ListCell<Raw> cell = new ListCell<Raw>() {

                @Override
                protected void updateItem(Raw item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item != null) {
                        setText(String.format("%s (%s)",item.getName(),item.getUnit().getNameShort()));
                    } else {
                        setText("");
                    }
                }
            };

            MenuItem edit = new MenuItem(PropManager.getLabel("new_raw.context_menu.edit"));
            edit.setOnAction(event -> {
                if (!cell.isEmpty()) {
                    listVBox.setVisible(false);
                    stackPane.setPrefSize(ADD_WIDTH,ADD_HEIGHT);
                    stage.setWidth(ADD_WIDTH);
                    stage.setHeight(ADD_HEIGHT);

                    addButton.setText(PropManager.getLabel("add_raw.edit_button"));
                    rawNameField.setText(cell.getItem().getName());
                    costField.setText(String.valueOf(cell.getItem().getCost()));
                    unitBox.setValue(cell.getItem().getUnit());

                    addVBox.setVisible(true);

                    editableRaw = cell.getItem();
                }
            });

            MenuItem delete = new MenuItem(PropManager.getLabel("new_raw.context_menu.delete"));
            delete.setOnAction(event -> {
                if (!cell.isEmpty()) {
                    deleteRaw(cell.getItem());
                }
            });

            ContextMenu contextMenu = new ContextMenu(edit,delete);
            cell.setContextMenu(contextMenu);

            return cell;
        });

        unitBox.setItems(FXCollections.observableArrayList(Unit.values()));
        unitBox.setValue(Unit.PIECE);

        newButton.setOnAction(event -> {
            addButton.setText(PropManager.getLabel("add_raw.add_button"));
            listVBox.setVisible(false);
            stackPane.setPrefSize(ADD_WIDTH,ADD_HEIGHT);
            stage.setWidth(ADD_WIDTH);
            stage.setHeight(ADD_HEIGHT);
            addVBox.setVisible(true);
        });

        addButton.setOnAction(event -> {
            if (addButton.getText().equals(PropManager.getLabel("add_raw.add_button"))) {
                if (addRaw()) {
                    addVBox.setVisible(false);
                    stackPane.setPrefSize(LIST_WIDTH,LIST_HEIGHT);
                    stage.setWidth(LIST_WIDTH);
                    stage.setHeight(LIST_HEIGHT);
                    listVBox.setVisible(true);
                }
            } else {
                if (editRaw()) {
                    addVBox.setVisible(false);
                    stackPane.setPrefSize(LIST_WIDTH,LIST_HEIGHT);
                    stage.setWidth(LIST_WIDTH);
                    stage.setHeight(LIST_HEIGHT);
                    listVBox.setVisible(true);
                }
            }
        });

    }

    public void setStage(Stage stage) {
        this.stage = stage;

        stage.setOnCloseRequest(event -> ControllersManager.getInstance().setNewRawWindowController(null));
    }

    private boolean addRaw() {
        rawNameField.getStyleClass().remove("error");
        costField.getStyleClass().remove("error");

        String rawName = rawNameField.getText().trim().replaceAll(Regexp.DOUBLE_SPACE, " ");
        String rawCost = costField.getText().trim();

        //Empty name
        if (rawName.isEmpty()) {
            errorLabel.setText(PropManager.getLabel("add_raw.error.empty_text"));
            errorLabel.setVisible(true);
            rawNameField.getStyleClass().add("error");
            return false;
        }

        //Duplicate name
        for (Raw raw : Repo.getInstance().getRaws()) {
            if (raw.getName().equals(rawName)) {
                errorLabel.setText(PropManager.getLabel("add_raw.error.dup_raw"));
                errorLabel.setVisible(true);
                rawNameField.getStyleClass().add("error");
                return false;
            }
        }

        //Wrong format on cost
        if (!rawCost.matches(Regexp.DOUBLE)) {
            errorLabel.setText(PropManager.getLabel("add_raw.error.wrong_format_cost"));
            errorLabel.setVisible(true);
            costField.getStyleClass().add("error");
            return false;
        } else if (!rawCost.contains(".")) {
            rawCost = rawCost + ".0";
        }

        Raw raw = new Raw(rawName,Double.parseDouble(rawCost),unitBox.getValue());
        Repo.getInstance().addRaw(raw);
        rawList.refresh();

        rawNameField.setText("");
        costField.setText("");
        unitBox.setValue(Unit.PIECE);

        return true;
    }

    private boolean editRaw() {
        if (editableRaw == null) {
            return false;
        }

        rawNameField.getStyleClass().remove("error");
        costField.getStyleClass().remove("error");

        String rawName = rawNameField.getText().trim().replaceAll(Regexp.DOUBLE_SPACE, " ");
        String rawCost = costField.getText().trim();

        //Empty name
        if (rawName.isEmpty()) {
            errorLabel.setText(PropManager.getLabel("add_raw.error.empty_text"));
            errorLabel.setVisible(true);
            rawNameField.getStyleClass().add("error");
            return false;
        }

        //Duplicate name
        for (Raw raw : Repo.getInstance().getRaws()) {
            if (raw.getName().equals(rawName) && raw != editableRaw) {
                errorLabel.setText(PropManager.getLabel("add_raw.error.dup_raw"));
                errorLabel.setVisible(true);
                rawNameField.getStyleClass().add("error");
                return false;
            }
        }

        //Wrong format on cost
        if (!rawCost.matches(Regexp.DOUBLE)) {
            errorLabel.setText(PropManager.getLabel("add_raw.error.wrong_format_cost"));
            errorLabel.setVisible(true);
            costField.getStyleClass().add("error");
            return false;
        } else if (!rawCost.contains(".")) {
            rawCost = rawCost + ".0";
        }

        editableRaw.setName(rawName);
        editableRaw.setCost(Double.parseDouble(rawCost));
        editableRaw.setUnit(unitBox.getValue());
        JsonFileUtil.saveAll();
        rawList.refresh();

        rawNameField.setText("");
        costField.setText("");
        unitBox.setValue(Unit.PIECE);

        return true;

    }

    private void deleteRaw(Raw raw) {
        Repo.getInstance().deleteRaw(raw);
        rawList.refresh();
    }
}

package by.nikiter.controller;

import by.nikiter.model.Repo;
import by.nikiter.model.entity.Unit;
import by.nikiter.model.entity.Raw;
import by.nikiter.util.JsonFileUtil;
import by.nikiter.util.PropManager;
import by.nikiter.util.Regexp;
import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class AddRawWindowController implements Initializable {

    private Stage stage;

    private final static double ADD_WIDTH = 340.0;
    private final static double ADD_HEIGHT = 242.0;
    private final static double CHOOSE_WIDTH = 465.0;
    private final static double CHOOSE_HEIGHT = 426.0;

    private Raw editableRaw = null;

    @FXML
    private StackPane stackPane;

    @FXML
    private VBox addVBox;

    @FXML
    private TextField rawNameField;

    @FXML
    private TextField quantityField;

    @FXML
    private ComboBox<Unit> unitBox;

    @FXML
    private TextField costField;

    @FXML
    private TextField searchField;

    @FXML
    private Label errorLabel;

    @FXML
    private Button addNewButton;

    @FXML
    private VBox chooseVBox;

    @FXML
    private ListView<Raw> rawList;

    @FXML
    private Button addButton;

    @FXML
    private Button newButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ControllersManager.getInstance().setAddRawWindowController(this);

        //set up search
        FilteredList<Raw> filteredList = new FilteredList<>(Repo.getInstance().getRaws(), p -> true);
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
                    chooseVBox.setVisible(false);
                    stackPane.setPrefSize(ADD_WIDTH, ADD_HEIGHT);
                    stage.setWidth(ADD_WIDTH);
                    stage.setHeight(ADD_HEIGHT);

                    addNewButton.setText(PropManager.getLabel("add_raw.edit_button"));
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
            addNewButton.setText(PropManager.getLabel("add_raw.add_button"));
            chooseVBox.setVisible(false);
            stackPane.setPrefSize(ADD_WIDTH, ADD_HEIGHT);
            stage.setWidth(ADD_WIDTH);
            stage.setHeight(ADD_HEIGHT);
            addVBox.setVisible(true);
        });

        addNewButton.setOnAction(event -> {
            if (addNewButton.getText().equals(PropManager.getLabel("add_raw.add_button"))) {
                if (newRaw()) {
                    addVBox.setVisible(false);
                    stackPane.setPrefSize(CHOOSE_WIDTH, CHOOSE_HEIGHT);
                    stage.setWidth(CHOOSE_WIDTH);
                    stage.setHeight(CHOOSE_HEIGHT);
                    chooseVBox.setVisible(true);
                }
            } else {
                if (editRaw()) {
                    addVBox.setVisible(false);
                    stackPane.setPrefSize(CHOOSE_WIDTH, CHOOSE_HEIGHT);
                    stage.setWidth(CHOOSE_WIDTH);
                    stage.setHeight(CHOOSE_HEIGHT);
                    chooseVBox.setVisible(true);
                }
            }
        });

        addButton.setOnAction(event -> {
            if (addRaw()) {
                stage.close();
            }
        });
    }

    public void setStage(Stage stage) {
        this.stage = stage;

        stage.setOnCloseRequest(e -> ControllersManager.getInstance().setAddRawWindowController(null));
    }

    private boolean newRaw() {
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

    private boolean addRaw() {
        Raw raw = rawList.getSelectionModel().getSelectedItem();
        if (raw == null) {
            if (chooseVBox.getChildren().size() == 4) {
                Label errorChooseLabel = new Label(PropManager.getLabel("add_raw.error.nothing_selected"));
                errorChooseLabel.setTextFill(Paint.valueOf("RED"));
                chooseVBox.getChildren().add(1,errorChooseLabel);
            }
            return false;
        }

        quantityField.getStyleClass().remove("error");
        String rawQuantity = quantityField.getText().trim();

        //Wrong format on quantity
        if (!rawQuantity.matches(Regexp.DOUBLE)) {
            quantityField.getStyleClass().add("error");
            return false;
        }

        Repo.getInstance().addRawToCurrent(raw,Double.parseDouble(rawQuantity));
        ControllersManager.getInstance().getMainWindowController().updateCurrentGrid(false);
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
        ControllersManager.getInstance().getMainWindowController().updateCurrentGrid(false);

        rawNameField.setText("");
        costField.setText("");
        unitBox.setValue(Unit.PIECE);

        return true;
    }

    private void deleteRaw(Raw raw) {
        Repo.getInstance().deleteRaw(raw);
    }
}

package by.nikiter.controller;

import by.nikiter.model.Repo;
import by.nikiter.model.Unit;
import by.nikiter.model.entity.Product;
import by.nikiter.model.entity.Raw;
import by.nikiter.util.PropManager;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.AccessibleRole;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class MainWindowController implements Initializable {

    private Stage stage;

    private Map<Product, GridPane> tables = new HashMap<>();

    @FXML
    private Menu langMenu;

    @FXML
    private MenuItem closeMenuItem;

    @FXML
    private ListView<Product> prodListView;

    @FXML
    private Button plusButton;

    @FXML
    private Button minusButton;

    @FXML
    private StackPane tablesStackPane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ControllersManager.getInstance().setMainWindowController(this);
        //todo: languages init

        prodListView.setItems(Repo.getInstance().getProducts());
        prodListView.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> {
            buildTables(newValue);
            Repo.getInstance().setCurrentProduct(newValue);
        }));

        plusButton.setOnAction(e -> openAddProductWindow());

        closeMenuItem.setOnAction(e -> Platform.exit());
    }

    public void setStage(Stage stage) {
        this.stage = stage;

        stage.setOnCloseRequest(e -> ControllersManager.getInstance().setMainWindowController(null));
    }

    public void updateCurrentTable() {
        tables.remove(Repo.getInstance().getCurrentProduct());
        buildTables(Repo.getInstance().getCurrentProduct());
    }

    private void openAddProductWindow() {
        Stage addProductStage = new Stage();
        addProductStage.setTitle(PropManager.getLabel("add_pd.name"));

        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/view/AddProductWindow.fxml"),
                ResourceBundle.getBundle("labels")
        );

        try {
            Parent root = loader.load();
            ((AddProductWindowController)loader.getController()).setStage(addProductStage);

            addProductStage.setScene(new Scene(root));
            addProductStage.initModality(Modality.WINDOW_MODAL);
            addProductStage.initOwner(stage);
            addProductStage.setResizable(false);
            addProductStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void buildTables(Product product) {

        if (tables.get(product) != null) {
            showTable(tables.get(product));
            return;
        }

        GridPane gridPane = new GridPane();
        tables.put(product, gridPane);

        Label productNameLabel = new Label(product.getName());
        productNameLabel.setFont(Font.font(14));
        Label productQuantityLabel = new Label(Integer.toString(product.getQuantity()));
        productQuantityLabel.setFont(Font.font(14));
        Label productUnitLabel = new Label(product.getUnit().getNameShort());
        productUnitLabel.setFont(Font.font(14));
        HBox nameBox = new HBox(5,productNameLabel,productQuantityLabel,productUnitLabel);

        Node rawTable = buildRawTable(product);

        VBox vBox = new VBox(5,nameBox,rawTable);

        gridPane.add(vBox,0,0);
        tablesStackPane.getChildren().add(gridPane);
        showTable(gridPane);
    }

    private Node buildRawTable(Product product) {

        Label tableNameLabel = new Label(PropManager.getLabel("main.table.raw.table_name"));

        Label addRawLabel = new Label(PropManager.getLabel("main.table.raw.add"));
        addRawLabel.setAccessibleRole(AccessibleRole.BUTTON);
        addRawLabel.setUnderline(true);
        addRawLabel.setOnMouseClicked(e -> {
            openAddRawWindow();
        });

        TableView<Raw> rawTable = new TableView<Raw>(FXCollections.observableList(product.getRaws()));
        rawTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        rawTable.setRowFactory(tv -> {
            TableRow<Raw> row = new TableRow<>();
            row.setOnMouseClicked(e -> {
                if (e.getClickCount() == 2 && !row.isEmpty()) {
                    System.out.println("Worked");
                }
            });
            return row;
        });

        TableColumn<Raw,String> nameColumn = new TableColumn<>(PropManager.getLabel("main.table.raw.name"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<Raw,String>("name"));
        rawTable.getColumns().add(nameColumn);

        TableColumn<Raw,Double> costColumn = new TableColumn<>(PropManager.getLabel("main.table.raw.cost"));
        costColumn.setCellValueFactory(new PropertyValueFactory<Raw,Double>("cost"));
        rawTable.getColumns().add(costColumn);

        TableColumn<Raw,Integer> quantityColumn = new TableColumn<>(PropManager.getLabel("main.table.raw.quantity"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<Raw,Integer>("quantity"));
        rawTable.getColumns().add(quantityColumn);

        TableColumn<Raw,Unit> unitColumn = new TableColumn<>(PropManager.getLabel("main.table.raw.unit"));
        unitColumn.setCellValueFactory(new PropertyValueFactory<Raw,Unit>("unit"));
        rawTable.getColumns().add(unitColumn);

        return new VBox(5, new HBox(20,tableNameLabel,addRawLabel),rawTable);
    }

    private void showTable(GridPane gridPane) {
        tables.forEach((p, g) -> g.setVisible(false));
        gridPane.setVisible(true);
    }

    private void openAddRawWindow() {
        Stage addRawStage = new Stage();
        addRawStage.setTitle(PropManager.getLabel("add_raw.name"));

        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/view/AddRawWindow.fxml"),
                ResourceBundle.getBundle("labels")
        );

        try {
            Parent root = loader.load();
            ((AddRawWindowController)loader.getController()).setStage(addRawStage);

            addRawStage.setScene(new Scene(root));
            addRawStage.initModality(Modality.WINDOW_MODAL);
            addRawStage.initOwner(stage);
            addRawStage.setResizable(false);
            addRawStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

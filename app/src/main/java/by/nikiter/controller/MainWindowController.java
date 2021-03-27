package by.nikiter.controller;

import by.nikiter.model.entity.ProductGridMap;
import by.nikiter.model.entity.ProductGridPane;
import by.nikiter.model.Repo;
import by.nikiter.model.Unit;
import by.nikiter.model.entity.Product;
import by.nikiter.model.entity.Raw;
import by.nikiter.model.format.CellFormatter;
import by.nikiter.util.CalcUtil;
import by.nikiter.util.PropManager;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.AccessibleRole;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class MainWindowController implements Initializable {

    private Stage stage;

    private final ProductGridMap productGrids = new ProductGridMap();

    @FXML
    private Menu langMenu;

    @FXML
    private MenuItem closeMenuItem;

    @FXML
    private HBox mainHBox;

    @FXML
    private ListView<Product> prodListView;

    @FXML
    private Button plusButton;

    @FXML
    private Button minusButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ControllersManager.getInstance().setMainWindowController(this);
        //todo: languages init

        prodListView.setItems(Repo.getInstance().getProducts());
        if (Repo.getInstance().getCurrentProduct() != null) {
            prodListView.getSelectionModel().select(Repo.getInstance().getCurrentProduct());
            buildGrid(Repo.getInstance().getCurrentProduct());
        }
        prodListView.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> {
            buildGrid(newValue);
            Repo.getInstance().setCurrentProduct(newValue);
        }));

        plusButton.setOnAction(e -> openAddProductWindow());
        minusButton.setOnAction(e -> deleteProduct(Repo.getInstance().getCurrentProduct()));

        closeMenuItem.setOnAction(e -> Platform.exit());
    }

    public void setStage(Stage stage) {
        this.stage = stage;

        stage.setOnCloseRequest(e -> ControllersManager.getInstance().setMainWindowController(null));
    }

    private void buildGrid(Product product) {

        if (productGrids.get(product) != null) {
            showGrid(productGrids.get(product));
            return;
        }

        ProductGridPane gridPane = new ProductGridPane();
        gridPane.setVgap(5);
        gridPane.setHgap(5);
        HBox.setHgrow(gridPane, Priority.ALWAYS);
        productGrids.add(product, gridPane);

        Label productNameLabel = new Label(product.getName());
        productNameLabel.setFont(Font.font(14));
        Label productQuantityLabel = new Label(Integer.toString(product.getQuantity()));
        productQuantityLabel.setFont(Font.font(14));
        Label productUnitLabel = new Label(product.getUnit().getNameShort());
        productUnitLabel.setFont(Font.font(14));
        HBox nameBox = new HBox(5, productNameLabel, productQuantityLabel, productUnitLabel);

        Node rawNode = buildRawNode(product);
        Node salaryNode = buildSalaryNode(product);
        Node calcNode = buildCalcNode(product);

        VBox vBox = new VBox(5, nameBox, rawNode);
        GridPane.setHgrow(vBox, Priority.ALWAYS);

        gridPane.add(vBox, 0, 0);
        gridPane.add(salaryNode, 0, 1);
        gridPane.add(calcNode, 1,0);
        showGrid(gridPane);
    }

    private Node buildRawNode(Product product) {

        Label tableNameLabel = new Label(PropManager.getLabel("main.table.raw.table_name"));

        Label addRawLabel = new Label(PropManager.getLabel("main.table.raw.add"));
        addRawLabel.setAccessibleRole(AccessibleRole.BUTTON);
        addRawLabel.setUnderline(true);
        addRawLabel.setOnMouseClicked(e -> {
            openAddRawWindow();
        });

        TableView<Raw> rawTable = new TableView<Raw>(FXCollections.observableList(product.getRaws()));
        productGrids.get(product).setRawTable(rawTable);
        rawTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        rawTable.setRowFactory(tv -> {
            TableRow<Raw> row = new TableRow<>();
            row.setOnMouseClicked(e -> {
                if (e.getClickCount() == 2 && !row.isEmpty()) {
                    openEditRawWindow(row.getItem());
                }
            });
            return row;
        });

        TableColumn<Raw,String> nameColumn = new TableColumn<>(PropManager.getLabel("main.table.raw.name"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<Raw,String>("name"));
        rawTable.getColumns().add(nameColumn);

        TableColumn<Raw,Double> costColumn = new TableColumn<>(PropManager.getLabel("main.table.raw.cost"));
        costColumn.setCellValueFactory(new PropertyValueFactory<Raw,Double>("cost"));
        costColumn.setCellFactory(CellFormatter.getRawCostFormat());
        rawTable.getColumns().add(costColumn);

        TableColumn<Raw,Integer> quantityColumn = new TableColumn<>(PropManager.getLabel("main.table.raw.quantity"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<Raw,Integer>("quantity"));
        rawTable.getColumns().add(quantityColumn);

        TableColumn<Raw,Unit> unitColumn = new TableColumn<>(PropManager.getLabel("main.table.raw.unit"));
        unitColumn.setCellValueFactory(new PropertyValueFactory<Raw,Unit>("unit"));
        rawTable.getColumns().add(unitColumn);

        return new VBox(5, new HBox(20,tableNameLabel,addRawLabel),rawTable);
    }

    private Node buildSalaryNode(Product product) {
        Label salaryLabel = new Label(PropManager.getLabel("main.salary"));
        TextField salaryField = new TextField();
        productGrids.get(product).setSalaryField(salaryField);
        salaryField.setPrefWidth(60.0);
        salaryField.setText(String.format(Locale.US,"%.2f",product.getSalary()));
        HBox salaryBox = new HBox(5,salaryLabel,salaryField);
        salaryBox.setAlignment(Pos.CENTER_LEFT);
        return salaryBox;
    }

    private Node buildCalcNode(Product product) {
        Label rawsCostLabel = new Label(PropManager.getLabel("main.calc.raws.cost"));
        Label totalCostLabel = new Label(PropManager.getLabel("main.calc.total.cost"));
        Label profitPercentLabel = new Label(PropManager.getLabel("main.calc.profit.percent"));
        Label profitNumberLabel = new Label(PropManager.getLabel("main.calc.profit.number"));
        Label productCostLabel = new Label(PropManager.getLabel("main.calc.product.cost"));

        TextField rawsCostField = new TextField();
        rawsCostField.setEditable(false);
        TextField totalCostField = new TextField();
        totalCostField.setEditable(false);
        TextField profitPercentField = new TextField();
        TextField profitNumberField = new TextField();
        TextField productCostField = new TextField();

        productGrids.get(product).setRawsCostField(rawsCostField);
        productGrids.get(product).setTotalCostField(totalCostField);
        productGrids.get(product).setProfitPercentField(profitPercentField);
        productGrids.get(product).setProfitNumberField(profitNumberField);
        productGrids.get(product).setProductCostField(productCostField);

        RadioButton profitPercentRadio = new RadioButton();
        RadioButton profitNumberRadio = new RadioButton();
        RadioButton productCostRadio = new RadioButton();
        productCostRadio.setSelected(true);

        ToggleGroup toggleGroup = new ToggleGroup();
        profitPercentRadio.setToggleGroup(toggleGroup);
        profitNumberRadio.setToggleGroup(toggleGroup);
        productCostRadio.setToggleGroup(toggleGroup);

        rawsCostField.setText(String.format(Locale.US,"%.2f",CalcUtil.calcRawsCost(product)));
        totalCostField.setText(String.format(Locale.US,"%.2f",CalcUtil.calcTotalCost(product)));
        profitPercentField.setText(String.format(Locale.US,"%.2f",CalcUtil.calcProfitPercent(product)));
        profitNumberField.setText(String.format(Locale.US,"%.2f",CalcUtil.calcProfitNumber(product)));
        productCostField.setText(String.format(Locale.US,"%.2f",product.getCost()));

        GridPane gridPane = new GridPane();
        gridPane.setHgap(5);
        gridPane.setVgap(5);

        gridPane.addRow(0,rawsCostLabel,rawsCostField);
        gridPane.addRow(1,totalCostLabel,totalCostField);
        gridPane.addRow(2,profitPercentLabel,profitPercentField,profitPercentRadio);
        gridPane.addRow(3,profitNumberLabel,profitNumberField,profitNumberRadio);
        gridPane.addRow(4,productCostLabel,productCostField,productCostRadio);

        return gridPane;
    }

    private void showGrid(ProductGridPane gridPane) {
        if (mainHBox.getChildren().size() > 1 && mainHBox.getChildren().get(1) instanceof ProductGridPane) {
            mainHBox.getChildren().remove(1);
        }
        mainHBox.getChildren().add(1,gridPane);
    }

    public void updateCurrentGrid() {
        Product product = Repo.getInstance().getCurrentProduct();
        ProductGridPane gridPane = productGrids.get(product);

        gridPane.getRawTable().setItems(FXCollections.observableArrayList(product.getRaws()));
        gridPane.getRawTable().refresh();

        gridPane.getSalaryField().setText(String.valueOf(product.getSalary()));

        gridPane.getRawsCostField().setText(String.valueOf(CalcUtil.calcRawsCost(product)));
        gridPane.getTotalCostField().setText(String.valueOf(CalcUtil.calcTotalCost(product)));
        gridPane.getProfitPercentField().setText(String.valueOf(CalcUtil.calcProfitPercent(product)));
        gridPane.getProfitNumberField().setText(String.valueOf(CalcUtil.calcProfitNumber(product)));
        gridPane.getProductCostField().setText(String.valueOf(product.getCost()));
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
            root.getStylesheets().add("styles/style.css");
            addProductStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void deleteProduct(Product product) {
        if (product == null) {
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(PropManager.getLabel("main.alert.delete.title"));
        alert.setHeaderText(PropManager.getLabel("main.alert.delete.body"));
        alert.setContentText(null);

        ButtonType yesButton = new ButtonType(PropManager.getLabel("main.alert.delete.yes"), ButtonBar.ButtonData.YES);
        ButtonType noButton = new ButtonType(PropManager.getLabel("main.alert.delete.no"), ButtonBar.ButtonData.NO);

        alert.getButtonTypes().setAll(yesButton,noButton);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == yesButton) {
            Repo.getInstance().deleteProduct(product);
            productGrids.remove(product);
        }
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
            root.getStylesheets().add("styles/style.css");
            addRawStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void openEditRawWindow(Raw raw) {
        Stage editRawStage = new Stage();
        editRawStage.setTitle(PropManager.getLabel("edit_raw.name"));

        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/view/EditRawWindow.fxml"),
                ResourceBundle.getBundle("labels")
        );

        try {
            Parent root = loader.load();
            ((EditRawWindowController)loader.getController()).setStage(editRawStage);
            ((EditRawWindowController)loader.getController()).setRaw(raw);

            editRawStage.setScene(new Scene(root));
            editRawStage.initModality(Modality.WINDOW_MODAL);
            editRawStage.initOwner(stage);
            editRawStage.setResizable(false);
            root.getStylesheets().add("styles/style.css");
            editRawStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}

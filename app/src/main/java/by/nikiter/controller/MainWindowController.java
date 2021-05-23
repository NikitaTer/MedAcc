package by.nikiter.controller;

import by.nikiter.model.entity.*;
import by.nikiter.model.Repo;
import by.nikiter.util.*;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.AccessibleRole;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class MainWindowController implements Initializable {

    private Stage stage;

    private final ProductGridMap productGrids = new ProductGridMap();

    @FXML
    private MenuItem closeMenuItem;

    @FXML
    private MenuItem rawsMenuItem;

    @FXML
    private TextField searchField;

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

        //product search set up
        FilteredList<Product> filteredList = new FilteredList<>(Repo.getInstance().getProducts(),p -> true);
        searchField.textProperty().addListener(((observable, oldValue, newValue) -> {
            filteredList.setPredicate(product -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                return product.getName().toLowerCase().contains(newValue.toLowerCase());
            });
        }));
        SortedList<Product> sortedList = new SortedList<>(filteredList);

        //product list set up
        prodListView.setItems(sortedList);
        prodListView.setCellFactory(tv -> {
            ListCell<Product> cell = new ListCell<Product>() {
                @Override
                protected void updateItem(Product item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item != null) {
                        setText(item.getName());
                    } else {
                        setText("");
                    }
                }
            };

            MenuItem edit = new MenuItem(PropManager.getLabel("main.list.product.context_menu.edit"));
            edit.setOnAction(event -> {
                if (!cell.isEmpty()) {
                    openEditProductWindow(cell.getItem());
                }
            });

            MenuItem delete = new MenuItem(PropManager.getLabel("main.list.product.context_menu.delete"));
            delete.setOnAction(event -> {
                if (!cell.isEmpty()) {
                    deleteProduct(cell.getItem());
                }
            });

            ContextMenu contextMenu = new ContextMenu(edit,delete);
            cell.setContextMenu(contextMenu);

            return cell;
        });

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

        rawsMenuItem.setOnAction(event -> openNewRawWindow());
    }

    public void setStage(Stage stage) {
        this.stage = stage;

        stage.setOnCloseRequest(e -> ControllersManager.getInstance().setMainWindowController(null));
    }

    private void buildGrid(Product product) {

        if (product == null) {
            if (mainHBox.getChildren().size() > 1 && mainHBox.getChildren().get(1) instanceof ProductGridPane) {
                mainHBox.getChildren().remove(1);
            }
            return;
        }

        if (productGrids.get(product) != null) {
            showGrid(productGrids.get(product));
            return;
        }

        ProductGridPane gridPane = new ProductGridPane();
        gridPane.setVgap(5);
        gridPane.setHgap(5);
        productGrids.add(product, gridPane);

        Label productNameLabel = new Label(product.getName());
        productNameLabel.setFont(Font.font(14));
        gridPane.setProductNameLabel(productNameLabel);
        Label productQuantityLabel = new Label(Double.toString(product.getQuantity()));
        productQuantityLabel.setFont(Font.font(14));
        gridPane.setProductQuantityLabel(productQuantityLabel);
        Label productUnitLabel = new Label(product.getUnit().getNameShort());
        productUnitLabel.setFont(Font.font(14));
        gridPane.setProductUnitLabel(productUnitLabel);
        HBox nameBox = new HBox(5, productNameLabel, productQuantityLabel, productUnitLabel);

        Node rawNode = buildRawNode(product);
        Node calcNode = buildCalcNode(product);
        Node packagingNode = buildPackagingNode(product);

        VBox vBox = new VBox(5, nameBox, rawNode);
        GridPane.setHgrow(vBox, Priority.ALWAYS);
        GridPane.setHgrow(packagingNode, Priority.ALWAYS);

        gridPane.add(vBox, 0, 0);
        gridPane.add(packagingNode, 0,1);
        gridPane.add(calcNode,1,0);
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

        TableView<Map.Entry<Raw, Double>> rawTable = new TableView<>(FXCollections.observableArrayList(product.getRaws().entrySet()));
        productGrids.get(product).setRawTable(rawTable);
        rawTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        rawTable.setMinSize(rawTable.getPrefWidth(),rawTable.getPrefHeight());

        ScrollPane scrollPane = new ScrollPane(rawTable);
        scrollPane.setFitToWidth(true);

        rawTable.setRowFactory(tv -> {
            TableRow<Map.Entry<Raw,Double>> row = new TableRow<>();

            MenuItem edit = new MenuItem(PropManager.getLabel("main.table.raw.context_menu.edit"));
            edit.setOnAction(event -> {
                if (!row.isEmpty()) {
                    openEditRawWindow(row.getItem());
                }
            });

            MenuItem delete = new MenuItem(PropManager.getLabel("main.table.raw.context_menu.delete"));
            delete.setOnAction(event -> {
                if (!row.isEmpty()) {
                    deleteRawFromProduct(product,row.getItem());
                }
            });

            ContextMenu contextMenu = new ContextMenu(edit,delete);

            row.setContextMenu(contextMenu);

            return row;
        });

        TableColumn<Map.Entry<Raw,Double>,String> nameColumn = new TableColumn<>(PropManager.getLabel("main.table.raw.name"));
        nameColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getKey().getName()));
        rawTable.getColumns().add(nameColumn);

        TableColumn<Map.Entry<Raw,Double>,Double> costColumn = new TableColumn<>(PropManager.getLabel("main.table.raw.cost"));
        costColumn.setCellValueFactory(param -> new SimpleDoubleProperty(param.getValue().getKey().getCost()).asObject());
        costColumn.setCellFactory(CellFormatter.getRawDoubleFormat());
        rawTable.getColumns().add(costColumn);

        TableColumn<Map.Entry<Raw,Double>,Double> quantityColumn = new TableColumn<>(PropManager.getLabel("main.table.raw.quantity"));
        quantityColumn.setCellValueFactory(param -> new SimpleDoubleProperty(param.getValue().getValue()).asObject());
        rawTable.getColumns().add(quantityColumn);

        TableColumn<Map.Entry<Raw,Double>,Unit> unitColumn = new TableColumn<>(PropManager.getLabel("main.table.raw.unit"));
        unitColumn.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue().getKey().getUnit()));
        rawTable.getColumns().add(unitColumn);

        return new VBox(5, new HBox(20,tableNameLabel,addRawLabel),scrollPane);
    }

    private Node buildCalcNode(Product product) {

        //build calc grid
        Label rawsCostLabel = new Label(PropManager.getLabel("main.calc.raws.cost"));
        rawsCostLabel.setTextAlignment(TextAlignment.RIGHT);
        GridPane.setHalignment(rawsCostLabel, HPos.RIGHT);
        Label totalCostLabel = new Label(PropManager.getLabel("main.calc.total.cost"));
        totalCostLabel.setTextAlignment(TextAlignment.RIGHT);
        GridPane.setHalignment(totalCostLabel, HPos.RIGHT);
        Label profitPercentLabel = new Label(PropManager.getLabel("main.calc.profit.percent"));
        profitPercentLabel.setTextAlignment(TextAlignment.RIGHT);
        GridPane.setHalignment(profitPercentLabel, HPos.RIGHT);

        TextField rawsCostField = new TextField();
        rawsCostField.setEditable(false);
        rawsCostField.setPrefWidth(60);
        TextField totalCostField = new TextField();
        totalCostField.setEditable(false);
        totalCostField.setPrefWidth(60);
        TextField profitPercentField = new TextField();
        profitPercentField.setPrefWidth(60);

        productGrids.get(product).setRawsCostField(rawsCostField);
        productGrids.get(product).setTotalCostField(totalCostField);
        productGrids.get(product).setProfitPercentField(profitPercentField);

        CheckBox profitPercentBox = new CheckBox();

        productGrids.get(product).setProfitPercentBox(profitPercentBox);

        rawsCostField.setText(String.format(Locale.US,"%.2f",CalcUtil.calcRawsCost(product)));
        totalCostField.setText(String.format(Locale.US,"%.2f",CalcUtil.calcProductCost(product)));
        profitPercentField.setText(String.format(Locale.US,"%.2f",CalcUtil.calcProfitPercent(product)));

        GridPane calcGrid = new GridPane();
        calcGrid.setHgap(5);
        calcGrid.setVgap(5);
        calcGrid.setAlignment(Pos.CENTER_RIGHT);

        calcGrid.addRow(0,rawsCostLabel,rawsCostField);
        calcGrid.addRow(1,totalCostLabel,totalCostField);
        calcGrid.addRow(2,profitPercentLabel,profitPercentField,profitPercentBox);

        //build salary box
        Label salaryLabel = new Label(PropManager.getLabel("main.salary"));
        salaryLabel.setTextAlignment(TextAlignment.RIGHT);
        GridPane.setHalignment(salaryLabel,HPos.RIGHT);
        TextField salaryField = new TextField();
        productGrids.get(product).setSalaryField(salaryField);
        salaryField.setPrefWidth(60.0);
        salaryField.setText(String.format(Locale.US,"%.2f",product.getSalary()));
        calcGrid.addRow(4,salaryLabel,salaryField);

        //build calc button
        Button calcButton = new Button(PropManager.getLabel("main.calc_button"));

        Label errorLabel = new Label("");
        errorLabel.setTextFill(Paint.valueOf("RED"));
        errorLabel.setVisible(false);
        errorLabel.setTextAlignment(TextAlignment.CENTER);

        calcButton.setOnAction(event -> {
            if (isFieldsValid(product,errorLabel)) {
                updateCurrentGrid(true);
            }
        });

        VBox calcButtonBox = new VBox(5, calcButton, errorLabel);
        calcButtonBox.setAlignment(Pos.CENTER);

        VBox vBox = new VBox(5,calcGrid,calcButtonBox);
        vBox.setAlignment(Pos.CENTER);
        vBox.setMinWidth(190);

        return vBox;
    }

    private Node buildPackagingNode(Product product) {

        Label tableNameLabel = new Label(PropManager.getLabel("main.table.packaging.name"));

        Label addPackagingWindow = new Label(PropManager.getLabel("main.table.packaging.add"));
        addPackagingWindow.setAccessibleRole(AccessibleRole.BUTTON);
        addPackagingWindow.setUnderline(true);
        addPackagingWindow.setOnMouseClicked(event -> {
            openAddPackagingWindow();
        });

        TableView<PackagingUnit> packagingTable = new TableView<>(FXCollections.observableArrayList(product.getPackagingUnits()));
        productGrids.get(product).setPackagingTable(packagingTable);
        packagingTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        packagingTable.setMinSize(packagingTable.getPrefWidth(),packagingTable.getPrefHeight());

        ScrollPane scrollPane = new ScrollPane(packagingTable);

        packagingTable.setRowFactory(param -> {
            TableRow<PackagingUnit> row = new TableRow<>();

            MenuItem edit = new MenuItem(PropManager.getLabel("main.table.packaging.menu.edit"));
            edit.setOnAction(event -> {
                if (!row.isEmpty()) {
                    openEditPackagingWindow(row.getItem());
                }
            });

            MenuItem delete = new MenuItem(PropManager.getLabel("main.table.packaging.menu.delete"));
            delete.setOnAction(event -> {
                if (!row.isEmpty()) {
                    deletePackagingFromProduct(product, row.getItem());
                }
            });

            row.setContextMenu(new ContextMenu(edit,delete));

            return row;
        });

        //name column
        TableColumn<PackagingUnit, String> nameColumn = new TableColumn<>(PropManager.getLabel("main.table.packaging.column.name"));
        nameColumn.setResizable(false);
        nameColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getName()));
        packagingTable.getColumns().add(nameColumn);

        //product quantity column
        TableColumn<PackagingUnit, Double> productQuantityColumn = new TableColumn<>(PropManager.getLabel("main.table.packaging.column.product_quantity"));
        productQuantityColumn.setResizable(false);
        productQuantityColumn.setCellValueFactory(param -> new SimpleDoubleProperty(param.getValue().getProductQuantity()).asObject());
        packagingTable.getColumns().add(productQuantityColumn);

        //unit column
        TableColumn<PackagingUnit, Unit> unitColumn = new TableColumn<>(PropManager.getLabel("main.table.packaging.column.unit"));
        unitColumn.setResizable(false);
        unitColumn.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue().getUnit()));
        packagingTable.getColumns().add(unitColumn);

        //quantity in box column
        TableColumn<PackagingUnit, Integer> quantityInBoxColumn = new TableColumn<>(PropManager.getLabel("main.table.packaging.column.quantity_in_box"));
        quantityInBoxColumn.setResizable(false);
        quantityInBoxColumn.setCellValueFactory(param -> new SimpleIntegerProperty(param.getValue().getQuantityInBox()).asObject());
        packagingTable.getColumns().add(quantityInBoxColumn);

        //additional expenses by piece column
        TableColumn<PackagingUnit, Double> addExpPieceColumn = new TableColumn<>(PropManager.getLabel("main.table.packaging.column.add_exp_piece"));
        addExpPieceColumn.setResizable(false);
        addExpPieceColumn.setCellValueFactory(param -> new SimpleDoubleProperty(param.getValue().getAddExpPiece()).asObject());
        addExpPieceColumn.setCellFactory(CellFormatter.getPackagingDoubleFormat());
        packagingTable.getColumns().add(addExpPieceColumn);

        //additional expenses by set column
        TableColumn<PackagingUnit, Double> addExpSetColumn = new TableColumn<>(PropManager.getLabel("main.table.packaging.column.add_exp_set"));
        addExpSetColumn.setResizable(false);
        addExpSetColumn.setCellValueFactory(param -> new SimpleDoubleProperty(param.getValue().getAddExpSet()).asObject());
        addExpSetColumn.setCellFactory(CellFormatter.getPackagingDoubleFormat());
        packagingTable.getColumns().add(addExpSetColumn);

        //piece cost column
        TableColumn<PackagingUnit, Double> pieceCostColumn = new TableColumn<>(PropManager.getLabel("main.table.packaging.column.piece_cost"));
        pieceCostColumn.setResizable(false);
        pieceCostColumn.setCellValueFactory(param -> new SimpleDoubleProperty(param.getValue().getPieceCost()).asObject());
        pieceCostColumn.setCellFactory(CellFormatter.getPackagingDoubleFormat());
        packagingTable.getColumns().add(pieceCostColumn);

        //set cost column
        TableColumn<PackagingUnit, Double> setCostColumn = new TableColumn<>(PropManager.getLabel("main.table.packaging.column.set_cost"));
        setCostColumn.setResizable(false);
        setCostColumn.setCellValueFactory(param -> new SimpleDoubleProperty(param.getValue().getSetCost()).asObject());
        setCostColumn.setCellFactory(CellFormatter.getPackagingDoubleFormat());
        packagingTable.getColumns().add(setCostColumn);

        //piece price column
        TableColumn<PackagingUnit, Double> piecePriceColumn = new TableColumn<>(PropManager.getLabel("main.table.packaging.column.piece_price"));
        piecePriceColumn.setResizable(false);
        piecePriceColumn.setCellValueFactory(param -> new SimpleDoubleProperty(param.getValue().getPiecePrice()).asObject());
        piecePriceColumn.setCellFactory(CellFormatter.getPackagingDoubleFormat());
        packagingTable.getColumns().add(piecePriceColumn);

        //set price column
        TableColumn<PackagingUnit, Double> setPriceColumn = new TableColumn<>(PropManager.getLabel("main.table.packaging.column.set_price"));
        setPriceColumn.setResizable(false);
        setPriceColumn.setCellValueFactory(param -> new SimpleDoubleProperty(param.getValue().getSetPrice()).asObject());
        setPriceColumn.setCellFactory(CellFormatter.getPackagingDoubleFormat());
        packagingTable.getColumns().add(setPriceColumn);

        return new VBox(5, new HBox(20, tableNameLabel,addPackagingWindow), scrollPane);
    }

    private void showGrid(ProductGridPane gridPane) {
        if (mainHBox.getChildren().size() > 1 && mainHBox.getChildren().get(1) instanceof ProductGridPane) {
            mainHBox.getChildren().remove(1);
        }
        mainHBox.getChildren().add(1,gridPane);
    }

    public void updateCurrentGrid(boolean updateSalary) {
        updateCurrentGrid(updateSalary,null);
    }

    public void updateCurrentGrid(boolean updateSalary, PackagingUnit pack) {
        Product product = Repo.getInstance().getCurrentProduct();
        ProductGridPane gridPane = productGrids.get(product);

        prodListView.refresh();

        gridPane.getProductNameLabel().setText(product.getName());
        gridPane.getProductQuantityLabel().setText(String.valueOf(product.getQuantity()));
        gridPane.getProductUnitLabel().setText(product.getUnit().getNameShort());

        gridPane.getRawTable().setItems(FXCollections.observableArrayList(product.getRaws().entrySet()));
        gridPane.getRawTable().refresh();

        if (updateSalary) {
            product.setSalary(Double.parseDouble(gridPane.getSalaryField().getText()));
            gridPane.getSalaryField().setText(String.format(Locale.US,"%.2f", product.getSalary()));
        }

        gridPane.getRawsCostField().setText(String.format(Locale.US,"%.2f",CalcUtil.calcRawsCost(product)));
        gridPane.getTotalCostField().setText(String.format(Locale.US,"%.2f",CalcUtil.calcProductCost(product)));

        CalcUtil.calcPackagingsCosts(product);
        if (!gridPane.getProfitPercentBox().isSelected()) {
            if (pack == null) {
                gridPane.getProfitPercentField().setText(String.format(Locale.US, "%.2f", CalcUtil.calcProfitPercent(product)));
            } else {
                gridPane.getProfitPercentField().setText(String.format(Locale.US, "%.2f", CalcUtil.calcProfitPercent(pack)));
            }
        }
        CalcUtil.calcPackagingsPricesByPercent(product, Double.parseDouble(gridPane.getProfitPercentField().getText()));
        JsonFileUtil.saveAllProducts();

        gridPane.getPackagingTable().setItems(FXCollections.observableArrayList(product.getPackagingUnits()));
        gridPane.getPackagingTable().refresh();
    }

    private void openAddProductWindow() {
        Stage addProductStage = new Stage();
        addProductStage.setTitle(PropManager.getLabel("add_pd.name"));
        addProductStage.getIcons().add(new Image("images/logo.png"));

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

    private void openAddRawWindow() {
        Stage addRawStage = new Stage();
        addRawStage.setTitle(PropManager.getLabel("add_raw.name"));
        addRawStage.getIcons().add(new Image("images/logo.png"));

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

    private void openAddPackagingWindow() {
        Stage addPackagingStage = new Stage();
        addPackagingStage.setTitle(PropManager.getLabel("add_pack.title"));
        addPackagingStage.getIcons().add(new Image("images/logo.png"));

        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/view/AddPackagingWindow.fxml"),
                ResourceBundle.getBundle("labels")
        );

        try {
            Parent root = loader.load();
            ((AddPackagingWindowController)loader.getController()).setStage(addPackagingStage);

            addPackagingStage.setScene(new Scene(root));
            addPackagingStage.initModality(Modality.WINDOW_MODAL);
            addPackagingStage.initOwner(stage);
            addPackagingStage.setResizable(false);
            root.getStylesheets().add("styles/style.css");
            addPackagingStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void openNewRawWindow() {
        Stage newRawStage = new Stage();
        newRawStage.setTitle(PropManager.getLabel("new_raw.title"));
        newRawStage.getIcons().add(new Image("images/logo.png"));

        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/view/NewRawWindow.fxml"),
                ResourceBundle.getBundle("labels")
        );

        try {
            Parent root = loader.load();
            ((NewRawWindowController)loader.getController()).setStage(newRawStage);

            newRawStage.setScene(new Scene(root));
            newRawStage.initModality(Modality.WINDOW_MODAL);
            newRawStage.initOwner(stage);
            newRawStage.setResizable(false);
            root.getStylesheets().add("styles/style.css");
            newRawStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void openEditRawWindow(Map.Entry<Raw,Double> raw) {
        Stage editRawStage = new Stage();
        editRawStage.setTitle(PropManager.getLabel("edit_raw.title"));
        editRawStage.getIcons().add(new Image("images/logo.png"));

        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/view/EditRawWindow.fxml"),
                ResourceBundle.getBundle("labels")
        );

        try {
            Parent root = loader.load();
            ((EditRawWindowController)loader.getController()).setStage(editRawStage);
            ((EditRawWindowController)loader.getController()).setProdRaw(Repo.getInstance().getCurrentProduct(),raw);

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

    private void openEditProductWindow(Product product) {
        Stage editProductStage = new Stage();
        editProductStage.setTitle(PropManager.getLabel("edit_prod.title"));
        editProductStage.getIcons().add(new Image("images/logo.png"));

        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/view/EditProductWindow.fxml"),
                ResourceBundle.getBundle("labels")
        );

        try {
            Parent root = loader.load();
            ((EditProductWindowController)loader.getController()).setStage(editProductStage);
            ((EditProductWindowController)loader.getController()).setProduct(product);

            editProductStage.setScene(new Scene(root));
            editProductStage.initModality(Modality.WINDOW_MODAL);
            editProductStage.initOwner(stage);
            editProductStage.setResizable(false);
            root.getStylesheets().add("styles/style.css");
            editProductStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void openEditPackagingWindow(PackagingUnit packaging) {
        Stage editPackagingStage = new Stage();
        editPackagingStage.setTitle(PropManager.getLabel("edit_pack.title"));
        editPackagingStage.getIcons().add(new Image("images/logo.png"));

        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/view/EditPackagingWindow.fxml"),
                ResourceBundle.getBundle("labels")
        );

        try {
            Parent root = loader.load();
            ((EditPackagingWindowController)loader.getController()).setStage(editPackagingStage);
            ((EditPackagingWindowController)loader.getController()).setPackaging(packaging);

            editPackagingStage.setScene(new Scene(root));
            editPackagingStage.initModality(Modality.WINDOW_MODAL);
            editPackagingStage.initOwner(stage);
            editPackagingStage.setResizable(false);
            root.getStylesheets().add("styles/style.css");
            editPackagingStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void deleteProduct(Product product) {
        if (product == null) {
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(PropManager.getLabel("main.alert.delete.product.title"));
        alert.setHeaderText(PropManager.getLabel("main.alert.delete.product.body"));
        alert.setContentText(null);

        ButtonType yesButton = new ButtonType(PropManager.getLabel("main.alert.delete.product.yes"), ButtonBar.ButtonData.YES);
        ButtonType noButton = new ButtonType(PropManager.getLabel("main.alert.delete.product.no"), ButtonBar.ButtonData.NO);

        alert.getButtonTypes().setAll(yesButton,noButton);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == yesButton) {
            Repo.getInstance().deleteProduct(product);
            productGrids.remove(product);
        }
    }

    private void deleteRawFromProduct(Product product, Map.Entry<Raw,Double> raw) {
        if (product == null || raw == null) {
            return;
        }

        Repo.getInstance().deleteRawFromProduct(product,raw.getKey());
        updateCurrentGrid(false);
    }

    private void deletePackagingFromProduct(Product product, PackagingUnit packaging) {
        Repo.getInstance().deletePackagingFromProduct(product, packaging);
        updateCurrentGrid(false);
    }

    //shit-code
    private boolean isFieldsValid(Product product, Label errorLabel) {
        ProductGridPane gridPane = productGrids.get(product);
        boolean isValid = true;

        errorLabel.setVisible(false);
        gridPane.getSalaryField().getStyleClass().remove("error");
        gridPane.getProfitPercentField().getStyleClass().remove("error");

        String salary = gridPane.getSalaryField().getText().trim();
        String profitPercent = gridPane.getProfitPercentField().getText().trim();

        if (!salary.matches(Regexp.DOUBLE)) {
            gridPane.getSalaryField().getStyleClass().add("error");
            isValid = false;
        } else if (!salary.contains(".")) {
            gridPane.getSalaryField().setText(salary + ".0");
        }

        if (!profitPercent.contains(".")) {
            gridPane.getProfitPercentField().setText(profitPercent + ".0");
        }

        if (gridPane.getProfitPercentBox().isSelected() && !profitPercent.matches(Regexp.DOUBLE)) {
            gridPane.getProfitPercentField().getStyleClass().add("error");
            isValid = false;
        }

        if (!isValid) {
            errorLabel.setText(PropManager.getLabel("main.error.wrong_format"));
            errorLabel.setVisible(true);
        }
        return isValid;
    }
}

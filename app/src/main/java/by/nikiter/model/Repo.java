package by.nikiter.model;

import by.nikiter.model.entity.Product;
import by.nikiter.model.entity.Raw;
import by.nikiter.util.JsonFileUtil;
import by.nikiter.util.PropManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;

import java.util.Optional;

public class Repo {

    private final ObservableList<Product> products;
    private final ObservableList<Raw> raws;
    private Product currentProduct = null;

    private static volatile Repo instance = null;

    private Repo() {
        raws = FXCollections.observableList(JsonFileUtil.getAllRaws());
        products = FXCollections.observableList(JsonFileUtil.getAllProducts(raws));
        if (products.size() > 0) {
            currentProduct = products.get(0);
        }
    }

    public static Repo getInstance() {
        if (instance != null) {
            return instance;
        }

        synchronized (Repo.class) {
            if (instance == null) {
                instance = new Repo();
            }

            return instance;
        }
    }

    public ObservableList<Product> getProducts() {
        return products;
    }

    public ObservableList<Raw> getRaws() {
        return raws;
    }

    public Raw getRawByName(String rawName) {
        for (Raw raw : raws) {
            if (raw.getName().equals(rawName)) {
                return raw;
            }
        }
        return null;
    }

    public void addProduct(Product product) {
        products.add(product);
        JsonFileUtil.saveAllProducts();
    }

    public void addRaw(Raw raw) {
        raws.add(raw);
        JsonFileUtil.saveAllRaws();
    }

    public void deleteProduct(Product product) {
        products.remove(product);
        JsonFileUtil.saveAllProducts();
        if (products.size() > 0) {
            currentProduct = products.get(0);
        }
    }

    public void setCurrentProduct(Product product) {
        currentProduct = product;
    }

    public Product getCurrentProduct() {
        return currentProduct;
    }

    public void addRawToCurrent(Raw raw, int cost) {
        currentProduct.addRaw(raw, cost);
        JsonFileUtil.saveAllProducts();

    }

    public void deleteRaw(Raw raw) {
        if (!raws.contains(raw)) {
            return;
        }

        Alert alert= new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(PropManager.getLabel("main.alert.delete.raw.title"));
        alert.setHeaderText(PropManager.getLabel("main.alert.delete.raw.body"));
        alert.setContentText(null);

        ButtonType yesButton = new ButtonType(PropManager.getLabel("main.alert.delete.raw.yes"), ButtonBar.ButtonData.YES);
        ButtonType noButton = new ButtonType(PropManager.getLabel("main.alert.delete.raw.no"), ButtonBar.ButtonData.NO);

        alert.getButtonTypes().setAll(yesButton,noButton);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == yesButton) {
            raws.remove(raw);
            products.forEach(product -> product.deleteRaw(raw));
            JsonFileUtil.saveAll();
        }
    }

    public void deleteRawFromProduct(Product product, Raw raw) {
        product.deleteRaw(raw);
        JsonFileUtil.saveAllProducts();
    }
}

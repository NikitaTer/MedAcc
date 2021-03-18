package by.nikiter.model;

import by.nikiter.model.entity.Employee;
import by.nikiter.model.entity.Product;
import by.nikiter.model.entity.Raw;
import by.nikiter.util.JsonFileUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Repo {

    private final ObservableList<Product> products;
    private Product currentProduct;

    private static volatile Repo instance = null;

    private Repo() {
        products = FXCollections.observableList(JsonFileUtil.getAllProducts());
        currentProduct = products.get(0);
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

    public void addProduct(Product product) {
        products.add(product);
        JsonFileUtil.saveAllProducts();
    }

    public void setCurrentProduct(Product product) {
        currentProduct = product;
    }

    public Product getCurrentProduct() {
        return currentProduct;
    }

    public void addRawToCurrent(Raw raw) {
        currentProduct.addRaw(raw);
        JsonFileUtil.saveAllProducts();
    }

    public void addEmployeeToCurrent(Employee employee) {
        currentProduct.addEmployee(employee);
        JsonFileUtil.saveAllProducts();
    }
}

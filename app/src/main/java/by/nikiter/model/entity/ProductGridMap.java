package by.nikiter.model.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ProductGridMap {
    private final List<Product> products = new ArrayList<>();
    private final List<ProductGridPane> gridPanes = new ArrayList<>();

    public ProductGridMap() {
    }

    public void add(Product product, ProductGridPane gridPane) {
        if (!products.contains(product)) {
            products.add(product);
            gridPanes.add(gridPane);
        }
    }

    public void remove(Product product) {
        for (int i = 0; i < products.size(); i++) {
            if (products.get(i) == product) {
                products.remove(i);
                gridPanes.remove(i);
                break;
            }
        }
    }

    public ProductGridPane get(Product product) {
        for (int i = 0; i < products.size(); i++) {
            if (products.get(i) == product) {
                return gridPanes.get(i);
            }
        }
        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductGridMap that = (ProductGridMap) o;
        return Objects.equals(products, that.products) &&
                Objects.equals(gridPanes, that.gridPanes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(products, gridPanes);
    }
}

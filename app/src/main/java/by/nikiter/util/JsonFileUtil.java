package by.nikiter.util;

import by.nikiter.model.Currency;
import by.nikiter.model.Repo;
import by.nikiter.model.Unit;
import by.nikiter.model.entity.Product;
import by.nikiter.model.entity.Raw;
import com.google.gson.Gson;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class JsonFileUtil {

    private static final Gson gson = new Gson();

    private static final String PRODUCT_FILE = "src\\main\\resources\\files\\Products";

    public static void main(String[] args) {
        Product product = new Product("Первый продукт", Unit.PIECE, 100);
        product.addRaw(new Raw("Первое сырьё",21.0,3,Unit.LITER));
        product.addRaw(new Raw("Второе сырьё",26.33,255,Unit.GRAM));
        product.addRaw(new Raw("Третье сырьё",27.33,6,Unit.KILOGRAM));

        Product product1 = new Product("Второй продукт", Unit.KILOGRAM, 25);


        try (BufferedWriter writer = new BufferedWriter(new FileWriter(PRODUCT_FILE))) {
            writer.write(gson.toJson(product));
            writer.newLine();
            writer.write(gson.toJson(product1));
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void saveAllProducts() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(PRODUCT_FILE))) {
            for (Product product : Repo.getInstance().getProducts()) {
                writer.write(gson.toJson(product));
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<Product> getAllProducts() {
        List<Product> products = new ArrayList<>();

        try (Stream<String> lines = Files.lines(new File(PRODUCT_FILE).toPath())) {
            lines.forEach(s -> products.add(gson.fromJson(s,Product.class)));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return products;
    }
}

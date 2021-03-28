package by.nikiter.util;

import by.nikiter.model.Repo;
import by.nikiter.model.entity.Product;
import com.google.gson.Gson;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class JsonFileUtil {

    private static final Gson gson = new Gson();

    private static final String PRODUCT_FILE = "Products";

    public static void saveAllProducts() {
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(PRODUCT_FILE),StandardCharsets.UTF_8)
        )) {
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

        try (Stream<String> lines = Files.lines(new File(PRODUCT_FILE).toPath(), StandardCharsets.UTF_8)) {
            lines.forEach(s -> products.add(gson.fromJson(s,Product.class)));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return products;
    }
}

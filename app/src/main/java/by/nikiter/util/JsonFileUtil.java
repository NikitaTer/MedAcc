package by.nikiter.util;

import by.nikiter.model.Repo;
import by.nikiter.model.entity.Product;
import by.nikiter.model.entity.Raw;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.collections.ObservableList;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.*;
import java.util.stream.Stream;

public class JsonFileUtil {

    private static final Gson gson = new Gson();

    private static final String PRODUCT_FILE = "Products";
    private static final String RAW_FILE = "Raws";
    private static final String PRODUCT_RAW_FILE = "product_raw";

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

        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(PRODUCT_RAW_FILE),StandardCharsets.UTF_8)
        )) {
            for (Product product : Repo.getInstance().getProducts()) {
                Map<String,Integer> raws = new HashMap<>();
                for (Map.Entry<Raw,Integer> raw : product.getRaws().entrySet())  {
                    raws.put(raw.getKey().getName(),raw.getValue());
                }
                writer.write(product.getName() + " : " + gson.toJson(raws));
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void saveAllRaws() {
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(RAW_FILE),StandardCharsets.UTF_8)
        )) {
            for (Raw raw : Repo.getInstance().getRaws()) {
                writer.write(gson.toJson(raw));
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void saveAll() {
        saveAllRaws();
        saveAllProducts();
    }

    public static List<Product> getAllProducts(ObservableList<Raw> raws) {
        List<Product> products = new ArrayList<>();

        File productFile = new File(PRODUCT_FILE);
        File productRawFile = new File(PRODUCT_RAW_FILE);

        try {
            if (!productFile.exists()) {
                productFile.createNewFile();
            }
            if (!productRawFile.exists()) {
                productRawFile.createNewFile();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (Stream<String> lines = Files.lines(productFile.toPath(), StandardCharsets.UTF_8)) {
            lines.forEach(s -> products.add(gson.fromJson(s,Product.class)));
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (Stream<String> lines = Files.lines(productRawFile.toPath(), StandardCharsets.UTF_8)) {
            lines.forEach(s -> {
                String[] nameRaws = s.split(" : ");
                String productName = nameRaws[0];
                String rawJson = null;
                if (nameRaws.length == 2) {
                    rawJson = nameRaws[1];
                }

                Map<String, Integer> rawNameCost = gson.fromJson(
                        rawJson,
                        new TypeToken<HashMap<String,Integer>>(){}.getType()
                );

                Map<Raw, Integer> rawsCost = new HashMap<>();
                if (rawNameCost != null) {
                    for (Map.Entry<String, Integer> rc : rawNameCost.entrySet()) {

                        Raw raw = null;
                        for (Raw r : raws) {
                            if (r.getName().equals(rc.getKey())) {
                                raw = r;
                            }
                        }

                        if (raw != null) {
                            rawsCost.put(raw,rc.getValue());
                        }
                    }
                }

                for (Product p : products) {
                    if (p.getName().equals(productName)) {
                        p.setRaws(rawsCost);
                    }
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        return products;
    }

    public static List<Raw> getAllRaws() {
        List<Raw> raws = new ArrayList<>();

        File rawFile = new File(RAW_FILE);

        try {
            if (!rawFile.exists()) {
                rawFile.createNewFile();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (Stream<String> lines = Files.lines(rawFile.toPath(), StandardCharsets.UTF_8)) {
            lines.forEach(s -> raws.add(gson.fromJson(s, Raw.class)));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return raws;
    }
}

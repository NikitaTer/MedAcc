package by.nikiter.util;

import by.nikiter.model.entity.Product;
import by.nikiter.model.entity.Raw;

public class CalcUtil {

    public static double calcRawsCost(Product product) {
        double rawsCost = 0.0;
        for (Raw raw : product.getRaws()) {
            rawsCost = rawsCost + (raw.getCost() * raw.getQuantity());
        }
        return rawsCost;
    }

    public static double calcTotalCost(Product product) {
        double rawsCost = 0.0;
        for (Raw raw : product.getRaws()) {
            rawsCost = rawsCost + (raw.getCost() * raw.getQuantity());
        }
        return rawsCost + product.getSalary();
    }

    public static double calcProfitNumber(Product product) {
        return product.getCost() - calcTotalCost(product);
    }

    public static double calcProfitPercent(Product product) {
        return calcProfitNumber(product) / calcTotalCost(product) * 100;
    }

    public static double calcCostByProfitNumber(Product product, double profit) {
        return profit + calcTotalCost(product);
    }

    public static double calcCostByProfitPercent(Product product, double profit) {
        return (profit / 100 * calcTotalCost(product)) + calcTotalCost(product);
    }
}

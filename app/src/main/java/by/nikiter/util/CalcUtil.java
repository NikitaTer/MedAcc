package by.nikiter.util;

import by.nikiter.model.entity.PackagingUnit;
import by.nikiter.model.entity.Product;
import by.nikiter.model.entity.Raw;

import java.util.Map;

public class CalcUtil {

    public static double calcRawsCost(Product product) {
        double rawsCost = 0.0;
        for (Map.Entry<Raw, Double> raw : product.getRaws().entrySet()) {
            rawsCost = rawsCost + (raw.getKey().getCost() * raw.getValue());
        }
        return rawsCost;
    }

    public static double calcProductCost(Product product) {
        double rawsCost = 0.0;
        for (Map.Entry<Raw, Double> raw : product.getRaws().entrySet()) {
            rawsCost = rawsCost + (raw.getKey().getCost() * raw.getValue());
        }
        return rawsCost + product.getSalary();
    }

    public static double calcProfitPercent(Product product) {
        if (product.getPackagingUnits().isEmpty()) {
            return 0.0;
        }

        PackagingUnit pack = product.getPackagingUnits().get(0);
        return (pack.getPiecePrice() - pack.getPieceCost()) * 100 / pack.getPieceCost();
    }

    public static double calcProfitPercent(PackagingUnit pack) {
        return (pack.getPiecePrice() - pack.getPieceCost()) * 100 / pack.getPieceCost();
    }

    public static void calcPackagingsCosts(Product product) {

        double productCost = calcProductCost(product);
        for (PackagingUnit pack : product.getPackagingUnits()) {
            double productQuantity = product.getQuantity();
            double quantityInPiece = pack.getProductQuantity();

            switch (product.getUnit()) {

                case MILLILITER:
                    switch (pack.getUnit()) {
                        case LITER:
                            quantityInPiece = quantityInPiece * 1000;
                            break;

                        default:
                            break;
                    }
                    break;

                case LITER:
                    switch (pack.getUnit()) {

                        case MILLILITER:
                            productQuantity = productQuantity * 1000;
                            break;

                        default:
                            break;
                    }
                    break;

                case MILLIGRAM:
                    switch (pack.getUnit()) {
                        case GRAM:
                            quantityInPiece = quantityInPiece * 1000;
                            break;

                        case KILOGRAM:
                            quantityInPiece = quantityInPiece * 1000000;
                            break;

                        default:
                            break;
                    }
                    break;

                case GRAM:
                    switch (pack.getUnit()) {
                        case MILLIGRAM:
                            productQuantity = productQuantity * 1000;
                            break;

                        case KILOGRAM:
                            quantityInPiece = quantityInPiece * 1000;
                            break;

                        default:
                            break;
                    }
                    break;

                case KILOGRAM:
                    switch (pack.getUnit()) {
                        case MILLIGRAM:
                            productQuantity = productQuantity * 1000000;
                            break;

                        case GRAM:
                            productQuantity = productQuantity * 1000;
                            break;

                        default:
                            break;
                    }
                    break;

                default:
                    break;
            }

            pack.setPieceCost(
                    (productCost / (productQuantity / quantityInPiece)) + pack.getAddExpPiece()
            );
            pack.setSetCost(
                    (pack.getPieceCost() * pack.getQuantityInBox()) + pack.getAddExpSet()
            );
        }
    }

    public static void calcPackagingsPricesByPercent(Product product, double percent) {

        for (PackagingUnit pack : product.getPackagingUnits()) {
            pack.setPiecePrice(
                    (percent / 100 * pack.getPieceCost()) + pack.getPieceCost()
            );
            pack.setSetPrice(
                    (percent / 100 * pack.getSetCost()) + pack.getSetCost()
            );
        }
    }
}

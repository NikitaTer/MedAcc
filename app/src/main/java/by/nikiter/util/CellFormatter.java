package by.nikiter.util;

import by.nikiter.model.entity.PackagingUnit;
import by.nikiter.model.entity.Raw;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;

import java.util.Locale;
import java.util.Map;

public class CellFormatter {

    public static Callback<TableColumn<Map.Entry<Raw,Double>, Double>, TableCell<Map.Entry<Raw,Double>, Double>> getRawDoubleFormat() {
        return param -> new TableCell<Map.Entry<Raw,Double>, Double>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                if (item != null) {
                    setText(String.format(Locale.US,"%.2f", item));
                }
            }
        };
    }

    public static Callback<TableColumn<PackagingUnit, Double>, TableCell<PackagingUnit, Double>> getPackagingDoubleFormat() {
        return param -> new TableCell<PackagingUnit, Double>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                if (item != null) {
                    setText(String.format(Locale.US,"%.2f", item));
                }
            }
        };

    }
}

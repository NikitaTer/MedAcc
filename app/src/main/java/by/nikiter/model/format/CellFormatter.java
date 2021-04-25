package by.nikiter.model.format;

import by.nikiter.model.entity.Raw;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;

import java.util.Locale;
import java.util.Map;

public class CellFormatter {

    public static Callback<TableColumn<Map.Entry<Raw,Integer>, Double>, TableCell<Map.Entry<Raw,Integer>, Double>> getRawCostFormat() {
        return param -> new TableCell<Map.Entry<Raw,Integer>, Double>() {

            @Override
            protected void updateItem(Double item, boolean empty) {
                if (item != null) {
                    setText(String.format(Locale.US,"%.2f", item));
                }
            }
        };
    }
}

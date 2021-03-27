package by.nikiter.model.format;

import by.nikiter.model.entity.Raw;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;

import java.util.Locale;

public class CellFormatter {

    public static Callback<TableColumn<Raw, Double>, TableCell<Raw, Double>> getRawCostFormat() {
        return new Callback<TableColumn<Raw, Double>, TableCell<Raw, Double>>() {
            @Override
            public TableCell<Raw, Double> call(TableColumn<Raw, Double> param) {
                TableCell<Raw, Double> cell = new TableCell<Raw, Double>() {

                    @Override
                    protected void updateItem(Double item, boolean empty) {
                        if (item != null) {
                            setText(String.format(Locale.US,"%.2f", item));
                        }
                    }
                };
                return cell;
            }
        };
    }
}

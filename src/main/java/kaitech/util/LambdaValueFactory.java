package kaitech.util;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;

import java.util.function.Function;

/**
 * Simple replacement for PropertyValueFactory, using a lambda to convert table rows to string data.
 * Maps from the row data (Supplier, Ingredient, etc) to any object. The object will have its {@link Object#toString()}
 * method called.
 *
 * @author Julia Harrison
 */
public class LambdaValueFactory<S> implements Callback<TableColumn.CellDataFeatures<S, String>, ObservableValue<String>> {

    private final Function<S, Object> func;

    public LambdaValueFactory(Function<S, Object> func) {
        this.func = func;
    }

    @Override
    public ObservableValue<String> call(TableColumn.CellDataFeatures<S, String> param) {
        S rowData = param.getValue();
        if (rowData == null) {
            return null;
        }
        return new ReadOnlyObjectWrapper<>(String.valueOf(func.apply(rowData)));
    }
}

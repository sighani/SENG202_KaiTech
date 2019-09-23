package kaitech.controller;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;

import java.util.function.Consumer;

/**
 * Simple class that allows a button with an action to be embedded in a TableColumn
 *
 * @param <S> The object type in the TableView
 */
public class ActionButtonTableCell_SalesController<S> extends TableCell<S, Button> {

    private final Button actionButton;

    private ActionButtonTableCell_SalesController(String label, Consumer<S> function) {
        getStyleClass().add("action-button-table-cell");

        actionButton = new Button(label);
        actionButton.setOnAction((ActionEvent e) -> function.accept(getCurrentItem()));
        actionButton.setMaxWidth(Double.MAX_VALUE);
    }

    /**
     * Create a TableColumn creator
     *
     * @param label    The static text to display in the button
     * @param function The function to run when the button is pressed
     * @param <S>      The object type in the TableView
     * @return A Callback that can be used as a CellFactory in a TableColumn
     */
    public static <S> Callback<TableColumn<S, Button>, TableCell<S, Button>> forTableColumn(String label, Consumer<S> function) {
        return ignored -> new ActionButtonTableCell_SalesController<>(label, function);
    }

    private S getCurrentItem() {
        return getTableView().getItems().get(getIndex());
    }

    @Override
    public void updateItem(Button item, boolean empty) {
        super.updateItem(item, empty);

        if (empty) {
            setGraphic(null);
        } else {
            setGraphic(actionButton);
        }
    }
}
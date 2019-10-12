package kaitech.controller;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import kaitech.api.database.MenuItemTable;
import kaitech.api.model.Business;
import kaitech.api.model.MenuItem;
import kaitech.model.BusinessImpl;
import kaitech.util.LambdaValueFactory;

import java.util.HashMap;
import java.util.Map;

public class AdjustItemsOrderedController {
    @FXML
    private TableView<MenuItem> table;

    @FXML
    private TableColumn<MenuItem, Button> addCol;

    @FXML
    private TableView<MenuItem> orderTable;

    @FXML
    private TableColumn<MenuItem, String> nameCol2;
    @FXML
    private TableColumn<MenuItem, Number> quantityCol;
    @FXML
    private TableColumn<MenuItem, Button> removeCol;

    @FXML
    private TableColumn<MenuItem, String> nameCol;

    @FXML
    private TableColumn<MenuItem, String> typeCol;

    @FXML
    private TableColumn<MenuItem, String> priceCol;

    @FXML
    private TableColumn<MenuItem, String> stockCol;

    @FXML
    private TableColumn<MenuItem, String> veganCol;

    @FXML
    private TableColumn<MenuItem, String> vegeCol;

    @FXML
    private TableColumn<MenuItem, String> gfCol;

    @FXML
    private TextField numItems;
    @FXML
    private Text responseText;

    private Business business;
    private MenuItemTable menuItemTable;
    private Map<MenuItem, Integer> newItemsOrdered = new HashMap<>();

    public void initialize() {
        business = BusinessImpl.getInstance();
        menuItemTable = business.getMenuItemTable();
    }

    /**
     * Sets up the items ordered to an empty HashMap ready to have values added to it.
     *
     * @param menuItems An empty HashMap.
     */
    public void setItemsOrdered(Map<MenuItem, Integer> menuItems) {

        newItemsOrdered = menuItems;

        nameCol2.setCellValueFactory(new LambdaValueFactory<>(MenuItem::getName));
        quantityCol.setCellValueFactory(cellData -> new SimpleIntegerProperty((newItemsOrdered.get(cellData.getValue()))));
        removeCol.setCellFactory(ActionButtonTableCell_SalesController.forTableColumn("X", foodItem -> {
            // When the remove button is pushed, remove the specified item from the order.
            if (newItemsOrdered.get(foodItem) == 1) {
                orderTable.getItems().remove(foodItem);
                newItemsOrdered.remove(foodItem);
                orderTable.refresh(); // Have to trigger a table refresh to make it show up in the table
            } else {
                newItemsOrdered.put(foodItem, newItemsOrdered.get(foodItem) - 1);
                orderTable.refresh();
            }
        }));
        orderTable.setItems(FXCollections.observableArrayList(newItemsOrdered.keySet()));

        nameCol.setCellValueFactory(new LambdaValueFactory<>(MenuItem::getName));
        typeCol.setCellValueFactory(new LambdaValueFactory<>(MenuItem::getType));
        priceCol.setCellValueFactory(new LambdaValueFactory<>(MenuItem::getPrice));
        stockCol.setCellValueFactory(cellData -> new SimpleStringProperty(Integer.toString(cellData.getValue().calculateNumServings(business))));
        veganCol.setCellValueFactory(new LambdaValueFactory<>(MenuItem::getIsVegan));
        vegeCol.setCellValueFactory(new LambdaValueFactory<>(MenuItem::getIsVeg));
        gfCol.setCellValueFactory(new LambdaValueFactory<>(MenuItem::getIsGF));
        addCol.setCellFactory(ActionButtonTableCell_SalesController.forTableColumn("Add", foodItem -> {
            // When the add button is pushed, add the specified item to the order.
            if (newItemsOrdered.containsKey(foodItem)) {
                newItemsOrdered.put(foodItem, newItemsOrdered.get(foodItem) + 1);
                orderTable.refresh();
            } else {
                newItemsOrdered.put(foodItem, 1);
            }
            orderTable.setItems(FXCollections.observableArrayList(newItemsOrdered.keySet()));
            orderTable.refresh();
        }));


        table.setItems(FXCollections.observableArrayList(menuItemTable.resolveAllMenuItems().values()));
    }


    /**
     * Closes the current screen.
     */

    public void closeAndSave() {
        Stage stage = (Stage) table.getScene().getWindow();
        stage.close();
    }
}

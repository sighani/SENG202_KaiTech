package kaitech.controller;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import kaitech.api.database.MenuItemTable;
import kaitech.api.model.Business;
import kaitech.api.model.Ingredient;
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
            // You can put whatever logic in here, or even open a new window.
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
            // You can put whatever logic in here, or even open a new window.
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
     * This method adds an item, and given quantity, to the HashMap, menuItems, which will later be set as the new
     * itemsOrdered value for the selected MenuItem, if it is not empty. Before adding the values, it first checks that
     * all fields are valid, and that an item has been selected from the TableView.
     */
   /* public void addItem() {
        if (fieldsAreValid()) {
            if (table.getSelectionModel().getSelectedItem() == null) {
                responseText.setText("You haven't selected a item.");
                responseText.setVisible(true);

            } else {
                MenuItem newItem;
                newItem = table.getSelectionModel().getSelectedItem();
                int numberItems = Integer.parseInt(numItems.getText());
                newItemsOrdered.put(newItem, numberItems);
                responseText.setText(numberItems + " of " + newItem.getName() + " added.");
                responseText.setVisible(true);
            }
        } else {
            responseText.setVisible(true);

        }
    }*/

    /**
     * Checks that all the fields are valid, and will not cause any exceptions.
     *
     * @return a boolean, true if fields are valid, false otherwise.
     */
    /*public boolean fieldsAreValid() {
        MenuItem newItem;
        newItem = table.getSelectionModel().getSelectedItem();
        if (numItems.getText().trim().length() == 0) {
            responseText.setText("The amount field is blank.");
            return false;
        }
        try {
            int numberItems = Integer.parseInt(numItems.getText());
        } catch (NumberFormatException e) {
            responseText.setText("Please enter an integer value for number of ingredients.");
            return false;
        }
        if (Integer.parseInt(numItems.getText()) < 0) {
            responseText.setText("Please enter a positive integer.");
            return false;

        }
        return true;

    }*/

    /**
     * Closes the current screen.
     */
    public void close() {
        newItemsOrdered.clear();
        Stage stage = (Stage) table.getScene().getWindow();
        stage.close();
    }

    public void closeAndSave() {
        Stage stage = (Stage) table.getScene().getWindow();
        stage.close();
    }
}

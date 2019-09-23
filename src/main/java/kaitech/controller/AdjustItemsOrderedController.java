package kaitech.controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
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

import java.util.Map;

public class AdjustItemsOrderedController {
    @FXML
    private TableView<MenuItem> table;

    @FXML
    private TableColumn<MenuItem, String> codeCol;

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
    private Map<MenuItem, Integer> itemsOrdered;

    public void initialize() {
        business = BusinessImpl.getInstance();
        menuItemTable = business.getMenuItemTable();
        codeCol.setCellValueFactory(new LambdaValueFactory<>(MenuItem::getCode));
        nameCol.setCellValueFactory(new LambdaValueFactory<>(MenuItem::getName));
        typeCol.setCellValueFactory(new LambdaValueFactory<>(MenuItem::getType));
        priceCol.setCellValueFactory(new LambdaValueFactory<>(MenuItem::getPrice));
        stockCol.setCellValueFactory(cellData -> new SimpleStringProperty(Integer.toString(cellData.getValue().calculateNumServings(business))));
        veganCol.setCellValueFactory(new LambdaValueFactory<>(MenuItem::getIsVegan));
        vegeCol.setCellValueFactory(new LambdaValueFactory<>(MenuItem::getIsVeg));
        gfCol.setCellValueFactory(new LambdaValueFactory<>(MenuItem::getIsGF));


        table.setItems(FXCollections.observableArrayList(menuItemTable.resolveAllMenuItems().values()));
    }

    /**
     * Sets up the items ordered to an empty HashMap ready to have values added to it.
     *
     * @param menuItems An empty HashMap.
     */
    public void setItemsOrdered(Map<MenuItem, Integer> menuItems) {
        itemsOrdered = menuItems;
    }

    /**
     * This method adds an item, and given quantity, to the HashMap, menuItems, which will later be set as the new
     * itemsOrdered value for the selected MenuItem, if it is not empty. Before adding the values, it first checks that
     * all fields are valid, and that an item has been selected from the TableView.
     */
    public void addItem() {
        if (fieldsAreValid()) {
            if (table.getSelectionModel().getSelectedItem() == null) {
                responseText.setText("You haven't selected a item.");
                responseText.setVisible(true);

            } else {
                MenuItem newItem;
                newItem = table.getSelectionModel().getSelectedItem();
                int numberItems = Integer.parseInt(numItems.getText());
                itemsOrdered.put(newItem, numberItems);
                responseText.setText(numberItems + " of " + newItem.getName() + " added.");
                responseText.setVisible(true);
            }
        } else {
            responseText.setVisible(true);

        }
    }

    /**
     * Checks that all the fields are valid, and will not cause any exceptions.
     *
     * @return a boolean, true if fields are valid, false otherwise.
     */
    public boolean fieldsAreValid() {
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

    }

    /**
     * Closes the current screen.
     */
    public void close() {
        Stage stage = (Stage) table.getScene().getWindow();
        stage.close();
    }
}

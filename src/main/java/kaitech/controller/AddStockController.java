package kaitech.controller;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import kaitech.api.database.InventoryTable;
import kaitech.api.model.Business;
import kaitech.api.model.Ingredient;
import kaitech.model.BusinessImpl;
import org.joda.money.format.MoneyFormatter;
import org.joda.money.format.MoneyFormatterBuilder;

public class AddStockController {
    @FXML
    private TableColumn<Ingredient, String> codeCol;

    @FXML
    private TableColumn<Ingredient, String> nameCol;

    @FXML
    private TableColumn<Ingredient, String> unitTypeCol;

    @FXML
    private TableColumn<Ingredient, String> costCol;

    @FXML
    private TableColumn<Ingredient, String> vegCol;

    @FXML
    private TableColumn<Ingredient, String> veganCol;

    @FXML
    private TableColumn<Ingredient, String> gfCol;

    @FXML
    private TableColumn<Ingredient, Number> quantityCol;

    @FXML
    private TableView<Ingredient> table;

    @FXML
    private Text ingredientText;
    @FXML
    private TextField numIngredientsText;
    @FXML
    private Text responseText;
    private Business business;
    private InventoryTable inventoryTable;
    private static final MoneyFormatter MONEY_FORMATTER = new MoneyFormatterBuilder().appendCurrencySymbolLocalized().appendAmountLocalized().toFormatter();


    public void initialize() {
        business = BusinessImpl.getInstance();
        inventoryTable = business.getInventoryTable();
        codeCol.setCellValueFactory(new PropertyValueFactory<>("code"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        unitTypeCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getUnit().toString()));
        costCol.setCellValueFactory(cellData -> new SimpleStringProperty(MONEY_FORMATTER.print(cellData.getValue().getPrice())));
        vegCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getIsVeg().toString()));
        veganCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getIsVegan().toString()));
        gfCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getIsGF().toString()));
        quantityCol.setCellValueFactory(cellData -> new SimpleIntegerProperty(inventoryTable.getOrAddQuantity(cellData.getValue())));

        table.setItems(FXCollections.observableArrayList(business.getIngredientTable().resolveAllIngredients().values()));
    }

    /**
     * Adds the given amount of stock, of the selected ingredient to the inventory of the business. Before adding,
     * this method checks that all the GUI fields are valid, and that an ingredient has been selected from the TableView.
     */
    public void addStock() {
        if (fieldsAreValid()) {
            if (table.getSelectionModel().getSelectedItem() == null) {
                responseText.setText("You haven't selected a item.");
                responseText.setVisible(true);

            } else {
                Ingredient newIngredient;
                newIngredient = table.getSelectionModel().getSelectedItem();
                int numIngredients = Integer.parseInt(numIngredientsText.getText());
                business.getInventoryTable().updateQuantity(newIngredient, numIngredients);
                responseText.setText(numIngredients + " of " + newIngredient.getName() + " added to stock.");
                responseText.setVisible(true);
            }
        } else {
            responseText.setVisible(true);

        }

    }

    /**
     * This method checks that all the GUI fields are valid.
     *
     * @return a boolean, true if all fields are valid, false otherwise.
     */
    public boolean fieldsAreValid() {
        if (numIngredientsText.getText().trim().length() == 0) {
            responseText.setText("The amount field is blank.");
            return false;
        }
        try {
            int numIngredients = Integer.parseInt(numIngredientsText.getText());
        } catch (NumberFormatException e) {
            responseText.setText("Please enter an integer value for number of ingredients.");
            return false;
        }
        if (Integer.parseInt(numIngredientsText.getText()) < 0) {
            responseText.setText("Please enter a positive integer.");
            return false;

        }
        return true;

    }

    public void close() {
        Stage stage = (Stage) ingredientText.getScene().getWindow();
        stage.close();
    }
}

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
import kaitech.api.database.IngredientTable;
import kaitech.api.database.InventoryTable;
import kaitech.api.model.Business;
import kaitech.api.model.Ingredient;
import kaitech.model.BusinessImpl;
import org.joda.money.format.MoneyFormatter;
import org.joda.money.format.MoneyFormatterBuilder;

import java.util.Map;

public class AddIngredientToRecipeController {
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
    private Text titleText;
    @FXML
    private Text responseText;
    private Map<Ingredient, Integer> newIngredients;
    private InventoryTable inventoryTable;
    private IngredientTable ingredientTable;
    private static final MoneyFormatter MONEY_FORMATTER = new MoneyFormatterBuilder() //
            .appendCurrencySymbolLocalized() //
            .appendAmountLocalized() //
            .toFormatter();

    public void initialize() {
        Business business = BusinessImpl.getInstance();
        inventoryTable = business.getInventoryTable();
        ingredientTable = business.getIngredientTable();
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
     * Sets up the list of ingredients that will be added to recipe.
     * @param ingredients a hashmap of <Ingredient, Integer> which contains the ingredients, and quantities to be added
     *                    to the recipe.
     */
    public void setRecipe(Map<Ingredient, Integer> ingredients) {
        newIngredients = ingredients;
    }

    /**
     * Sets a message, called if a new recipe is being added."
     */
    public void setNewMessage() {
        titleText.setText("Please select the ingredients, and quantities for the new recipe:");
        titleText.setVisible(true);
    }

    /**
     * Sets a message, called if a recipe is being modified.
     */
    public void setModifyMessage() {
        titleText.setText("Select new ingredients and quantities for the modified recipe:");
        titleText.setVisible(true);
    }

    /**
     * This method adds an ingredient, an the given quantity to the hashmap that will later be added to the recipe, before
     * it adds the given values, it first checks that the fields are valid, and that an ingredient has been selected.
     */
    public void addIngredient() {
        if(fieldsAreValid()) {
            if (table.getSelectionModel().getSelectedItem() == null) {
                responseText.setText("You haven't selected a item.");
                responseText.setVisible(true);

            } else {
                Ingredient newIngredient;
                newIngredient = table.getSelectionModel().getSelectedItem();
                int numIngredients = Integer.parseInt(numIngredientsText.getText());
                newIngredients.put(newIngredient, numIngredients);
                responseText.setText(numIngredients + " of " + newIngredient.getName() + " added.");
                responseText.setVisible(true);
            }
        } else {
            responseText.setVisible(true);
        }
    }

    /**
     * A method that checks the fields in the GUI screen are valid.
     * @return a boolean, true if all fields are valid, false otherwise.
     */
    public boolean fieldsAreValid() {
        Ingredient newIngredient;
        if (numIngredientsText.getText().trim().length() == 0) {
            responseText.setText("The amount field is blank.");
            return false;
        }
        try {
            Integer.parseInt(numIngredientsText.getText());
        } catch (NumberFormatException e) {
            responseText.setText("Please enter an integer value for number of ingredients.");
            return false;
        }
        if (Integer.parseInt(numIngredientsText.getText()) < 1) {
            responseText.setText("Please enter an amount greater than zero.");
            return false;
        }
        return true;

    }

    /**
     * Closes the current GUI screen.
     */
    public void close() {
        Stage stage = (Stage) ingredientText.getScene().getWindow();
        stage.close();
    }
}

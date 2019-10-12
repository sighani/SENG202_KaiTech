package kaitech.controller;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import kaitech.api.model.Business;
import kaitech.api.model.Ingredient;
import kaitech.model.BusinessImpl;
import kaitech.util.LambdaValueFactory;
import org.joda.money.format.MoneyFormatter;
import org.joda.money.format.MoneyFormatterBuilder;

import java.util.HashMap;
import java.util.Map;

public class AddIngredientToRecipeController {
    @FXML
    private TableColumn<Ingredient, String> codeCol;

    @FXML
    private TableColumn<Ingredient, String> nameCol;


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
    private TableView<Ingredient> orderTable;

    @FXML
    private TableColumn<Ingredient, String> nameCol2;

    @FXML
    private TableColumn<Ingredient, Button> removeCol;
    @FXML
    private TableColumn<Ingredient, Button> addButtonColumn;


    @FXML
    private TextField ingredientText;
    @FXML
    private TextField numIngredientsText;
    @FXML
    private Text titleText;
    @FXML
    private Text responseText;

    private Map<Ingredient, Integer> newIngredients;
    private static final MoneyFormatter MONEY_FORMATTER = new MoneyFormatterBuilder() //
            .appendCurrencySymbolLocalized() //
            .appendAmountLocalized() //
            .toFormatter();
    private Business business;

    public void initialize() {
        business = BusinessImpl.getInstance();
        newIngredients = new HashMap<>();
    }

    /**
     * Sets up the list of ingredients that will be added to recipe.
     *
     * @param ingredients a hashmap of Ingredient to Integer which contains the ingredients, and quantities to be added
     *                    to the recipe.
     */
    public void setRecipe(Map<Ingredient, Integer> ingredients) {
        newIngredients = ingredients;
        nameCol2.setCellValueFactory(new LambdaValueFactory<>(Ingredient::getName));
        quantityCol.setCellValueFactory(cellData -> new SimpleIntegerProperty((newIngredients.get(cellData.getValue()))));
        removeCol.setCellFactory(ActionButtonTableCell_SalesController.forTableColumn("X", foodItem -> {
            // First check that fields are valid.
            if (fieldsAreValid()) {
                //If they are remove the specified quantity and ingredient.
                if (newIngredients.get(foodItem) <= Integer.parseInt(numIngredientsText.getText())) {
                    orderTable.getItems().remove(foodItem);
                    newIngredients.remove(foodItem);
                    orderTable.refresh(); // Have to trigger a table refresh to make it show up in the table
                } else {
                    newIngredients.put(foodItem, newIngredients.get(foodItem) - Integer.parseInt(numIngredientsText.getText()));
                    orderTable.refresh();
                }
                responseText.setVisible(false);
            } else {
                responseText.setVisible(true);
            }
        }));
        orderTable.setItems(FXCollections.observableArrayList(newIngredients.keySet()));

        codeCol.setCellValueFactory(new PropertyValueFactory<>("code"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        costCol.setCellValueFactory(cellData -> new SimpleStringProperty(MONEY_FORMATTER.print(cellData.getValue().getPrice())));
        vegCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getIsVeg().toString()));
        veganCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getIsVegan().toString()));
        gfCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getIsGF().toString()));
        addButtonColumn.setCellFactory(ActionButtonTableCell_SalesController.forTableColumn("Add", foodItem -> {
            // First check that the fields are valid.
            if (fieldsAreValid2()) {
                //If they are add the specified quantity and ingredient.
                if (newIngredients.containsKey(foodItem)) {
                    newIngredients.put(foodItem, newIngredients.get(foodItem) + Integer.parseInt(ingredientText.getText()));
                } else {
                    newIngredients.put(foodItem, Integer.parseInt(ingredientText.getText()));
                }
                orderTable.setItems(FXCollections.observableArrayList(newIngredients.keySet()));
                orderTable.refresh();
                responseText.setVisible(false);
            } else {
                responseText.setVisible(true);
            }
        }));

        table.setItems(FXCollections.observableArrayList(business.getIngredientTable().resolveAllIngredients().values()));

    }



    /**
     * A method that checks the fields in the GUI screen are valid.
     *
     * @return a boolean, true if all fields are valid, false otherwise.
     */

    public boolean fieldsAreValid() {
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
     * A method that checks the fields in the GUI screen are valid.
     *
     * @return a boolean, true if all fields are valid, false otherwise.
     */
    public boolean fieldsAreValid2() {
        if (ingredientText.getText().trim().length() == 0) {
            responseText.setText("The amount field is blank.");
            return false;
        }
        try {
            Integer.parseInt(ingredientText.getText());
        } catch (NumberFormatException e) {
            responseText.setText("Please enter an integer value for number of ingredients.");
            return false;
        }
        if (Integer.parseInt(ingredientText.getText()) < 1) {
            responseText.setText("Please enter an amount greater than zero.");
            return false;
        }
        return true;

    }

    /**
     * Closes the current GUI screen.
     */
    public void closeAndSave() {
        Stage stage = (Stage) table.getScene().getWindow();
        stage.close();
    }

}

package kaitech.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import kaitech.api.database.InventoryTable;
import kaitech.api.model.Business;
import kaitech.api.model.Ingredient;
import kaitech.api.model.Recipe;
import kaitech.model.BusinessImpl;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ModifyRecipeController {

    @FXML
    private TextField name;

    @FXML
    private TextField prepTime;

    @FXML
    private TextField cookTime;


    @FXML
    private Text responseText;

    @FXML
    private Text titleText;

    private Recipe recipe;

    private Map<Ingredient, Integer> newIngredients;
    private InventoryTable inventoryTable;
    private Business business;

    /**
     * Sets the recipe that is being modified.
     *
     * @param recipe the recipe that is being modified.
     */
    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
        start();
    }

    /**
     * Called on start of the screen, sets the fields to the relevant values.
     */
    public void start() {
        name.setText(recipe.getName());
        prepTime.setText(Integer.toString(recipe.getPreparationTime()));
        cookTime.setText(Integer.toString(recipe.getCookingTime()));
        newIngredients = new HashMap<>();
        business = BusinessImpl.getInstance();
        inventoryTable = business.getInventoryTable();
    }

    /**
     * Exits the current screen.
     */
    public void exit() {
        Stage stage = (Stage) titleText.getScene().getWindow();
        stage.close();
    }

    /**
     * Confirms the modifying of the recipe, first checks that the fields are valid.
     */
    public void confirm() {
        if (fieldsAreValid()) {
            recipe.setName(name.getText());
            recipe.setCookingTime(Integer.parseInt(cookTime.getText()));
            recipe.setPreparationTime(Integer.parseInt(prepTime.getText()));

            if (!newIngredients.isEmpty()) {
                recipe.setIngredients(newIngredients);
                int numberOfServings = 99999;
                for (Map.Entry<Ingredient, Integer> entry : newIngredients.entrySet()) {
                    int temp = inventoryTable.getIngredientQuantity(entry.getKey()) / entry.getValue();
                    if (temp < numberOfServings) {
                        numberOfServings = temp;
                    }
                }
                if (newIngredients.isEmpty()) {
                    numberOfServings = 0;
                }
                recipe.setNumServings(numberOfServings);
            }
            responseText.setText("Recipe has been updated!");
            responseText.setVisible(true);
        } else {
            responseText.setVisible(true);
        }
    }

    /**
     * Checks that all the fields in the GUI screen are valid.
     *
     * @return a boolean, true if fields are valid, false otherwise.
     */
    public boolean fieldsAreValid() {
        boolean isValid = true;
        if (name.getText().strip().length() == 0 || prepTime.getText().trim().length() == 0 || cookTime.getText().trim().length() == 0) {
            responseText.setText("A field is empty.");
            isValid = false;
        } else {
            try {
                Integer.parseInt(prepTime.getText());
                Integer.parseInt(cookTime.getText());
                if (Integer.parseInt(cookTime.getText()) < 0 || (Integer.parseInt(prepTime.getText()) < 0)) {
                    responseText.setText("Please enter a positive integer for cook and prep time.");
                    return false;

                }
            } catch (NumberFormatException e) {
                isValid = false;
                responseText.setText("Please enter only numbers for the prep time and cook time.");
            }
        }

        return isValid;
    }

    /**
     * Launches a screen where the user can select the new ingredients for the recipe.
     */
    public void selectIngredients() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("addIngredient.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.setTitle("Add Ingredients");
            stage.setScene(new Scene(root));
            stage.show();
            AddIngredientToRecipeController controller = loader.getController();
            controller.setRecipe(newIngredients);
            controller.setModifyMessage();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

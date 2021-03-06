package kaitech.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import kaitech.api.database.RecipeTable;
import kaitech.api.model.Business;
import kaitech.api.model.Ingredient;
import kaitech.model.BusinessImpl;
import kaitech.model.RecipeImpl;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class NewRecipeController {

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
    @FXML
    private TextField numServings;

    private RecipeTable recipeTable;

    private Map<Ingredient, Integer> newIngredients;

    public void initialize() {
        Business business = BusinessImpl.getInstance();
        recipeTable = business.getRecipeTable();
        newIngredients = new HashMap<>();
    }

    /**
     * Closes the current screen.
     */
    public void exit() {
        Stage stage = (Stage) titleText.getScene().getWindow();
        stage.close();
    }

    /**
     * Checks the return value of fieldsAreValid, if it is true confirms the adding of the recipe, otherwise it
     * gives the user an informative message.
     */
    public void confirm() {
        if (fieldsAreValid()) {
            String name = this.name.getText();
            int preparationTime = Integer.parseInt(prepTime.getText());
            int cookingTime = Integer.parseInt(cookTime.getText());
            int numberOfServings = Integer.parseInt(numServings.getText());

            RecipeImpl newRecipe = new RecipeImpl(name, preparationTime, cookingTime, numberOfServings, newIngredients);
            recipeTable.putRecipe(newRecipe);
            responseText.setText("Recipe has been added!");
            responseText.setVisible(true);
        } else {
            responseText.setVisible(true);
        }
    }

    /**
     * Checks whether all the fields are valid.
     *
     * @return a boolean, true if fields are valid, false otherwise.
     */
    public boolean fieldsAreValid() {
        boolean isValid = true;
        if (name.getText().trim().length() == 0 || prepTime.getText().trim().length() == 0 || cookTime.getText().trim().
                length() == 0 || numServings.getText().trim().length() == 0) {
            responseText.setText("A field is empty.");
            isValid = false;
        } else {
            try {
                Integer.parseInt(prepTime.getText());
                Integer.parseInt(cookTime.getText());
                Integer.parseInt(numServings.getText());
                if (Integer.parseInt(cookTime.getText()) < 0 || (Integer.parseInt(prepTime.getText()) < 0)) {
                    responseText.setText("Please enter a positive integer for cook and prep time.");
                    return false;

                }
                if (Integer.parseInt(numServings.getText()) < 1) {
                    responseText.setText("Please enter a value greater than 0 for num servings.");
                    return false;
                }
            } catch (NumberFormatException e) {
                isValid = false;
                responseText.setText("Please enter only numbers for prep time and cook time.");
            }
        }

        return isValid;
    }

    /**
     * Launches a screen, where the user can select the ingredients for the recipe.
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
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

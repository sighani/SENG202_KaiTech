package kaitech.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import kaitech.api.database.RecipeTable;
import kaitech.api.model.Business;
import kaitech.api.model.Ingredient;
import kaitech.model.BusinessImpl;
import kaitech.model.RecipeImpl;

import java.util.HashMap;
import java.util.Map;

public class NewRecipeController {
    @FXML
    private TextField prepTime;
    @FXML
    private TextField cookTime;
    @FXML
    private TextField numServings;
    @FXML
    private Text responseText;
    @FXML
    private Text titleText;

    private RecipeTable recipeTable;

    private Business business;

    public void initialize() {

        business = BusinessImpl.getInstance();
        recipeTable = business.getRecipeTable();
    }

    public void exit() {
        Stage stage = (Stage) titleText.getScene().getWindow();
        stage.close();

    }

    public void confirm() {
        if(fieldsAreValid()) {
            Map<Ingredient, Integer> ingredients = new HashMap<>();
            int preparationTime = Integer.parseInt(prepTime.getText());
            int cookingTime = Integer.parseInt(cookTime.getText());
            int numberOfServings = Integer.parseInt(numServings.getText());

            RecipeImpl newRecipe = new RecipeImpl(preparationTime, cookingTime, numberOfServings, ingredients);
            recipeTable.putRecipe(newRecipe);
            responseText.setText("Recipe has been added!");
            responseText.setVisible(true);
        }
        else {
            responseText.setVisible(true);
        }
    }

    public boolean fieldsAreValid() {
        boolean isValid = true;
        if (prepTime.getText().trim().length() == 0 || cookTime.getText().trim().length() == 0 ||
                numServings.getText().trim().length() == 0) {
            responseText.setText("A field is empty.");
            isValid = false;
        }
        else {
            try {
                Integer.parseInt(prepTime.getText());
                Integer.parseInt(cookTime.getText());
                Integer.parseInt(numServings.getText());
            }
            catch(NumberFormatException e) {
                isValid = false;
                responseText.setText("Please enter only a number for for all fields.");
            }
        }

        return isValid;
    }


}

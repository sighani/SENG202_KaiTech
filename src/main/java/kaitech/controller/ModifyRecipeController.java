package kaitech.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import kaitech.api.model.Ingredient;
import kaitech.api.model.Recipe;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ModifyRecipeController {

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

    private Recipe recipe;

    private Map<Ingredient, Integer> newIngredients;

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
        start();
    }

    public void start() {
        prepTime.setText(Integer.toString(recipe.getPreparationTime()));
        cookTime.setText(Integer.toString(recipe.getCookingTime()));
        numServings.setText(Integer.toString(recipe.getNumServings()));
        newIngredients = new HashMap<>();
        newIngredients.putAll(recipe.getIngredients());
    }

    public void exit() {
        Stage stage = (Stage) titleText.getScene().getWindow();
        stage.close();
    }

    public void confirm() {
        if (fieldsAreValid()) {
            recipe.setCookingTime(Integer.parseInt(prepTime.getText()));
            recipe.setPreparationTime(Integer.parseInt(cookTime.getText()));
            recipe.setNumServings(Integer.parseInt(numServings.getText()));
            if (!newIngredients.isEmpty()) {
                recipe.setIngredients(newIngredients);
            }
            responseText.setText("Recipe has been updated!");
            responseText.setVisible(true);
        } else {
            responseText.setVisible(true);
        }
    }

    public boolean fieldsAreValid() {
        boolean isValid = true;
        if (prepTime.getText().trim().length() == 0 || cookTime.getText().trim().length() == 0 ||
                numServings.getText().trim().length() == 0) {
            responseText.setText("A field is empty.");
            isValid = false;
        } else {
            try {
                Integer.parseInt(prepTime.getText());
                Integer.parseInt(cookTime.getText());
                Integer.parseInt(numServings.getText());
            } catch (NumberFormatException e) {
                isValid = false;
                responseText.setText("Please enter only a number for for all fields.");
            }
        }

        return isValid;
    }

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
            AddIngredientToRecipeController controller = loader.<AddIngredientToRecipeController>getController();
            controller.setRecipe(newIngredients);
            controller.setModifyMessage();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

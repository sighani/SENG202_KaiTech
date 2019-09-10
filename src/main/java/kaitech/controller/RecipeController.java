package kaitech.controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import kaitech.api.database.RecipeTable;
import kaitech.api.model.Business;
import kaitech.api.model.Ingredient;
import kaitech.api.model.Recipe;
import kaitech.model.BusinessImpl;
import kaitech.model.RecipeImpl;

import java.util.HashMap;
import java.util.Map;

public class RecipeController {
    @FXML
    TableView table;
    @FXML
    private TableColumn<Recipe, String> recipeID;

    @FXML
    private TableColumn<Recipe, String> prepTime;

    @FXML
    private TableColumn<Recipe, String> cookTime;

    @FXML
    private TableColumn<Recipe, String> numServings;

    private Business business;

    private RecipeTable recipeTable;

    public void initialize() {
        business = BusinessImpl.getInstance();
        Map<Ingredient, Integer> ingredients = new HashMap<>();
        recipeTable = business.getRecipeTable();
        Recipe newRecipe = new RecipeImpl(1, 20, 40, 20, ingredients);
        Recipe newRecipe1 = new RecipeImpl(2, 40, 20, 10, ingredients);
        recipeTable.putRecipe(newRecipe);
        recipeTable.putRecipe(newRecipe1);

        recipeID.setCellValueFactory(cellData -> new SimpleStringProperty(Integer.toString(cellData.getValue().getID())));
        prepTime.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPreparationTime() + " minutes"));
        cookTime.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCookingTime() + " minutes"));
        numServings.setCellValueFactory(cellData -> new SimpleStringProperty(Integer.toString(cellData.getValue().getNumServings())));

        table.setItems(FXCollections.observableArrayList(business.getRecipeTable().resolveAllRecipes().values()));
    }

}

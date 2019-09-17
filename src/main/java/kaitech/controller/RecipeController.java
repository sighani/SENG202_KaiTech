package kaitech.controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import kaitech.api.database.RecipeTable;
import kaitech.api.model.Business;
import kaitech.api.model.Ingredient;
import kaitech.api.model.Recipe;
import kaitech.model.BusinessImpl;
import kaitech.model.RecipeImpl;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class RecipeController {
    @FXML
    TableView<Recipe> table;
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

    /**
     * Deletes the record of the chosen sale.
     *
     * @param event when the deleteRecord button is pressed.
     */
    public void deleteRecord(ActionEvent event) {
        recipeTable.removeRecipe(table.getSelectionModel().getSelectedItem().getID());
        table.setItems(FXCollections.observableArrayList(business.getRecipeTable().resolveAllRecipes().values()));


    }

    /**
     * Changes the currently displayed scene to the main menu.
     *
     * @param event Indicates the event which occurred, which caused the method to be called.
     */
    @FXML
    public void returnToMain(ActionEvent event) throws IOException {
        try {
            Parent mainMenuParent = FXMLLoader.load(getClass().getResource("MainMenu.fxml"));
            Scene MainMenuScene = new Scene(mainMenuParent);

            Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
            window.setTitle("Main Menu");
            window.setScene(MainMenuScene);
            window.show();

        } catch (IOException e) {
            throw new IOException("Error in exiting manual input.");

        }


    }


}

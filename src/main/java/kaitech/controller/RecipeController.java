package kaitech.controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import kaitech.api.database.RecipeTable;
import kaitech.api.model.Business;
import kaitech.api.model.Ingredient;
import kaitech.api.model.Recipe;
import kaitech.model.BusinessImpl;
import kaitech.model.IngredientImpl;
import kaitech.model.RecipeImpl;
import kaitech.util.LambdaValueFactory;
import kaitech.util.ThreeValueLogic;
import kaitech.util.UnitType;
import org.joda.money.Money;

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
    @FXML
    private TableColumn<Recipe, String> ingredientsCol;

    private Business business;

    private RecipeTable recipeTable;

    public void initialize() {
        business = BusinessImpl.getInstance();
        Map<Ingredient, Integer> ingredients = new HashMap<>();
        recipeTable = business.getRecipeTable();
        Recipe newRecipe = new RecipeImpl(20, 40, 20, ingredients);
        Recipe newRecipe1 = new RecipeImpl(40, 20, 10, ingredients);
        recipeTable.putRecipe(newRecipe);
        recipeTable.putRecipe(newRecipe1);

        recipeID.setCellValueFactory(cellData -> new SimpleStringProperty(Integer.toString(cellData.getValue().getID())));
        prepTime.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPreparationTime() + " minutes"));
        cookTime.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCookingTime() + " minutes"));
        numServings.setCellValueFactory(cellData -> new SimpleStringProperty(Integer.toString(cellData.getValue().getNumServings())));

        ingredientsCol.setCellValueFactory(cellData -> {
            StringBuilder ingredientsString = new StringBuilder();
            for (Map.Entry<Ingredient, Integer> entry : cellData.getValue().getIngredients().entrySet()) {
                ingredientsString.append(entry.getKey().getName()).append(": ").append(entry.getValue()).append(", ");
            }
            if(ingredientsString.length() > 0){
                ingredientsString.deleteCharAt((ingredientsString.length()-1));
                ingredientsString.deleteCharAt((ingredientsString.length()-1));
            }
            return new SimpleStringProperty(ingredientsString.toString());
        });
        table.setItems(FXCollections.observableArrayList(business.getRecipeTable().resolveAllRecipes().values()));
    }

    /**
     * Deletes the record of the chosen sale.
     *
     * @param event when the deleteRecord button is pressed.
     */
    public void deleteRecord(ActionEvent event) {
        if (!business.isLoggedIn()) {
            LogInController l = new LogInController();
            l.showScreen(null);
        }else {
            recipeTable.removeRecipe(table.getSelectionModel().getSelectedItem().getID());
            table.setItems(FXCollections.observableArrayList(business.getRecipeTable().resolveAllRecipes().values()));
        }

    }

    /**
     * Adjusts the details of a sale, used if a sale was initially input incorrectly.
     * @param event the event which caused this method ot be called.
     */


    public void adjustDetails(ActionEvent event) throws IOException{
        try {
            if (!business.isLoggedIn()) {
                LogInController l = new LogInController();
                l.showScreen("modifyRecipe.fxml");
            }else {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("modifyRecipe.fxml"));
                Parent root = loader.load();
                ModifyRecipeController controller = loader.<ModifyRecipeController>getController();
                controller.setRecipe(table.getSelectionModel().getSelectedItem());
                Stage stage = new Stage();
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.setResizable(false);
                stage.setTitle("Modify Record details");
                stage.setScene(new Scene(root));
                stage.show();
                stage.setOnHiding(new EventHandler<WindowEvent>() {
                    @Override
                    public void handle(WindowEvent paramT) {
                        table.getColumns().get(0).setVisible(false);
                        table.getColumns().get(0).setVisible(true);
                    }
                });
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
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

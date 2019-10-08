package kaitech.controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import kaitech.api.database.RecipeTable;
import kaitech.api.model.Business;
import kaitech.api.model.Ingredient;
import kaitech.api.model.MenuItem;
import kaitech.api.model.Recipe;
import kaitech.model.BusinessImpl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;

public class RecipeController {

    @FXML
    TableView<Recipe> table;

    @FXML
    private TableColumn<Recipe, String> recipeID;

    @FXML
    private TableColumn<Recipe, String> name;

    @FXML
    private TableColumn<Recipe, String> prepTime;

    @FXML
    private TableColumn<Recipe, String> cookTime;

    @FXML
    private TableColumn<Recipe, String> numServings;

    @FXML
    private TableColumn<Recipe, String> ingredientsCol;

    @FXML
    private Text responseText;

    private Business business;

    private RecipeTable recipeTable;

    public void initialize() {
        business = BusinessImpl.getInstance();
        recipeTable = business.getRecipeTable();

        recipeID.setCellValueFactory(cellData -> new SimpleStringProperty(Integer.toString(cellData.getValue().getID())));
        name.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        prepTime.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPreparationTime() + " minutes"));
        cookTime.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCookingTime() + " minutes"));
        numServings.setCellValueFactory(cellData -> new SimpleStringProperty(Integer.toString(cellData.getValue().getNumServings())));

        ingredientsCol.setCellValueFactory(cellData -> {
            StringBuilder ingredientsString = new StringBuilder();
            for (Map.Entry<Ingredient, Integer> entry : cellData.getValue().getIngredients().entrySet()) {
                ingredientsString.append(entry.getKey().getName()).append(": ").append(entry.getValue()).append(", ");
            }
            if (ingredientsString.length() > 0) {
                ingredientsString.deleteCharAt((ingredientsString.length() - 1));
                ingredientsString.deleteCharAt((ingredientsString.length() - 1));
            }
            return new SimpleStringProperty(ingredientsString.toString());
        });
        table.setItems(FXCollections.observableArrayList(business.getRecipeTable().resolveAllRecipes().values()));
    }

    /**
     * Deletes the chosen recipe.
     */
    public void deleteRecipe() {
        if (!business.isLoggedIn()) {
            LogInController l = new LogInController();
            l.showScreen();
        } else {
            Boolean skipRemove = false;
            if (table.getSelectionModel().getSelectedItem() == null) {
                responseText.setText("You haven't selected a recipe.");
                responseText.setVisible(true);

            } else {
                Boolean partOfMenuItem = false;
                String menuItemsString = " ";
                for (MenuItem item : business.getMenuItemTable().resolveAllMenuItems().values()) {
                    if (item.getRecipe() == table.getSelectionModel().getSelectedItem()) {
                        partOfMenuItem = true;
                        menuItemsString += item.getName();
                        menuItemsString += ", ";
                    }
                }
                if (partOfMenuItem) {
                    skipRemove = true;
                    Alert alert = new Alert(Alert.AlertType.WARNING, "This recipe is a part of the following Menu Items: " + menuItemsString + "those Menu Items will also be deleted, are you sure you want to continue?", ButtonType.YES, ButtonType.NO);
                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.get() == ButtonType.YES) {
                        //now we need to clean up
                        ArrayList<MenuItem> tempMenuItems = new ArrayList<MenuItem>();
                        for (MenuItem item : business.getMenuItemTable().resolveAllMenuItems().values()) {
                            if (item.getRecipe() == table.getSelectionModel().getSelectedItem()) {
                                tempMenuItems.add(item);
                            }
                        }
                        for (MenuItem item : tempMenuItems) {
                            if (item.getRecipe() == table.getSelectionModel().getSelectedItem()) {
                                business.getMenuItemTable().removeMenuItem(item.getCode());
                            }
                        }
                        recipeTable.removeRecipe(table.getSelectionModel().getSelectedItem().getID());
                        table.setItems(FXCollections.observableArrayList(business.getRecipeTable().resolveAllRecipes().values()));
                        responseText.setText("Recipe and related MenuItems deleted.");
                        responseText.setVisible(true);


                    }
                }
                if (!skipRemove) {
                    recipeTable.removeRecipe(table.getSelectionModel().getSelectedItem().getID());
                    table.setItems(FXCollections.observableArrayList(business.getRecipeTable().resolveAllRecipes().values()));
                    responseText.setText("Recipe deleted.");
                    responseText.setVisible(true);
                }
            }
        }

    }

    /**
     * Adjusts the details of a recipe.
     */
    public void adjustDetails() {
        if (table.getSelectionModel().getSelectedItem() != null) {
            try {
                if (!business.isLoggedIn()) {
                    LogInController l = new LogInController();
                    l.showScreen();
                } else {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("modifyRecipe.fxml"));
                    Parent root = loader.load();
                    Stage stage = new Stage();
                    stage.initModality(Modality.APPLICATION_MODAL);
                    stage.setResizable(false);
                    stage.setTitle("Modify Recipe details");
                    stage.setScene(new Scene(root));
                    stage.show();
                    ModifyRecipeController controller = loader.getController();
                    controller.setRecipe(recipeTable.getOrAddRecipe(table.getSelectionModel().getSelectedItem()));
                    stage.setOnHiding(paramT -> {
                        table.getColumns().get(0).setVisible(false);
                        table.getColumns().get(0).setVisible(true);
                    });
                }
            } catch (IOException | NullPointerException e) {
                e.printStackTrace();
            }
        } else {
            responseText.setText("You haven't selected a recipe.");
            responseText.setVisible(true);
        }
    }

    /**
     * Changes the currently displayed scene to the main menu.
     *
     * @param event Indicates the event which occurred, which caused the method to be called.
     * @throws IOException In case there are any errors
     */
    @FXML
    public void returnToMain(ActionEvent event) throws IOException {
        try {
            Parent mainMenuParent = FXMLLoader.load(getClass().getResource("MainMenu.fxml"));
            Scene MainMenuScene = new Scene(mainMenuParent);

            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setTitle("Main Menu");
            window.setScene(MainMenuScene);
            window.show();
        } catch (IOException e) {
            throw new IOException("Error in exiting manual input.");
        }
    }
}

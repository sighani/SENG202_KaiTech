package kaitech.controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import kaitech.api.model.Business;
import kaitech.api.model.Recipe;
import kaitech.model.BusinessImpl;

import java.io.IOException;

public class AddRecipeToMenuItemController {
    @FXML
    private TableView<Recipe> table;
    @FXML
    private TableColumn<Recipe, String> recipeID;

    @FXML
    private TableColumn<Recipe, String> prepTime;

    @FXML
    private TableColumn<Recipe, String> cookTime;

    @FXML
    private TableColumn<Recipe, String> numServings;
    @FXML
    private Text responseText;
    @FXML
    private TableColumn<Recipe, String> name;

    public void initialize() {
        Business business = BusinessImpl.getInstance();
        recipeID.setCellValueFactory(cellData -> new SimpleStringProperty(Integer.toString(cellData.getValue().getID())));
        prepTime.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPreparationTime() + " minutes"));
        cookTime.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCookingTime() + " minutes"));
        numServings.setCellValueFactory(cellData -> new SimpleStringProperty(Integer.toString(cellData.getValue().getNumServings())));
        name.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));

        table.setItems(FXCollections.observableArrayList(business.getRecipeTable().resolveAllRecipes().values()));
    }


    /**
     * This method launches the screen where the user can enter the details of the MenuItem they want to add,
     * with the selected recipe, being the recipe related to the MenuItem.
     */
    public void selectRecipe() {
        if (table.getSelectionModel().getSelectedItem() == null) {
            responseText.setText("You haven't selected a item.");
            responseText.setVisible(true);

        } else {
            try {
                responseText.setVisible(false);
                Recipe newRecipe = table.getSelectionModel().getSelectedItem();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("newMenuItem.fxml"));
                Parent root = loader.load();
                Stage stage = new Stage();
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.setResizable(false);
                stage.setTitle("Enter Menu Item Details");
                stage.setScene(new Scene(root));
                stage.show();
                NewMenuItemController controller = loader.getController();
                controller.setNewRecipe(newRecipe);
                controller.setComboBoxes();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Closes the current screen.
     */
    public void close() {
        Stage stage = (Stage) table.getScene().getWindow();
        stage.close();
    }
}

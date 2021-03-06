package kaitech.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * The controller for the manualData screen where the user makes a data entry
 * manually by filling out the fields.
 */
public class ManualDataController {
    @FXML
    private TextField ingredCode;
    @FXML
    private TextField ingredName;
    @FXML
    private TextField ingredCost;
    @FXML
    private ComboBox<String> ingredUnit;
    @FXML
    private ComboBox<String> isVege;
    @FXML
    private ComboBox<String> isVegan;
    @FXML
    private ComboBox<String> isGf;

    @FXML
    private Text manualUploadText;

    @FXML
    public void initialize() {
    }

    /**
     * Launches the screen which allows a user to add an ingredient.
     */
    public void addIngredient() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ingredient.fxml"));
            Parent root = loader.load();
            NewIngredientController controller = loader.getController();
            controller.setComboBoxes();
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.setTitle("Adding new supplier:");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
        }
    }


    /**
     * Launches the screen where the user can add a new supplier.
     */
    public void addSupplier() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("newSupplier.fxml"));
            Parent root = loader.load();
            NewSupplierController controller = loader.getController();
            controller.setComboBoxes();
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.setTitle("Adding new supplier:");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
        }
    }

    /**
     * Launches the scene where the user can add a new menu.
     */

    public void addMenu() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("menu.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.setTitle("Adding new menu");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method launches the screen, where the user can select which recipe they want to add a MenuItem for, which
     * will then lead to the screen where they can enter the other details of the MenuItem.
     */
    public void addMenuItem() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("addRecipeToMenuItem.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.setTitle("Select Related Recipe");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
        }
    }


    /**
     * Launches the screen where the user can enter the details of a new recipe and then add it to the business.
     */
    public void addRecipe() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("newrecipe.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.setTitle("Adding new recipe:");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
        }
    }

    /**
     * Launches the screen where the user can add stock to the inventory of the business.
     */
    public void addStock() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("addStock.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.setTitle("Adding stock:");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
        }
    }

    public void addLoyaltyCard() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("newLoyaltyCard.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.setTitle("Adding loyalty card:");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
        }

    }

    /**
     * @param event Exit button is pressed, scene is changed to main menu
     * @throws IOException Print error
     */
    public void exitManualInput(ActionEvent event) throws IOException {
        try {
            //When exit button pressed
            Parent mainMenuParent = FXMLLoader.load(getClass().getResource("MainMenu.fxml"));
            Scene MainMenuScene = new Scene(mainMenuParent);

            //This line gets the Stage information, and returns to main menu
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setTitle("Main Menu");
            window.setScene(MainMenuScene);
            window.show();
        } catch (IOException e) {
            throw new IOException("Error in exiting manual input.");
        }
    }
}

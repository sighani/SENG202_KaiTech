package kaitech.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * The controller for the menuItem screen, which is a screen showing the details
 * of a particular MenuItem.
 */
public class MenuItemController {

    /**
     * This class allows for the adding of a new recipe into the system, before it is added the action
     * has to be confirmed.
     */
    public void addRecipe() {


    }

    /**
     * This class allows for the deleting of a recipe currently stored in the system, before it is deleted the action
     * has to be confirmed.
     */
    public void deleteRecipe() {


    }

    /**
     * This class allows for the modifying of a recipe currently stored in the system, before it is modified the action
     * has to be confirmed.
     */
    public void modifyRecipe() {


    }

    /**
     * Confirms the adding of a recipe.
     */
    public void confirmAdd() {

    }

    /**
     * Confirms the deleting of a recipe.
     */
    public void confirmDelete() {

    }

    /**
     * Confirms the modifying of a recipe.
     */
    public void confirmModify() {

    }

    /**
     * Changes the currently displayed scene to the main menu.
     *
     * @param event Indicates the event which occurred, which caused the method to be called.
     */
    @FXML
    public void returnToMain(ActionEvent event) throws IOException {
        try {
            Parent mainMenuParent = FXMLLoader.load(getClass().getResource("gui/MainMenu.fxml"));
            Scene MainMenuScene = new Scene(mainMenuParent);

            Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
            window.setScene(MainMenuScene);
            window.show();

        } catch (IOException e) {
            throw new IOException("Error in exiting manual input.");

        }


    }

}

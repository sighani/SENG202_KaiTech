package kaitech.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.stage.Stage;
import kaitech.model.MenuItem;

/**
 * The controller for the menuItem screen, which is a screen showing the details
 * of a particular MenuItem.
 */
public class MenuItemController {
    private MenuItem item;




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
    public void returnToMain(ActionEvent event) {
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stage.setScene(mainMenuScene);

    }




}

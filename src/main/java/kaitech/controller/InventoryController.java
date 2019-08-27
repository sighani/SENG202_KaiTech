package kaitech.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.stage.Stage;
import kaitech.model.Ingredient;

/**
 * The controller for the inventory screen for displaying the current inventory
 * and stock of the business.
 */
public class InventoryController {

    public void itemSelected(Ingredient item) {

    }

    /**
     * Edits the currently selected item to the users chosen values.
     */
    public void edit() {

    }

    /**
     * Confirms the action.
     */
    public void confirm() {

    }

    /**
     * Takes the user to the next page.
     *
     * @param event Indicates the event which occurred, which caused the method to be called.
     */

    @FXML
    public void nextPage(ActionEvent event) {
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stage.setScene(nextPage);

    }

    /**
     * Takes the user to the previous page.
     *
     * @param event Indicates the event which occurred, which caused the method to be called.
     */

    @FXML
    public void previousPage(ActionEvent event) {
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stage.setScene(previousPage);

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

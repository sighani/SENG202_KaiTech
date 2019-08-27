package kaitech.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.stage.Stage;
import kaitech.model.Menu;
import kaitech.model.MenuItem;

/**
 * The controller for menu where the user can view and click on each MenuItem in a
 * menu.
 */
public class MenuController {

    Menu menu;

    /**
     * Opens a screen where you can view more details about a specific menu item.
     * @param item
     */

    @FXML
    public void openMenuItem(MenuItem item) {

    }

    /**
     * Takes the user to the next page.
     */

    @FXML
    public void nextPage(ActionEvent event) {
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stage.setScene(nextPage);

    }

    /**
     * Takes the user to the previous page.
     */

    @FXML
    public void previousPage(ActionEvent event) {
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stage.setScene(previousPage);

    }

    /**
     * Allows the user to search through the various menu items to find the desired item.
     */
    public void search() {

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

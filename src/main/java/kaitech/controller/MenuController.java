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
 * The controller for menu where the user can view and click on each MenuItem in a
 * menu.
 */
public class MenuController {

    /**
     * Opens a screen where you can view more details about a specific menu item.
     *
     * @param event when menuItem button is pressed
     * @throws IOException In case there are any errors
     */
    @FXML
    public void openMenuItem(ActionEvent event) throws IOException {
        try {
            Parent menuItemParent = FXMLLoader.load(getClass().getResource("gui/MenuItem.fxml"));
            Scene menuItemScene = new Scene(menuItemParent);

            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(menuItemScene);
            window.show();

        } catch (IOException e) {
            throw new IOException("Error in exiting manual input.");

        }

    }

    /**
     * Takes the user to the next page.
     *
     * @param event The ActionEvent for switching screens
     */
    @FXML
    public void nextPage(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        //stage.setScene(nextPage);
    }

    /**
     * Takes the user to the previous page.
     * @param event The ActionEvent for changing screens
     */
    @FXML
    public void previousPage(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        //stage.setScene(previousPage);

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
     * @throws IOException In case there are any errors
     */
    @FXML
    public void returnToMain(ActionEvent event) throws IOException {
        try {
            Parent mainMenuParent = FXMLLoader.load(getClass().getResource("gui/MainMenu.fxml"));
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

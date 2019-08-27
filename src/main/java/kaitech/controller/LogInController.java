package kaitech.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.stage.Stage;

/**
 * The controller for the login screen, where the user logs in to gain access to
 * restricted actions.
 */
public class LogInController {

    /**
     * This allows the user to log into their account, by entering their details. When logged in they will have access
     * to extra functionality.
     */
    public void login() {

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

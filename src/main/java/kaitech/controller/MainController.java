package kaitech.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.stage.Stage;

/**
 * The controller for the main menu screen, which is the home of the system. It
 * opens most of the other fxml files.
 */
public class MainController {

    /**
     * Opens the inventory screen for viewing.
     *
     * @param event Indicates the event which occurred, which caused the method to be called.
     */

    @FXML
    public void openInventoryScreen(ActionEvent event) {
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stage.setScene(inventoryScreen);

    }

    /**
     * Opens the records screen for viewing.
     *
     * @param event Indicates the event which occurred, which caused the method to be called.
     */

    @FXML
    public void openRecordsScreen(ActionEvent event) {
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stage.setScene(recordsScreen);

    }

    /**
     * Opens the sales screen for viewing.
     *
     * @param event Indicates the event which occurred, which caused the method to be called.
     */

    @FXML
    public void openSalesScreen(ActionEvent event) {
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stage.setScene(inventoryScreen);

    }

    /**
     * Opens the data screen for viewing.
     *
     * @param event Indicates the event which occurred, which caused the method to be called.
     */

    @FXML
    public void openDataScreen(ActionEvent event) {
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stage.setScene(inventoryScreen);

    }

    /**
     * Quits the application.
     *
     * @param event Indicates the event which occurred, which caused the method to be called.
     */

    @FXML
    public void quit(ActionEvent event) {
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stage.close();

    }

}

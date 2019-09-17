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
 * The controller for the uploading data screen. Here the user can choose to
 * upload an XML file or a manual data entry. The user is taken to the appropriate
 * screen with the corresponding controllers.
 */
public class DataController {
    /**
     * Opens the XML data entry screen.
     *
     * @param event when upload button pressed, open XML data entry screen.
     * @throws IOException prints error.
     */
    @FXML
    public void openXMLScreen(ActionEvent event) throws IOException {
        try {
            Parent XMLScreenParent = FXMLLoader.load(getClass().getResource("dataSelection.fxml"));
            Scene XMLScreenScene = new Scene(XMLScreenParent);

            Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
            window.setScene(XMLScreenScene);
            window.show();

        } catch (IOException e) {
            throw new IOException("Error in exiting manual input.");

        }


    }

    /**
     * Opens the manual data entry screen.
     *
     * @param event when ManualEntry button pressed, open manual data entry screen.
     * @throws IOException
     */

    @FXML
    public void openManualScreen(ActionEvent event) throws IOException {
        try {
            Parent manualScreenParent = FXMLLoader.load(getClass().getResource("DataType.fxml"));
            Scene ManualScreenScene = new Scene(manualScreenParent);

            Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
            window.setScene(ManualScreenScene);
            window.show();

        } catch (IOException e) {
            throw new IOException("Error in exiting manual input.");

        }


    }
    /**
     * Returns the user to the main menu screen.
     *
     * @param event when MainMenu button pressed, return to main menu.
     * @throws IOException prints error.
     */
    @FXML
    public void returnToMain(ActionEvent event) throws IOException {
        try {
            Parent mainMenuParent = FXMLLoader.load(getClass().getResource("gui/MainMenu.fxml"));
            Scene MainMenuScene = new Scene(mainMenuParent);

            Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
            window.setTitle("Main Menu");
            window.setScene(MainMenuScene);
            window.show();

        } catch (IOException e) {
            throw new IOException("Error in exiting manual input.");

        }


    }
}

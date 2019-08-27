package kaitech.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.stage.Stage;

/**
 * The controller for the uploading data screen. Here the user can choose to
 * upload an XML file or a manual data entry. The user is taken to the appropriate
 * screen with the corresponding controllers.
 */
public class DataController {
    /**
     * Changes the currently displayed scene to be the XML data entry screen, where the user can then enter data
     * through using an XML file.
     */

    @FXML
    public void openXMLScreen(ActionEvent event) {
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stage.setScene(XMLScreen);


    }

    /**
     * Changes the currently displayed scene to be the manual data entry screen, where the user can then enter data
     * manually.
     */

    @FXML
    public void openManualScreen(ActionEvent event) {
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stage.setScene(manualDataScreen);

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

package kaitech.controller;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.stage.Stage;

/**
 * The controller for the XML data entry screen, where the user can select and upload
 * an XML file to be parsed.
 */
public class XMLDataController {

    /**
     * Opens the XML data entry screen.
     */
    public void chooseFile() {
    }

    /**
     * Opens the manual data entry screen.
     */
    public void displayFile() {

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

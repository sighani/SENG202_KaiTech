package kaitech.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

/**
 * The controller for the XML data entry screen, where the user can select and upload
 * an XML file to be parsed.
 */
public class XMLDataController {
    /**
     *
     * @param event exit button pressed
     * @throws IOException print error
     */
    public void exitUpload(ActionEvent event) throws IOException {
        try {
            //When exit button pressed from upload screen, set main menu scene
            Parent mainMenuParent = FXMLLoader.load(getClass().getResource("MainMenu.fxml"));
            Scene MainMenuScene = new Scene(mainMenuParent);

            //Get stage info and set scene to main menu
            Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
            window.setScene(MainMenuScene);
            window.show();
        }catch (IOException e){
            throw new IOException("Error in exiting upload.");
        }


    }
}

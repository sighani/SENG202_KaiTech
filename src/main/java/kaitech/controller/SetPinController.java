package kaitech.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import kaitech.api.model.Business;
import kaitech.model.BusinessImpl;

import java.io.IOException;

public class SetPinController {
    @FXML
    private PasswordField pinField;

    @FXML
    private Text resultText;

    private Business business;

    @FXML
    public void initialize() {
        business = BusinessImpl.getInstance();

    }

    /**
     * Allows the user to change the pin of the business. The user must enter the current pin to be able to change to
     * a new pin, unless the pin has not been set yet.
     */
    public void setPin() {
        try {
            if (business.isLoggedIn() || business.getIsPinEmpty(Business.DEFAULT_USER)) {
                business.setPin(Business.DEFAULT_USER, pinField.getCharacters());
                business.logOut();
                resultText.setText("Pin was successfully changed.");
            } else {
                resultText.setText("Confirm your current pin first.");
                LogInController l = new LogInController();
                l.showScreen("");
            }
        } catch (IllegalArgumentException e) {
            resultText.setText(e.getMessage());
        } finally {
            resultText.setVisible(true);
        }
    }

    public void back(ActionEvent event) throws IOException {
        try {
            if (business.getIsPinEmpty(Business.DEFAULT_USER)) {
                resultText.setText("A pin must be set first!");
                resultText.setVisible(true);
            } else {
                Parent mainMenuParent = FXMLLoader.load(getClass().getResource("MainMenu.fxml"));
                Scene MainMenuScene = new Scene(mainMenuParent);

                Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
                window.setTitle("Main Menu");
                window.setScene(MainMenuScene);
                window.show();
            }

        } catch (IOException e) {
            throw new IOException("Error in exiting manual input.");
        }
    }
}

package kaitech.controller;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
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
            if (business.isLoggedIn() || business.getPinIsNull()) {
                business.setPin(pinField.getText());
                business.logOut();
                resultText.setText("Pin was successfully changed.");
            }
            else {
                try {
                    resultText.setText("Confirm your current pin first.");
                    Parent root = FXMLLoader.load(getClass().getResource("login.fxml"));
                    Stage stage = new Stage();
                    stage.initModality(Modality.APPLICATION_MODAL);
                    stage.setResizable(false);
                    stage.setTitle("Confirm your current pin");
                    stage.setScene(new Scene(root, 400, 250));
                    stage.show();
                    stage.setOnHiding(new EventHandler<WindowEvent>() {
                        @Override
                        public void handle(WindowEvent paramT) {
                            business.setPin(pinField.getText());
                            business.logOut();
                            resultText.setText("Pin was successfully changed.");
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        catch (IllegalArgumentException e) {
            resultText.setText(e.getMessage());
        }
        finally {
            resultText.setVisible(true);
        }
    }
}

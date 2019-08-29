package kaitech.controller;

import javafx.fxml.FXML;
import javafx.scene.text.Text;
import kaitech.api.model.Business;

/**
 * The controller for the login screen, where the user logs in to gain access to
 * restricted actions.
 */
public class LogInController {
    @FXML
    private Text passwordText;

    @FXML
    private Text loginButton;

    @FXML
    private Text loginResponse;

    private Business business;

    public void login() {
        if (!passwordText.getText().isEmpty()) {
            boolean loggedIn = business.logIn(passwordText.getText());
            if (loggedIn) {
                loginResponse.setText("Log in was successful.");
            }
            loginResponse.setVisible(true);
        }
    }
}

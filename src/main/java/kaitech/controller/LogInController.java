package kaitech.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import kaitech.api.model.Business;
import kaitech.model.BusinessImpl;

/**
 * The controller for the login screen, where the user logs in to gain access to
 * restricted actions.
 */
public class LogInController {
    @FXML
    private PasswordField passwordText;

    @FXML
    private Text loginResponse;

    @FXML
    private Button loginButton;

    private Business business;

    @FXML
    public void initialize() {
        business = BusinessImpl.getInstance();
    }

    public void login() {
        if (!passwordText.getText().isEmpty()) {
            boolean loggedIn = business.logIn(passwordText.getText());
            if (loggedIn) {
                Stage stage = (Stage) loginButton.getScene().getWindow();
                stage.close();
            }
            else {
                loginResponse.setText("Pin incorrect");
            }
            loginResponse.setVisible(true);
        }
    }
}

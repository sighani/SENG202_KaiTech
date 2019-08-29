package kaitech.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
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

    private BusinessImpl business;

    public void login() {
        // Place holder to create business object. In reality, would need to get the business as a parameter.
        business = new BusinessImpl();
        business.setPin("1042");
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

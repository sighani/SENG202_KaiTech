package kaitech.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
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
    private Button btnBack;

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
            boolean loggedIn = business.logIn(Business.DEFAULT_USER, passwordText.getCharacters());
            if (loggedIn) {
                Stage stage = (Stage) loginButton.getScene().getWindow();
                stage.close();
            } else {
                loginResponse.setText("Pin incorrect");
            }
            loginResponse.setVisible(true);
        }
    }

    public void showScreen(String nextScreen) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("login.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage(StageStyle.UNDECORATED);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.setTitle("Please Log in");
            stage.setScene(new Scene(root));
            stage.show();
            stage.setAlwaysOnTop(true);
        } catch (Exception e) {
            System.out.println("Unknown exception");
        }
    }

    public void cancelLogin(){
        //needs to go back
        Stage stage = (Stage) btnBack.getScene().getWindow();
        stage.close();
    }

}

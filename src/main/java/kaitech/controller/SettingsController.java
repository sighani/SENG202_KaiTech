package kaitech.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import kaitech.api.model.Business;
import kaitech.model.BusinessImpl;

import java.io.IOException;

public class SettingsController {
    /**
     * Singleton business object
     */
    private Business business;

    @FXML
    private TextField txtLoyaltyPercent;

    @FXML
    Button btnSetPercent;

    @FXML
    public void initialize() {
        business = BusinessImpl.getInstance();
        txtLoyaltyPercent.setText(Integer.toString(business.getLoyaltyCardSettingsTable().getCurrentPercentage()));
    }

    /**
     * Deletes the database and closes the program
     */
    private Label alertLabel = new Label("You are about to delete all data and close the program, are you sure?");

    public void reset() {
        alertLabel.setWrapText(true);
        Alert alert = new Alert(Alert.AlertType.WARNING, "", ButtonType.YES, ButtonType.NO);
        alert.getDialogPane().setContent(alertLabel);

        alert.showAndWait();
        if (alert.getResult().equals(ButtonType.YES)) {
            BusinessImpl.reset();
        }
    }

    public void setPin(ActionEvent event) throws IOException {
        try {
            Parent setPinParent = FXMLLoader.load(getClass().getResource("setPin.fxml"));
            Scene setPinScene = new Scene(setPinParent);

            //Get stage info and switch scenes.
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setTitle("Change Pin");
            window.setScene(setPinScene);
            window.show();
        } catch (IOException e) {
            throw new IOException("Error in opening set pin scene");
        }
    }

    public void backToMain(ActionEvent event) throws IOException {
        try {
            //Back to main menu
            Parent recordsParent = FXMLLoader.load(getClass().getResource("MainMenu.fxml"));
            Scene recordsScene = new Scene(recordsParent);

            //Get stage info and switch scenes.
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setTitle("Main Menu");
            window.setScene(recordsScene);
            window.show();
        } catch (IOException e) {
            throw new IOException("Error in opening Main Menu scene.");
        }
    }

    public void setLoyaltyPercentage() {
        try {
            business.getLoyaltyCardSettingsTable().setCurrentPercentage(Integer.parseInt(txtLoyaltyPercent.getText()));
        } catch (Exception e) {
            // Enter a valid amount
            e.printStackTrace();
        }
    }

    public void viewLoyaltyCards() {
        try {
            //opens a new "New Ingredient" screen for each missing code
            FXMLLoader loaderTemp = new FXMLLoader(getClass().getResource("loyaltyCardView.fxml"));
            Parent rootTemp = loaderTemp.load();
            Stage stageTemp = new Stage();
            stageTemp.initModality(Modality.APPLICATION_MODAL);
            stageTemp.setResizable(false);
            stageTemp.setTitle("Loyalty Cards");
            stageTemp.setScene(new Scene(rootTemp));
            stageTemp.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

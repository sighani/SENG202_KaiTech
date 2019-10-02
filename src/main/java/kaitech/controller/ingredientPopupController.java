package kaitech.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;
import kaitech.io.LoadData;

import java.util.List;

public class ingredientPopupController {

    @FXML
    private Label lblCodesMissed;

    @FXML
    private Button btnCancel;

    @FXML
    private Button btnManual;

    private List<String> missingIng;

    @FXML
    public void initialize(){
        missingIng = LoadData.getMissingIngredients();
        lblCodesMissed.setText(missingIng.toString());

    }

    public void exit(){
        //close and go to main menu
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("MainMenu.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.setTitle("Main Menu");
            stage.setScene(new Scene(root));
            stage.show();
        }catch (Exception e) {
            //fix
        }
        Stage currentStage = (Stage) btnCancel.getScene().getWindow();
        currentStage.close();

    }

    public void manualEntry(){
        //take to the ingredient screen
        try {
            //loads main menu in the background
            FXMLLoader loader = new FXMLLoader(getClass().getResource("MainMenu.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.setTitle("Main Menu");
            stage.setScene(new Scene(root));
            stage.show();

            //loads a new ingredient window for each new ingredient
            for(String s : missingIng){
                //opens a new "New Ingredient" screen for each missing code
                FXMLLoader loaderTemp = new FXMLLoader(getClass().getResource("ingredient.fxml"));
                Parent rootTemp = loaderTemp.load();
                NewIngredientController controller = loaderTemp.getController();
                controller.setComboBoxes();
                controller.getingredCode().setText(s);
                Stage stageTemp = new Stage();
                stageTemp.initModality(Modality.APPLICATION_MODAL);
                stageTemp.setResizable(false);
                stageTemp.setTitle("Main Menu");
                stageTemp.setScene(new Scene(rootTemp));
                stageTemp.show();
            }
        }catch (Exception e) {
            //fix
        }
        Stage currentStage = (Stage) btnCancel.getScene().getWindow();
        currentStage.close();

    }
}

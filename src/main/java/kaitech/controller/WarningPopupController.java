package kaitech.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class WarningPopupController {
    @FXML
    private Button btnConfirm;

    @FXML
    public void initilize(){
        //comment
    }

    public void exit(){
        Stage stage = (Stage) btnConfirm.getScene().getWindow();
        stage.close();
    }
}

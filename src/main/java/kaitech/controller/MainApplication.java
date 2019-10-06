package kaitech.controller;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import kaitech.api.model.Business;
import kaitech.model.BusinessImpl;

import java.io.IOException;

public class MainApplication extends Application {
    @Override
    public void start(Stage primaryStage) throws IOException {
        Business business = BusinessImpl.getInstance();
        if (business.getIsPinEmpty(Business.DEFAULT_USER)) {
            Parent root = FXMLLoader.load(getClass().getResource("setPin.fxml"));
            primaryStage.setTitle("Setup");
            primaryStage.setScene(new Scene(root, 1024, 576));
            primaryStage.show();
        } else {
            Parent root = FXMLLoader.load(getClass().getResource("MainMenu.fxml"));
            primaryStage.setTitle("Main Menu");
            primaryStage.setScene(new Scene(root, 1024, 576));
            primaryStage.show();
        }
    }

    public static void main(String[] args) {
        Business business = BusinessImpl.getInstance();
        launch(args);
    }
}

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
        Parent root = FXMLLoader.load(getClass().getResource("MainMenu.fxml"));
        primaryStage.setTitle("Inventory");
        primaryStage.setScene(new Scene(root, 700, 400));
        primaryStage.show();
    }

    public static void main(String[] args) {
        Business business = BusinessImpl.getInstance();
        BusinessImpl.reset();
        launch(args);
    }
}

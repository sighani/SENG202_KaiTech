package kaitech.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import kaitech.api.model.Business;
import kaitech.model.BusinessImpl;

import java.io.IOException;

public class MainController {
    @FXML
    private Text actionTarget;

    /**
     * The business object
     */
    private Business business = BusinessImpl.getInstance();

    /**
     * @param event upload button pressed, open data selection scene
     * @throws IOException display error
     */
    public void upload(ActionEvent event) throws IOException {
        try {
            //When logout button pressed, from home screen get DataSelection scene
            Parent dataSelectParent = FXMLLoader.load(getClass().getResource("XmlLoader.fxml"));
            Scene dataSelectScene = new Scene(dataSelectParent);

            //Get stage info and switch scenes.
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setTitle("Upload File");
            window.setScene(dataSelectScene);
            window.show();
        } catch (IOException e) {
            throw new IOException("Error in opening upload scene");
        }

    }

    /**
     * @param event when manual input button pressed, opening data type selection scene
     * @throws IOException print error
     */
    public void manualInput(ActionEvent event) throws IOException {
        try {
            if (!business.isLoggedIn()) {
                LogInController l = new LogInController();
                l.showScreen("modifyRecipe.fxml");
            } else {
                //When manual input button pressed, from home screen, get data type scene
                Parent dataTypeParent = FXMLLoader.load(getClass().getResource("dataType.fxml"));
                Scene dataTypeScene = new Scene(dataTypeParent);

                //Get stage info and switch scenes.
                Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
                window.setTitle("Manual Data Entry");
                window.setScene(dataTypeScene);
                window.show();
            }
        } catch (IOException e) {
            throw new IOException("Error in opening data type scene");
        }
    }

    /**
     * When the menu button is pressed, display message
     */
    public void menu(ActionEvent event) throws IOException {
        try {
            Parent inventoryParent = FXMLLoader.load(getClass().getResource("menuItem.fxml"));
            Scene inventoryScene = new Scene(inventoryParent);

            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setTitle("Menu");
            window.setScene(inventoryScene);
            window.show();
        } catch (IOException e) {
            throw new IOException("Error in opening inventory scene.");
        }
    }

    /**
     * When the inventory button is pressed, display message
     */
    public void inventory(ActionEvent event) throws IOException {
        try {
            Parent inventoryParent = FXMLLoader.load(getClass().getResource("inventory.fxml"));
            Scene inventoryScene = new Scene(inventoryParent);

            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setTitle("Inventory");
            window.setScene(inventoryScene);
            window.show();
        } catch (IOException e) {
            throw new IOException("Error in opening inventory scene.");
        }
    }


    /**
     * When the records button is pressed, open the records screen.
     *
     * @param event when the records button on the main menu gets pressed.
     * @throws IOException prints an error message
     */
    public void records(ActionEvent event) throws IOException {
        try {
            //When sales button pressed, from home screen, get sales scene
            Parent recordsParent = FXMLLoader.load(getClass().getResource("records.fxml"));
            Scene recordsScene = new Scene(recordsParent);

            //Get stage info and switch scenes.
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setTitle("Sales Records");
            window.setScene(recordsScene);
            window.show();
        } catch (IOException e) {
            throw new IOException("Error in opening records scene.");
        }
    }

    /**
     * Called when the recipe button on the MainMenu.fxml is pressed, opens the recipe.fxml screen.
     *
     * @param event when the recipe button is pressed.
     * @throws IOException throws an error.
     */
    public void recipes(ActionEvent event) throws IOException {
        //try {
            //When sales button pressed, from home screen, get sales scene
            Parent recordsParent = FXMLLoader.load(getClass().getResource("recipe.fxml"));
            Scene recordsScene = new Scene(recordsParent);

            //Get stage info and switch scenes.
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setTitle("Recipes");
            window.setScene(recordsScene);
            window.show();
        //} catch (IOException e) {
           // throw new IOException("Error in opening suppliers scene");
        //}
    }

    /**
     * When the suppliers button is pressed, display message
     */
    public void suppliers(ActionEvent event) throws IOException {
        try {
            //When sales button pressed, from home screen, get sales scene
            Parent recordsParent = FXMLLoader.load(getClass().getResource("suppliers.fxml"));
            Scene recordsScene = new Scene(recordsParent);

            //Get stage info and switch scenes.
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setTitle("Suppliers");
            window.setScene(recordsScene);
            window.show();
        } catch (IOException e) {
            throw new IOException("Error in opening suppliers scene", e);
        }
    }

    /**
     * @param event when the sales button is pressed, open the sales scene
     * @throws IOException print error message
     */
    public void sales(ActionEvent event) throws IOException {
        //When sales button pressed, from home screen, get sales scene
        Parent salesParent = FXMLLoader.load(getClass().getResource("Sales.fxml"));
        Scene salesScene = new Scene(salesParent);

        //Get stage info and switch scenes.
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setTitle("Sales");
        window.setScene(salesScene);
        window.show();
    }

    /**
     * Logs the user out if they are logged in
     */
    public void logout() {
        Business business = BusinessImpl.getInstance();
        business.logOut();
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
}

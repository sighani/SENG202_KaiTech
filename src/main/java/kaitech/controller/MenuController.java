package kaitech.controller;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import kaitech.api.database.MenuTable;
import kaitech.api.model.Business;
import kaitech.api.model.Ingredient;
import kaitech.api.model.Menu;
import kaitech.model.BusinessImpl;
import kaitech.util.LambdaValueFactory;

import java.io.IOException;

/**
 * The controller for menu where the user can view and click on each MenuItem in a
 * menu.
 */
public class MenuController {
    @FXML
    private TableView<Ingredient> table;

    @FXML
    private TableColumn<Menu, String> idCol;

    @FXML
    private TableColumn<Menu, String> nameCol;

    @FXML
    private TableColumn<Menu, String> descCol;

    private Business business;
    private MenuTable menuTable;

    @FXML
    public void initialize() {
        business = BusinessImpl.getInstance();
        menuTable = business.getMenuTable();

        idCol.setCellValueFactory(new LambdaValueFactory<>(Menu::getID));
        nameCol.setCellValueFactory(new LambdaValueFactory<>(Menu::getTitle));
        descCol.setCellValueFactory(new LambdaValueFactory<>(Menu::getDescription));
    }

    public void viewItems() {

    }

    /**
     * Changes the currently displayed scene to the main menu.
     *
     * @param event Indicates the event which occurred, which caused the method to be called.
     * @throws IOException In case there are any errors
     */
    @FXML
    public void returnToMain(ActionEvent event) throws IOException {
        try {
            Parent mainMenuParent = FXMLLoader.load(getClass().getResource("gui/MainMenu.fxml"));
            Scene MainMenuScene = new Scene(mainMenuParent);

            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setTitle("Main Menu");
            window.setScene(MainMenuScene);
            window.show();

        } catch (IOException e) {
            throw new IOException("Error in exiting manual input.");
        }
    }
}

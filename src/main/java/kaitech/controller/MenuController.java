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
import javafx.stage.Modality;
import javafx.stage.Stage;
import kaitech.api.database.MenuTable;
import kaitech.api.model.Business;
import kaitech.api.model.Menu;
import kaitech.api.model.MenuItem;
import kaitech.model.BusinessImpl;
import kaitech.util.LambdaValueFactory;

import java.io.IOException;
import java.util.ArrayList;

/**
 * The controller for menu where the user can view and click on each MenuItem in a
 * menu.
 */
public class MenuController {
    @FXML
    private TableView<Menu> table;

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
        table.setItems(FXCollections.observableArrayList(menuTable.resolveAllMenus().values()));
    }

    /**
     * Launches the screen for viewing the MenuItems within the selected Menu only
     */
    public void viewItems() {
        if (table.getSelectionModel().getSelectedItem() != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("menuItem.fxml"));
                Parent root = loader.load();
                Stage stage = new Stage();
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.setResizable(false);
                stage.setTitle("Viewing Menu Items in Menu " + table.getSelectionModel().getSelectedItem().getTitle());
                stage.setScene(new Scene(root));
                stage.show();
                MenuItemController controller = loader.getController();
                ArrayList<MenuItem> menuItems = new ArrayList<>(table.getSelectionModel().getSelectedItem().getMenuItems().values());
                controller.start(false, menuItems);
                stage.setOnHiding(paramT -> {
                    table.getColumns().get(0).setVisible(false);
                    table.getColumns().get(0).setVisible(true);
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Launches the screen for viewing all the MenuItems of the business
     */
    public void viewAllItems() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("menuItem.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.setTitle("Viewing all Menu Items");
            stage.setScene(new Scene(root));
            stage.show();
            MenuItemController controller = loader.getController();
            controller.start(true, null);
            stage.setOnHiding(paramT -> {
                table.getColumns().get(0).setVisible(false);
                table.getColumns().get(0).setVisible(true);
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void modify() {
        if (table.getSelectionModel().getSelectedItem() != null) {
            try {
                if (!business.isLoggedIn()) {
                    LogInController l = new LogInController();
                    l.showScreen();
                } else {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("modifyMenu.fxml"));
                    Parent root = loader.load();
                    Stage stage = new Stage();
                    stage.initModality(Modality.APPLICATION_MODAL);
                    stage.setResizable(false);
                    stage.setTitle("Modify Menu");
                    stage.setScene(new Scene(root));
                    stage.show();
                    ModifyMenuController controller = loader.getController();
                    controller.setMenu(menuTable.getMenu(table.getSelectionModel().getSelectedItem().getID()));
                    stage.setOnHiding(paramT -> {
                        table.getColumns().get(0).setVisible(false);
                        table.getColumns().get(0).setVisible(true);
                    });
                }
            } catch (IOException | NullPointerException e) {
                e.printStackTrace();
            }
        }
    }

    public void delete() {
        if (table.getSelectionModel().getSelectedItem() != null) {
            if (!business.isLoggedIn()) {
                LogInController l = new LogInController();
                l.showScreen();
            } else {
                menuTable.removeMenu(table.getSelectionModel().getSelectedItem().getID());
                table.setItems(FXCollections.observableArrayList(menuTable.resolveAllMenus().values()));
            }
        }
    }

    public void returnToMain(ActionEvent event) {
        try {
            Parent mainMenuParent = FXMLLoader.load(getClass().getResource("MainMenu.fxml"));
            Scene MainMenuScene = new Scene(mainMenuParent);

            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setTitle("Main Menu");
            window.setScene(MainMenuScene);
            window.show();

        } catch (IOException e) {
            System.err.println("Error exiting Menu Controller: " + e);
        }
    }
}

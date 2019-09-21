package kaitech.controller;


import javafx.beans.property.SimpleStringProperty;
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
import kaitech.api.database.MenuItemTable;
import kaitech.api.model.Business;
import kaitech.api.model.MenuItem;
import kaitech.model.BusinessImpl;
import kaitech.util.LambdaValueFactory;

import java.io.IOException;

/**
 * The controller for menu where the user can view and click on each MenuItem in a
 * menu.
 */
public class MenuItemController {

    @FXML
    private TableView<MenuItem> table;

    @FXML
    private TableColumn<MenuItem, String> codeCol;

    @FXML
    private TableColumn<MenuItem, String> nameCol;

    @FXML
    private TableColumn<MenuItem, String> typeCol;

    @FXML
    private TableColumn<MenuItem, String> priceCol;

    @FXML
    private TableColumn<MenuItem, String> stockCol;

    @FXML
    private TableColumn<MenuItem, String> veganCol;

    @FXML
    private TableColumn<MenuItem, String> vegeCol;

    @FXML
    private TableColumn<MenuItem, String> gfCol;

    private Business business;
    private MenuItemTable menuItemTable;

    @FXML
    public void initialize() {
        business = BusinessImpl.getInstance();
        menuItemTable = business.getMenuItemTable();

        codeCol.setCellValueFactory(new LambdaValueFactory<>(MenuItem::getCode));
        nameCol.setCellValueFactory(new LambdaValueFactory<>(MenuItem::getName));
        typeCol.setCellValueFactory(new LambdaValueFactory<>(MenuItem::getType));
        priceCol.setCellValueFactory(new LambdaValueFactory<>(MenuItem::getPrice));
        stockCol.setCellValueFactory(cellData -> new SimpleStringProperty(Integer.toString(cellData.getValue().calculateNumServings(business))));
        veganCol.setCellValueFactory(new LambdaValueFactory<>(MenuItem::getIsVegan));
        vegeCol.setCellValueFactory(new LambdaValueFactory<>(MenuItem::getIsVeg));
        gfCol.setCellValueFactory(new LambdaValueFactory<>(MenuItem::getIsGF));

        table.setItems(FXCollections.observableArrayList(menuItemTable.resolveAllMenuItems().values()));
    }

    /**
     * Removes the selected menu item from the table and refreshes the table.
     */
    public void delete() {
        menuItemTable.removeMenuItem(table.getSelectionModel().getSelectedItem().getCode());
        table.setItems(FXCollections.observableArrayList(menuItemTable.resolveAllMenuItems().values()));
    }

    /**
     * Launches the modify menu item screen. Upon closing the popup, the table is refreshed. The clicked ingredient
     * is passed to the popup via a setter.
     */
    public void modify() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("modifyMenuItem.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.setTitle("Modify Menu Item");
            stage.setScene(new Scene(root));
            stage.show();
            ModifyMenuItemController controller = loader.getController();
            controller.setMenuItem(menuItemTable.getOrAddItem(table.getSelectionModel().getSelectedItem()));
            stage.setOnHiding(paramT -> {
                table.getColumns().get(0).setVisible(false);
                table.getColumns().get(0).setVisible(true);
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void exit(ActionEvent event) {
        try {
            Parent mainMenuParent = FXMLLoader.load(getClass().getResource("MainMenu.fxml"));
            Scene MainMenuScene = new Scene(mainMenuParent);

            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setTitle("Main Menu");
            window.setScene(MainMenuScene);
            window.show();

        } catch (IOException e) {
            System.err.println("Error exiting MenuItem Controller: " + e);
        }
    }
}

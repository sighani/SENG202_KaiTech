package kaitech.controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import kaitech.api.model.Business;
import kaitech.api.model.MenuItem;
import kaitech.model.BusinessImpl;
import org.joda.money.Money;

import java.io.IOException;
import java.util.List;


public class SalesController {

    private Business business;

    @FXML
    private TableView<MenuItem> orderTable;

    @FXML
    private TableColumn<MenuItem, String> nameCol;

    @FXML
    private TableColumn<MenuItem, String> costCol;

    @FXML
    private TableColumn<MenuItem, Button> editCol;

    @FXML
    private TableColumn<MenuItem, Button> removeCol;

    @FXML
    private Button eftposButton;

    @FXML
    private Button prevOrderButton;

    @FXML
    private Button exitButton;

    @FXML
    private Button cancelButton;

    @FXML
    private Button managerTaskButton;

    @FXML
    private Button confirmButton;

    @FXML
    private Button cashButton;


    @FXML
    public void initialize() {
        business = BusinessImpl.getInstance();
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        costCol.setCellValueFactory(cellData -> {
            Money cost = cellData.getValue().getPrice();
            String toShow = "$" + cost.getAmountMajorInt() + "." + cost.getAmountMinorInt();
            return new SimpleStringProperty(toShow);
        });
        editCol.setCellFactory(ActionButtonTableCell_SalesController.forTableColumn("Edit", foodItem -> {
            // You can put whatever logic in here, or even open a new window.
            // For example here we'll just toggle the isGf
            //orderTable.setGlutenFree(!foodItem.isGlutenFree());
            orderTable.refresh(); // Have to trigger a table refresh to make it show up in the table
        }));
        removeCol.setCellFactory(ActionButtonTableCell_SalesController.forTableColumn("Remove", foodItem -> {
            // You can put whatever logic in here, or even open a new window.
            // For example here we'll just toggle the isGf
            //orderTable.setGlutenFree(!foodItem.isGlutenFree());
            orderTable.getItems().remove(foodItem);
            orderTable.refresh(); // Have to trigger a table refresh to make it show up in the table
        }));
        List<MenuItem> foodItems = createTestData(); // This would come from your real data however you access that.
        orderTable.setItems(FXCollections.observableArrayList(foodItems));
    }

    /**
     * Just create some example data.
     *
     * @return Basic example data
     */
    private List<MenuItem> createTestData() {
        return List.of(
//                new MenuItemImpl(),
//                new MenuItemImpl(),
//                new MenuItemImpl()
        );
    }


    /**
     * @param event cancel button pressed
     * @throws IOException print error
     */
    public void exitSalesScreen(ActionEvent event) throws IOException {
        try {
            //When logout button pressed, from home screen
            Parent mainMenuParent = FXMLLoader.load(getClass().getResource("MainMenu.fxml"));
            Scene MainMenuScene = new Scene(mainMenuParent);

            //This line gets the Stage information
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(MainMenuScene);
            window.show();
        } catch (IOException e) {
            throw new IOException("Error in exiting sales screen.");
        }

    }

    /**
     * When the records button is pressed, open the records screen.
     *
     * @param event when the records button on the main menu gets pressed.
     * @throws IOException prints an error message
     */
    public void prevOrder(ActionEvent event) throws IOException {
        try {
            //When sales button pressed, from home screen, get sales scene
            Parent recordsParent = FXMLLoader.load(getClass().getResource("records.fxml"));
            Scene recordsScene = new Scene(recordsParent);

            //Get stage info and switch scenes.
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(recordsScene);
            window.show();
        } catch (IOException e) {
            throw new IOException("Error in opening records screen.");
        }
    }

    /**
     * Method stub to get it running.
     * TODO: IMPLEMENT!!!
     */
    public void eftposPayment() {

    }

    /**
     * Method stub to get it running.
     * TODO: IMPLEMENT!!!
     */
    public void cashPayment() {

    }

    /**
     * Method stub to get it running.
     * TODO: IMPLEMENT!!!
     */
    public void openManagerTasks() {

    }

    /**
     * Method stub to get it running.
     * TODO: IMPLEMENT!!!
     */
    public void confirmOrder() {

    }
}
package kaitech.controller;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import kaitech.api.model.Business;
import kaitech.api.model.MenuItem;
import kaitech.api.model.Sale;
import kaitech.model.BusinessImpl;
import kaitech.util.LambdaValueFactory;
import org.joda.money.format.MoneyFormatter;
import org.joda.money.format.MoneyFormatterBuilder;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class SalesController {

    private Business business;

    private Map<MenuItem, Integer> itemsOrdered = new HashMap<>();

    @FXML
    private TableView<MenuItem> orderTable;

    @FXML
    private TableColumn<MenuItem, String> nameCol;

    @FXML
    private TableColumn<MenuItem, String> costCol;

    @FXML
    private TableColumn<MenuItem, Button> removeCol;

    @FXML
    private TableColumn<MenuItem, Number> quantityCol;

    @FXML
    private GridPane gridPaneItems;

    @FXML
    private Button prevOrderButton;

    @FXML
    private Button exitButton;

    @FXML
    private RadioButton cashRadio;

    @FXML
    private RadioButton eftposRadio;

    @FXML
    private Button cancelButton;

    @FXML
    private Button managerTaskButton;

    @FXML
    private Button confirmButton;

    private Sale currentSale;

    /**
     * A formatter for readable displaying of money.
     */
    private static final MoneyFormatter MONEY_FORMATTER = new MoneyFormatterBuilder() //
            .appendCurrencySymbolLocalized() //
            .appendAmountLocalized() //
            .toFormatter();

    @FXML
    public void initialize() {
        business = BusinessImpl.getInstance();

        nameCol.setCellValueFactory(new LambdaValueFactory<>(MenuItem::getName));
        costCol.setCellValueFactory(new LambdaValueFactory<>(e -> MONEY_FORMATTER.print(e.getPrice().multipliedBy(itemsOrdered.get(e)))));
        quantityCol.setCellValueFactory(cellData -> new SimpleIntegerProperty((itemsOrdered.get(cellData.getValue()))));
        removeCol.setCellFactory(ActionButtonTableCell_SalesController.forTableColumn("X", foodItem -> {
            // You can put whatever logic in here, or even open a new window.
            if (itemsOrdered.get(foodItem) == 1) {
                orderTable.getItems().remove(foodItem);
                itemsOrdered.remove(foodItem);
                orderTable.refresh(); // Have to trigger a table refresh to make it show up in the table
            } else {
                itemsOrdered.put(foodItem, itemsOrdered.get(foodItem) - 1);
                orderTable.refresh();
            }
        }));
        orderTable.setItems(FXCollections.observableArrayList(itemsOrdered.keySet()));

        int rowIndex = 0;
        int colIndex = 0;

        for (String mICode : business.getMenuItemTable().getAllIMenuItemCodes()) {
            Button tempButton = new Button(business.getMenuItemTable().getMenuItem(mICode).getName());
            tempButton.setPrefSize(121, 71);
            tempButton.setOnAction(actionEvent -> addToSale(business.getMenuItemTable().getMenuItem(mICode)));
            gridPaneItems.add(tempButton, colIndex, rowIndex);

            if (colIndex == 3 && rowIndex == 5) {
                // we've maxed out the bloody table
                break;
            }

            if (colIndex == 3) {
                colIndex = 0;
                rowIndex++;
            } else {
                colIndex++;
            }
        }
    }

    public void addToSale(MenuItem menuItem) {
        if (itemsOrdered.containsKey(menuItem)) {
            itemsOrdered.put(menuItem, itemsOrdered.get(menuItem) + 1);
            orderTable.refresh();
        } else {
            itemsOrdered.put(menuItem, 1);
        }
        orderTable.setItems(FXCollections.observableArrayList(itemsOrdered.keySet()));
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

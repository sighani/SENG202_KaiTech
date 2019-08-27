package kaitech.controller;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import kaitech.model.Business;
import kaitech.model.MenuItem;
import kaitech.model.Sale;
import kaitech.util.PaymentType;

//import java.awt.event.ActionEvent;
import javafx.event.ActionEvent;
import org.joda.money.Money;

import javax.swing.*;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * This is the class for the sales controller, it takes information from the GUI, and sends it to the model, which will
 * process the information accordingly and then it, sends the processed information back to the GUI to be displayed.
 */
public class SalesController {

    /**
     * A map from each menu item to their quantities in the sale as integers.
     */
    private Map<MenuItem, Integer> itemsSelected;

    /**
     * An instance of the sales class, where information from the sales screen will be sent, and then the calculated
     * information will be sent back to the sales screen
     */
    private Sale theSale = new Sale();

    /**
     * An instance of the sales screen, where information from the user will be gathered from, this information will be
     * sent to the sales model class, be processed, and then sent back to the sale screen to be displayed to the user.
     */
    private SalesScreen theSalesScreen = new SalesScreen();

    /**
     * An instance of the Business class, this class is called when we want to add information to the lists stored in
     * the Business class.
     */
    private Business theBusiness = new Business();

    /*public SalesController(Sale theSale, SalesScreen theSalesScreen, Business theBusiness, Map<MenuItem, Integer> itemsSelected){

        this.theSale = theSale;
        this.theSalesScreen = theSalesScreen;
        this.theBusiness = theBusiness;
        itemsSelected = itemsSelected;

    }*/




    /**
     * This method gets called when the createRecord button is pushed in the View. It takes the relevant information,
     * from the relevant fields in the View and then creates a new Record. It then checks if the user clicked confirm
     * or deny if they did the record is added, otherwise it is not and the information is discarded.
     */

    @FXML
    public void createRecord(ActionEvent event) {

        LocalDate date = null;
        LocalTime time = null;
        String notes = "";
        Map<MenuItem, Integer> itemsOrdered;

        date = theSalesScreen.getDate();
        time = theSalesScreen.getTime();
        PaymentType paymentType = theSalesScreen.getPaymentType();
        notes = theSalesScreen.getNotes();
        Money totalPrice = theSalesScreen.getTotalPrice();
        itemsOrdered = theSalesScreen.getItemsOrdered();

        Sale newRecord = new Sale(itemsOrdered, date, time, paymentType, notes, totalPrice, theBusiness);

        String confirmValue = ((Button)event.getSource()).getText();

        if(confirmValue.equals("Confirm")) {
            theBusiness.addRecord(newRecord);
        }
        else {
            // here probably set a label to something like "Cancel clicked, Ingredient not added.
        }




    }

    public void itemSelected(MenuItem item) {

    }

    /**
     * Calculates the total price of the sale.
     */

    @FXML
    public void calculateTotal(ActionEvent event) {
        int totalPrice = Sale.calculateTotalCost(theSalesScreen.getOrder());

    }

    /**
     * Takes the input of the item to be added, then calls the increaseAmount method in the Sale class, which will
     * increment the amount of the sale.
     * @param item
     */

    @FXML
    public void incrementAmount(MenuItem item) {
       theSale.increaseAmount(item);


    }

    /**
     * Takes the input of the item to be removed, then calls the decreaseAmount method in the Sale class, which will
     * decrement the amount of the sale.
     * @param item
     */

    @FXML
    public void decrementAmount(MenuItem item) {
        theSale.decreaseAmount(item);

    }

    /**
     * Confirms the order, telling the system to store the information, and send the order to the current order screen.
     */
    public void confirmOrder() {

    }

    /**
     * Creates a new order, which items can be added to, and then can be confirmed or cancelled.
     */
    public void newOrder() {

    }

    public void cancelOrder() {


    }

    /**
     * Changes the currently displayed scene to the main menu.
     *
     * @param event Indicates the event which occurred, which caused the method to be called.
     */
    @FXML
    public void returnToMain(ActionEvent event) {
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stage.setScene(mainMenuScene);

    }


}

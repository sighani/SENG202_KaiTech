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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import kaitech.api.model.Business;
import kaitech.api.model.MenuItem;
import kaitech.api.model.Sale;
import kaitech.model.BusinessImpl;
import kaitech.model.SaleImpl;
import kaitech.util.PaymentType;
import org.joda.money.Money;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

/**
 * The controller for the records screen, which shows a list of sales records, which
 * may span over several pages. Sales may be edited provided the user has logged in.
 */
public class RecordsController {
    @FXML
    private TableView<Sale> table;

    @FXML
    private TableColumn<Sale, String> dateCol;

    @FXML
    private TableColumn<Sale, String> timeCol;

    @FXML
    private TableColumn<Sale, String> paymentTypeCol;

    @FXML
    private TableColumn<Sale, String> notesCol;

    @FXML
    private TableColumn<Sale, String> priceCol;

    private Business business;

    @FXML
    public void initialize() {
        Map<MenuItem, Integer> menuItems = new HashMap<MenuItem, Integer>();

        business = BusinessImpl.getInstance();
        LocalDate date = java.time.LocalDate.now();
        LocalTime time = java.time.LocalTime.now();
        Money totalPrice = Money.parse("NZD 20");
        Money totalPrice2 = Money.parse("NZD 40");
        Sale newSale = new SaleImpl(menuItems, date, time, PaymentType.CASH, "Twenty", totalPrice, business);
        Sale newSale1 = new SaleImpl(menuItems, date, time, PaymentType.CASH, "Fifty", totalPrice2, business);
        Sale newSale2 = new SaleImpl(menuItems, date, time, PaymentType.CASH, "Twenty", totalPrice, business);
        Sale newSale3 = new SaleImpl(menuItems, date, time, PaymentType.CHEQUE, "None", totalPrice2, business);
        Sale newSale4 = new SaleImpl(menuItems, date, time, PaymentType.SAVINGS, "None", totalPrice, business);
        Sale newSale5 = new SaleImpl(menuItems, date, time, PaymentType.CREDIT, "None", totalPrice, business);
        Sale newSale6 = new SaleImpl(menuItems, date, time, PaymentType.CASH, "Fifty", totalPrice2, business);
        Sale newSale7 = new SaleImpl(menuItems, date, time, PaymentType.CASH, "Twenty", totalPrice2, business);
        Sale newSale8 = new SaleImpl(menuItems, date, time, PaymentType.CASH, "Twenty", totalPrice, business);
        Sale newSale9 = new SaleImpl(menuItems, date, time, PaymentType.CASH, "Twenty", totalPrice, business);
        Sale newSale10 = new SaleImpl(menuItems, date, time, PaymentType.CASH, "Twenty", totalPrice, business);

        dateCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDate()));
        timeCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTime()));
        paymentTypeCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPaymentType().toString()));
        notesCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNotes()));
        priceCol.setCellValueFactory(cellData -> {
            Money cost = cellData.getValue().getPrice();
            String toShow = "$" + cost.getAmountMajorInt() + "." + cost.getAmountMinorInt();
            return new SimpleStringProperty(toShow);
        });

        table.setItems(FXCollections.observableArrayList(business.getRecords()));
    }
    /**
     * Adjusts the details of a sale, used if a sale was initially input incorrectly.
     * @param event the event which caused this method ot be called.
     */


    public void adjustDetails(ActionEvent event) throws IOException{
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("modifyRecord.fxml"));
            Parent root = loader.load();
            ModifyRecordController controller = loader.<ModifyRecordController>getController();
            controller.setRecord(table.getSelectionModel().getSelectedItem());
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.setTitle("Modify Record details");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    /**
     *Confirms the details of the given sale and stores the information in the system.
     * @param sale The sale which is being confirmed.
     */
    public void confirmChanges(SaleImpl sale) {

                /*
        String name = supName.getText();
        String address = supAddress.getText();
        String type = supNumType.getValue();
        String number = supNumber.getText();
        String email = supEmail.getText();
        String url = supURL.getText();


        // supID = supID.getText();
        // Supplier supToEdit = buisness.getSupplier(supID);
        // supToEdit.setPhoneType(type);
        // supToEdit.setEmail(email);
        // supToEdit.setURL(url);

        System.out.println("Updated supplier information:");
        System.out.println("Name: " + name);
        System.out.println("Address: " + address);
        System.out.println("Number Type: " + type);
        System.out.println("Phone Number: " + number);
        System.out.println("Email: " + email);
        System.out.println("URL: " + url);
        manualUploadText.setText("Supplier: " + name + ", has been edited.  ");
        manualUploadText.setVisible(true);*/

    }

    /**
     * Deletes the record of the chosen sale.
     *
     * @param event when the deleteRecord button is pressed.
     */
    public void deleteRecord(ActionEvent event) {
        business.getRecords().remove(table.getSelectionModel().getSelectedItem());
        table.setItems(FXCollections.observableArrayList(business.getRecords()));


    }

    /**
     * Takes the user to the next page.
     */
    public void nextPage() {

    }

    /**
     * Takes the user to the previous page.
     */
    public void previousPage() {
    }

    /**
     * Changes the currently displayed scene to the main menu.
     *
     * @param event Indicates the event which occurred, which caused the method to be called.
     */
    @FXML
    public void returnToMain(ActionEvent event) throws IOException {
        try {
            Parent mainMenuParent = FXMLLoader.load(getClass().getResource("MainMenu.fxml"));
            Scene MainMenuScene = new Scene(mainMenuParent);

            Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
            window.setScene(MainMenuScene);
            window.show();

        } catch (IOException e) {
            throw new IOException("Error in exiting manual input.");

        }


    }

}

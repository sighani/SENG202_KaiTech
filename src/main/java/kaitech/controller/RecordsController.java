package kaitech.controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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
import javafx.stage.WindowEvent;
import kaitech.api.database.SaleTable;
import kaitech.api.model.*;
import kaitech.model.BusinessImpl;
import kaitech.model.MenuItemImpl;
import kaitech.model.RecipeImpl;
import kaitech.model.SaleImpl;
import kaitech.util.MenuItemType;
import kaitech.util.PaymentType;
import org.joda.money.Money;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

    @FXML
    private TableColumn<Sale, String> ingredientsCol;
    @FXML
    private TableColumn<Sale, String> receiptNoCol;

    private Business business;

    private SaleTable recordsTable;

    @FXML
    public void initialize() {
        business = BusinessImpl.getInstance();
        Map<MenuItem, Integer> menuItems = new HashMap<>();
       /* Map<MenuItem, Integer> menuItems2 = new HashMap<>();
        Map<Ingredient, Integer> ingredientsMap = new HashMap<>();
        List<String> ingredients = new ArrayList<>();
        Recipe newRecipe = new RecipeImpl(ingredientsMap, 12, 14, 2);
        Money menuItemPrice = Money.parse("NZD 10.00");
        MenuItem menuItem1 = new MenuItemImpl("1234", "Cheese", ingredients, newRecipe, menuItemPrice, MenuItemType.MISC);
        MenuItem menuItem2 = new MenuItemImpl("1234", "Burger", ingredients, newRecipe, menuItemPrice, MenuItemType.MISC);
        MenuItem menuItem3 = new MenuItemImpl("1234", "Drink", ingredients, newRecipe, menuItemPrice, MenuItemType.MISC);
        MenuItem menuItem4 = new MenuItemImpl("1234", "Sandwich", ingredients, newRecipe, menuItemPrice, MenuItemType.MISC);
        MenuItem menuItem5 = new MenuItemImpl("1234", "Chips", ingredients, newRecipe, menuItemPrice, MenuItemType.MISC);
        //menuItems.put()

        menuItems.put(menuItem1, 1);
        menuItems.put(menuItem2, 1);
        menuItems2.put(menuItem3, 1);
        menuItems2.put(menuItem4, 1); */

        recordsTable = business.getSaleTable();
        //if(recordsTable.isEmpty() == false) {
        LocalDate date = java.time.LocalDate.now();
        LocalTime time = java.time.LocalTime.now();
        Money totalPrice = Money.parse("NZD 20.00");
        Money totalPrice2 = Money.parse("NZD 40.10");
        Sale newSale = new SaleImpl(date, time, totalPrice, PaymentType.CASH, "Twenty", menuItems);
        Sale newSale1 = new SaleImpl(date, time, totalPrice2, PaymentType.CASH, "Fifty", menuItems);
        Sale newSale2 = new SaleImpl(date, time, totalPrice, PaymentType.CREDIT, "None", menuItems);
        Sale newSale3 = new SaleImpl(date, time, totalPrice2, PaymentType.SAVINGS, "None", menuItems);
        Sale newSale4 = new SaleImpl(date, time, totalPrice, PaymentType.CHEQUE, "None", menuItems);
        //}

        recordsTable.putSale(newSale);
        recordsTable.putSale(newSale1);
        recordsTable.putSale(newSale2);
        recordsTable.putSale(newSale3);
        recordsTable.putSale(newSale4);

        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        receiptNoCol.setCellValueFactory(cellData -> new SimpleStringProperty(Integer.toString(cellData.getValue().getReceiptNumber())));
        dateCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDate().toString()));
        timeCol.setCellValueFactory(cellData -> new SimpleStringProperty(timeFormatter.format(cellData.getValue().getTime())));
        paymentTypeCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPaymentType().toString()));
        notesCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNotes()));
        priceCol.setCellValueFactory(cellData -> {
            Money cost = cellData.getValue().getTotalPrice();
            String toShow = "$" + cost.getAmountMajorInt() + "." + String.format("%02d", cost.getAmountMinorInt() - cost.getAmountMajorInt() * 100);
            return new SimpleStringProperty(toShow);
        });

        table.setItems(FXCollections.observableArrayList(business.getSaleTable().resolveAllSales().values()));
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
            stage.setOnHiding(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent paramT) {
                    table.getColumns().get(0).setVisible(false);
                    table.getColumns().get(0).setVisible(true);
                }
            });
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
        recordsTable.removeSale(table.getSelectionModel().getSelectedItem().getReceiptNumber());
        table.setItems(FXCollections.observableArrayList(business.getSaleTable().resolveAllSales().values()));


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

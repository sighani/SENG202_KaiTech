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
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import kaitech.api.database.SaleTable;
import kaitech.api.model.*;
import kaitech.model.BusinessImpl;
import kaitech.model.MenuItemImpl;
import kaitech.model.RecipeImpl;
import kaitech.model.SaleImpl;
import kaitech.util.LambdaValueFactory;
import kaitech.util.MenuItemType;
import kaitech.util.PaymentType;
import org.joda.money.Money;
import org.joda.money.format.MoneyFormatter;
import org.joda.money.format.MoneyFormatterBuilder;

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
    @FXML
    private Text responseText;

    private Business business;

    private SaleTable recordsTable;

    /**
     * A formatter for readable displaying of money.
     */
    private static final MoneyFormatter MONEY_FORMATTER = new MoneyFormatterBuilder().appendCurrencySymbolLocalized().appendAmountLocalized().toFormatter();

    @FXML
    public void initialize() {
        business = BusinessImpl.getInstance();
        Map<MenuItem, Integer> menuItems = new HashMap<>();
        Map<MenuItem, Integer> menuItems2 = new HashMap<>();
        Map<Ingredient, Integer> ingredientsMap = new HashMap<>();
        List<String> ingredients = new ArrayList<>();
        Recipe newRecipe = new RecipeImpl(12, 14, 2, ingredientsMap);
        Money menuItemPrice = Money.parse("NZD 10.00");
        MenuItem menuItem1 = new MenuItemImpl("1234", "Cheese", menuItemPrice, newRecipe, MenuItemType.MISC, ingredients);
        MenuItem menuItem2 = new MenuItemImpl("4", "Burger", menuItemPrice, newRecipe, MenuItemType.MISC, ingredients);
        //menuItems.put()

        menuItems.put(menuItem1, 1);
        menuItems.put(menuItem2, 1);

        recordsTable = business.getSaleTable();
        //if(recordsTable.isEmpty() == false) {
        LocalDate date = java.time.LocalDate.now();
        LocalTime time = java.time.LocalTime.now();
        Money totalPrice = Money.parse("NZD 20.00");
        Money totalPrice2 = Money.parse("NZD 40.10");
        Sale newSale = new SaleImpl(date, time, totalPrice, PaymentType.CASH, "Good Sale", menuItems);
        Sale newSale1 = new SaleImpl(date, time, totalPrice2, PaymentType.CASH, "Customer wants order in 30 minutes.", menuItems);
        Sale newSale2 = new SaleImpl(date, time, totalPrice, PaymentType.CREDIT, "No special notes.", menuItems);
        Sale newSale3 = new SaleImpl(date, time, totalPrice2, PaymentType.SAVINGS, "", menuItems);
        Sale newSale4 = new SaleImpl(date, time, totalPrice, PaymentType.CHEQUE, "", menuItems);
        //}

        recordsTable.putSale(newSale);
        recordsTable.putSale(newSale1);
        recordsTable.putSale(newSale2);
        recordsTable.putSale(newSale3);
        recordsTable.putSale(newSale4);

        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        receiptNoCol.setCellValueFactory(new LambdaValueFactory<>(Sale::getReceiptNumber));
        dateCol.setCellValueFactory(new LambdaValueFactory<>(Sale::getDate));
        timeCol.setCellValueFactory(new LambdaValueFactory<>(Sale::getTime));
        paymentTypeCol.setCellValueFactory(new LambdaValueFactory<>(Sale::getPaymentType));
        notesCol.setCellValueFactory(new LambdaValueFactory<>(Sale::getNotes));
        priceCol.setCellValueFactory(new LambdaValueFactory<>(cellData -> MONEY_FORMATTER.print(cellData.getTotalPrice())));
        ingredientsCol.setCellValueFactory(cellData -> {
            StringBuilder ingredientsString = new StringBuilder();
            for (Map.Entry<MenuItem, Integer> entry : cellData.getValue().getItemsOrdered().entrySet()) {
                ingredientsString.append(entry.getKey().getName()).append(": ").append(entry.getValue()).append(", ");
            }
            if(ingredientsString.length() > 0){
                ingredientsString.deleteCharAt((ingredientsString.length()-1));
                ingredientsString.deleteCharAt((ingredientsString.length()-1));
            }
            return new SimpleStringProperty(ingredientsString.toString());
        });

        table.setItems(FXCollections.observableArrayList(business.getSaleTable().resolveAllSales().values()));
    }
    /**
     * Adjusts the details of a sale, used if a sale was initially input incorrectly.
     * @param event the event which caused this method ot be called.
     */


    public void adjustDetails(ActionEvent event) throws IOException{
        try {
            if (!business.isLoggedIn()) {
                LogInController l = new LogInController();
                l.showScreen("modifyRecord.fxml");
            }else {
                if (table.getSelectionModel().getSelectedItem() == null) {
                    responseText.setText("You haven't selected a record.");
                    responseText.setVisible(true);

                } else {
                    responseText.setVisible(false);
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
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }




    /**
     * Deletes the record of the chosen sale.
     *
     * @param event when the deleteRecord button is pressed.
     */
    public void deleteRecord(ActionEvent event) {
        if (!business.isLoggedIn()) {
            LogInController l = new LogInController();
            l.showScreen(null);
        }else {
            if (table.getSelectionModel().getSelectedItem() == null) {
                responseText.setText("You haven't selected a record.");
                responseText.setVisible(true);

            } else {
                recordsTable.removeSale(table.getSelectionModel().getSelectedItem().getReceiptNumber());
                table.setItems(FXCollections.observableArrayList(business.getSaleTable().resolveAllSales().values()));
                responseText.setText("Record deleted.");
                responseText.setVisible(true);
            }
        }

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
            window.setTitle("Main Menu");
            window.setScene(MainMenuScene);
            window.show();

        } catch (IOException e) {
            throw new IOException("Error in exiting manual input.");

        }


    }

}

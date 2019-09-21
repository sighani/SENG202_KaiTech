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
import kaitech.api.database.SaleTable;
import kaitech.api.model.Business;
import kaitech.api.model.MenuItem;
import kaitech.api.model.Sale;
import kaitech.model.BusinessImpl;
import kaitech.util.LambdaValueFactory;
import org.joda.money.format.MoneyFormatter;
import org.joda.money.format.MoneyFormatterBuilder;

import java.io.IOException;
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
        recordsTable = business.getSaleTable();

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
            if (ingredientsString.length() > 0) {
                ingredientsString.deleteCharAt((ingredientsString.length() - 1));
                ingredientsString.deleteCharAt((ingredientsString.length() - 1));
            }
            return new SimpleStringProperty(ingredientsString.toString());
        });

        table.setItems(FXCollections.observableArrayList(business.getSaleTable().resolveAllSales().values()));
    }

    /**
     * Adjusts the details of a sale, used if a sale was initially input incorrectly.
     *
     * @param event the event which caused this method ot be called.
     */
    public void adjustDetails(ActionEvent event) {
        try {
            if (!business.isLoggedIn()) {
                LogInController l = new LogInController();
                l.showScreen("modifyRecord.fxml");
            } else {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("modifyRecord.fxml"));
                Parent root = loader.load();
                ModifyRecordController controller = loader.<ModifyRecordController>getController();
                controller.setRecord(recordsTable.getSale(table.getSelectionModel().getSelectedItem().getReceiptNumber()));
                Stage stage = new Stage();
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.setResizable(false);
                stage.setTitle("Modify Record details");
                stage.setScene(new Scene(root));
                stage.show();
                stage.setOnHiding(paramT -> {
                    table.getColumns().get(0).setVisible(false);
                    table.getColumns().get(0).setVisible(true);
                });
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
        } else {
            recordsTable.removeSale(table.getSelectionModel().getSelectedItem().getReceiptNumber());
            table.setItems(FXCollections.observableArrayList(business.getSaleTable().resolveAllSales().values()));
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

            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setTitle("Main Menu");
            window.setScene(MainMenuScene);
            window.show();
        } catch (IOException e) {
            throw new IOException("Error in exiting manual input.");
        }
    }
}

package kaitech.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import kaitech.api.database.LoyaltyCardTable;
import kaitech.api.model.Business;
import kaitech.api.model.LoyaltyCard;
import kaitech.model.BusinessImpl;
import kaitech.util.LambdaValueFactory;
import org.joda.money.format.MoneyFormatter;
import org.joda.money.format.MoneyFormatterBuilder;

public class LoyaltyCardViewController {

    @FXML
    private TableView<LoyaltyCard> cardDisplayTable;

    /**
     * Loyalty card table columns
     */
    @FXML
    private TableColumn<LoyaltyCard, String> idCardCol;
    @FXML
    private TableColumn<LoyaltyCard, String> nameCardCol;
    @FXML
    private TableColumn<LoyaltyCard, String> dateCardCol;
    @FXML
    private TableColumn<LoyaltyCard, String> balanceCardCol;

    @FXML
    private LoyaltyCardTable loyaltyCardTable;

    private Business business;

    /**
     * A formatter for readable displaying of money.
     */
    private static final MoneyFormatter MONEY_FORMATTER = new MoneyFormatterBuilder() //
            .appendCurrencySymbolLocalized() //
            .appendAmountLocalized() //
            .toFormatter();

    public void initialize() {
        business = BusinessImpl.getInstance();
        loyaltyCardTable = business.getLoyaltyCardTable();
        idCardCol.setCellValueFactory(new LambdaValueFactory<>(LoyaltyCard::getId));
        nameCardCol.setCellValueFactory(new LambdaValueFactory<>(LoyaltyCard::getCustomerName));
        dateCardCol.setCellValueFactory(new LambdaValueFactory<>(LoyaltyCard::getLastPurchase));
        balanceCardCol.setCellValueFactory(new LambdaValueFactory<>(e -> MONEY_FORMATTER.print(e.getBalance())));
        cardDisplayTable.setItems(FXCollections.observableArrayList(loyaltyCardTable.resolveAllLoyaltyCards().values()));
        cardDisplayTable.setVisible(true);
    }

    /**
     * Removes the selected loyalty card from the table and refreshes the table.
     */
    public void delete() {
        if (cardDisplayTable.getSelectionModel().getSelectedItem() != null) {
            if (!business.isLoggedIn()) {
                LogInController l = new LogInController();
                l.showScreen();
            } else {
                loyaltyCardTable.removeLoyaltyCard(cardDisplayTable.getSelectionModel().getSelectedItem().getId());
                cardDisplayTable.setItems(FXCollections.observableArrayList(loyaltyCardTable.resolveAllLoyaltyCards().values()));
            }
        }
    }

    public void back() {
        Stage stage = (Stage) cardDisplayTable.getScene().getWindow();
        stage.close();
    }
}

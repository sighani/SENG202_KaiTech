package kaitech.controller;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import kaitech.api.model.MenuItem;
import kaitech.api.model.*;
import kaitech.model.BusinessImpl;
import kaitech.model.SaleImpl;
import kaitech.util.LambdaValueFactory;
import kaitech.util.PaymentType;
import org.joda.money.Money;
import org.joda.money.format.MoneyFormatter;
import org.joda.money.format.MoneyFormatterBuilder;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


public class SalesController {

    /**
     * Check box for using loyalty card balance
     */
    @FXML
    private CheckBox ckBoxUseBalance;

    /**
     * Text feild for Loyalty card number
     */
    @FXML
    private TextField txtboxLoyaltyCard;

    /**
     * Label for showing the balance of the card
     */
    @FXML
    private Label lblCardBalance;

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
    private Button cancelButton;

    @FXML
    private RadioButton cashRadio;

    @FXML
    private RadioButton eftposRadio;

    @FXML
    private Button managerTaskButton;

    @FXML
    private Label totalCostLabel;

    @FXML
    private Button confirmButton;

    @FXML
    private ToggleGroup saleType;

    @FXML
    private Dialog actionStatus;

    @FXML
    private Label lblErr;

    private Business business;

    private Map<MenuItem, Integer> itemsOrdered = new HashMap<>();

    private Map<Ingredient, Integer> tempInventory;

    private Money totalPrice;

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
        totalPrice = Money.parse("NZD 0.00");
        nameCol.setCellValueFactory(new LambdaValueFactory<>(MenuItem::getName));
        costCol.setCellValueFactory(new LambdaValueFactory<>(e -> MONEY_FORMATTER.print(e.getPrice()
                .multipliedBy(itemsOrdered.get(e)))));
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
            totalPrice = totalPrice.minus(foodItem.getPrice());
            totalCostLabel.setText(MONEY_FORMATTER.print(totalPrice));
            updateTempInventory(foodItem, false);
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

        cashRadio.setToggleGroup(saleType);
        eftposRadio.setToggleGroup(saleType);

        tempInventory = business.getInventoryTable().resolveInventory();

        ckBoxUseBalance.setSelected(false);
        lblCardBalance.setText(null);
        txtboxLoyaltyCard.setText(null);
    }

    /**
     * Updates the temporary inventory with the addition or removal of the ingredients required for a given menu item.
     *
     * @param menuItem The menu item whose ingredients should be removed from the inventory
     * @param removing A boolean indicating whether the menu item's ingredients are being removed from the inventory,
     *                 or added back to the inventory.
     */
    private void updateTempInventory(MenuItem menuItem, boolean removing) {
        if (menuItem.getRecipe() != null) {
            for (Ingredient ingredient : menuItem.getRecipe().getIngredients().keySet()) {
                if (removing) {
                    if (tempInventory.get(ingredient) - menuItem.getRecipe().getIngredients().get(ingredient) < 0) {
                        //we cant make this item
                        lblErr.setVisible(true);
                    } else {
                        tempInventory.replace(ingredient, tempInventory.get(ingredient)
                                - menuItem.getRecipe().getIngredients().get(ingredient));
                    }
                } else {
                    tempInventory.replace(ingredient, tempInventory.get(ingredient)
                            + menuItem.getRecipe().getIngredients().get(ingredient));
                }
            }
        }
    }

    /**
     * Checks the balance of the selected card
     */
    public void checkBalance() {
        //check that the text field is not null
        if (!txtboxLoyaltyCard.getText().isEmpty()) {
            LoyaltyCard tempLoyaltyCard = business.getLoyaltyCardTable()
                    .getLoyaltyCard(Integer.parseInt(txtboxLoyaltyCard.getText()));
            lblCardBalance.setText(MONEY_FORMATTER.print(tempLoyaltyCard.getBalance()));
        }
    }


    /**
     * Adds a menu Item to sale
     *
     * @param menuItem The menu item to add to the sale
     */
    public void addToSale(MenuItem menuItem) {
        lblErr.setVisible(false);
        updateTempInventory(menuItem, true);

        if (!lblErr.isVisible()) {

            if (itemsOrdered.containsKey(menuItem)) {
                itemsOrdered.put(menuItem, itemsOrdered.get(menuItem) + 1);
                orderTable.refresh();
            } else {
                itemsOrdered.put(menuItem, 1);
            }
            totalPrice = totalPrice.plus(menuItem.getPrice());
            orderTable.setItems(FXCollections.observableArrayList(itemsOrdered.keySet()));

            if (ckBoxUseBalance.isSelected()) {
                //reduce the price by 10 percent
                //currently only visual and it changes it at purchase/when the ck box is deselected
                //checking if it goes below 0, if yes just set the price to 0
                if (totalPrice.minus(business.getLoyaltyCardTable()
                        .getLoyaltyCard(Integer.parseInt(txtboxLoyaltyCard.getText())).getBalance()).isNegative()) {
                    totalCostLabel.setText(MONEY_FORMATTER.print(Money.parse("NZD 0.00")));
                } else {
                    totalCostLabel.setText(MONEY_FORMATTER.print(totalPrice.minus(business.getLoyaltyCardTable()
                            .getLoyaltyCard(Integer.parseInt(txtboxLoyaltyCard.getText())).getBalance())));
                }
            } else {
                totalCostLabel.setText(MONEY_FORMATTER.print(totalPrice));
            }
        }
    }

    /**
     * Checks if the check box is selected, if it is it changes total cost to cost - discount, if not it just sets it
     * to total cost
     */
    public void updateCostLoyaltyCard() {
        if (ckBoxUseBalance.isSelected()) {
            if (totalPrice.minus(business.getLoyaltyCardTable()
                    .getLoyaltyCard(Integer.parseInt(txtboxLoyaltyCard.getText())).getBalance()).isNegative()) {
                totalCostLabel.setText(MONEY_FORMATTER.print(Money.parse("NZD 0.00")));
            } else {
                totalCostLabel.setText(MONEY_FORMATTER.print(totalPrice.minus(business.getLoyaltyCardTable()
                        .getLoyaltyCard(Integer.parseInt(txtboxLoyaltyCard.getText())).getBalance())));
            }
        } else {
            totalCostLabel.setText(MONEY_FORMATTER.print(totalPrice));
        }
    }


    /**
     * @param event cancel button pressed
     * @throws IOException print error
     */
    public void exitSalesScreen(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.WARNING, "Are you sure you would like to return to the main menu?",
                ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.YES) {
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

    public void cancelOrder() {
        Alert alert = new Alert(Alert.AlertType.WARNING, "Are you sure you would like to cancel this order?",
                ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.YES) {
            //now we need to clean up
            itemsOrdered.entrySet().clear();
            orderTable.getItems().clear();
            orderTable.refresh();

            saleType.selectToggle(eftposRadio);
            totalPrice = Money.parse("NZD 0.00");
            totalCostLabel.setText(MONEY_FORMATTER.print(totalPrice));
            tempInventory = business.getInventoryTable().resolveInventory();
            lblErr.setVisible(false);
            ckBoxUseBalance.setSelected(false);
            lblCardBalance.setText(null);
            txtboxLoyaltyCard.setText(null);
        }
    }

    /**
     * Takes the ordered menuItems generates a sales object
     */
    public void confirmOrder() {
        if (itemsOrdered.isEmpty()) {
            Alert alert = new Alert(AlertType.ERROR, "There are no items in the current order", ButtonType.CLOSE);
            alert.showAndWait();
        } else {
            LocalDate localDate = java.time.LocalDate.now();
            LocalTime localTime = java.time.LocalTime.now();

            Map<MenuItem, Integer> itemsInOrder = new HashMap<>();

            //for getting total time and total ordered items
            for (MenuItem menuItem : itemsOrdered.keySet()) {
                itemsInOrder.put(menuItem, itemsOrdered.get(menuItem));

            }

            PaymentType p;
            if (saleType.getSelectedToggle().equals(cashRadio)) {
                p = PaymentType.CASH;
            } else if (saleType.getSelectedToggle().equals(eftposRadio)) {
                p = PaymentType.EFTPOS;
            } else {
                p = PaymentType.UNKNOWN;
            }

            if (!txtboxLoyaltyCard.getText().isEmpty() && ckBoxUseBalance.isSelected()) {
                //if there is a loyalty card number in the box, and use balance is selected we reduce price
                totalPrice = business.getLoyaltyCardTable()
                        .getLoyaltyCard(Integer.parseInt(txtboxLoyaltyCard.getText())).spendPoints(totalPrice);
            } else if (!txtboxLoyaltyCard.getText().isEmpty()) {
                //we add points to the card
                business.getLoyaltyCardTable()
                        .getLoyaltyCard(Integer.parseInt(txtboxLoyaltyCard.getText())).addPoints(totalPrice);
            }

            //generating new sales object
            Sale sale = new SaleImpl(localDate, localTime, totalPrice, p, "", itemsInOrder);
            business.getSaleTable().putSale(sale);

            //now we need to clean up
            itemsOrdered.entrySet().clear();
            orderTable.getItems().clear();
            orderTable.refresh();

            saleType.selectToggle(eftposRadio);
            totalPrice = Money.parse("NZD 0.00");
            totalCostLabel.setText(MONEY_FORMATTER.print(totalPrice));
            tempInventory = business.getInventoryTable().resolveInventory();
            lblErr.setVisible(false);

            ckBoxUseBalance.setSelected(false);
            lblCardBalance.setText(null);
            txtboxLoyaltyCard.setText(null);
        }
    }
}

package kaitech.controller;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import kaitech.api.database.MenuItemTable;
import kaitech.api.model.Business;
import kaitech.api.model.MenuItem;
import kaitech.model.BusinessImpl;
import kaitech.util.MenuItemType;
import org.joda.money.Money;
import org.joda.money.format.MoneyFormatter;
import org.joda.money.format.MoneyFormatterBuilder;

public class ModifyMenuItemController {

    @FXML
    private Text menuItemCode;

    @FXML
    private TextField menuItemName;

    @FXML
    private TextField menuItemPrice;

    @FXML
    private ComboBox<MenuItemType> menuItemType;

    @FXML
    private Text responseText;

    @FXML
    private Text titleText;


    /**
     * The Menu Item that is being modified.
     */
    private MenuItem menuItem;

    private Business business;

    private MenuItemTable menuItemTable;

    /**
     * A formatter for readable displaying of money.
     */
    private static final MoneyFormatter MONEY_FORMATTER = new MoneyFormatterBuilder().appendAmountLocalized().toFormatter();

    /**
     * Sets the menu item and calls the start method. This is used as an alternative to an initialize method as the
     * supplier must be obtained as a parameter.
     *
     * @param menuItem The menu item to be modified.
     */
    public void setMenuItem(MenuItem menuItem) {
        this.menuItem = menuItem;
        start();
    }

    public void start() {
        titleText.setText("Now modifying Menu Item: " + menuItem.getCode());
        business = BusinessImpl.getInstance();

        menuItemTable = business.getMenuItemTable();
        menuItemCode.setText(menuItem.getCode());
        menuItemName.setText(menuItem.getName());
        menuItemPrice.setText(MONEY_FORMATTER.print(menuItem.getPrice()));
        menuItemType.getItems().setAll(MenuItemType.values());
        menuItemType.getSelectionModel().select(menuItem.getType());
    }

    /**
     * Item is modified and close scene, return to menu item list
     */
    public void confirm() {
        if (fieldsAreValid()) {
            menuItem.setName(menuItemName.getText());
            menuItem.setPrice(Money.parse("NZD " + menuItemPrice.getText()));
            menuItem.setType(menuItemType.getValue());

            Stage stage = (Stage) titleText.getScene().getWindow();
            stage.close();
        } else {
            responseText.setVisible(true);
        }
    }

    /**
     * Exit without modifying menu item
     */
    public void exit() {
        Stage stage = (Stage) titleText.getScene().getWindow();
        stage.close();
    }

    /**
     * Checks the validity of every TextField. This includes empty fields, invalid prices, and invalid quantities.
     *
     * @return A boolean, true if all fields are valid, false otherwise.
     */
    public boolean fieldsAreValid() {
        boolean isValid = true;
        try {
            Money newPrice = Money.parse("NZD " + menuItemPrice.getText());
            if (newPrice.isLessThan(Money.parse("NZD 0"))) {
                responseText.setText("Price cannot be negative.");
                isValid = false;
            }
        } catch (IllegalArgumentException e) {
            responseText.setText("Invalid Cost value. Prices should be of the form X.XX where X is a digit");
            isValid = false;
        } catch (ArithmeticException e) {
            responseText.setText("Restrict the Cost value to two digits after the decimal point.");
            isValid = false;
        }
        if (menuItemName.getText().trim().length() == 0 ||
                menuItemPrice.getText().trim().length() == 0) {
            responseText.setText("A field is empty.");
            isValid = false;
        }
        return isValid;
    }

    /**
     * When modify recipe button is pressed, open recipe for menu item and
     **/
    public void modifyRecipe() {
        /*
        try {
             FXMLLoader loader = new FXMLLoader(getClass().getResource("modifyRecipe.fxml"));
             Parent root = loader.load();
             Stage stage = new Stage();
             stage.initModality(Modality.APPLICATION_MODAL);
             stage.setResizable(false);
             stage.setTitle("Modify Recipe");
             stage.setScene(new Scene(root));
             stage.show();
             ModifyRecipeController controller = loader.getController();
             controller.setMenuItem(this.menuItem);
             stage.setOnHiding(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent paramT) { }
             });
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        catch (NullPointerException e) { }
    **/
    }
}

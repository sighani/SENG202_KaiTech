package kaitech.controller;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import kaitech.api.database.InventoryTable;
import kaitech.api.model.Business;
import kaitech.api.model.Ingredient;
import kaitech.model.BusinessImpl;
import kaitech.util.ThreeValueLogic;
import kaitech.util.UnitType;
import org.joda.money.Money;
import org.joda.money.format.MoneyFormatter;
import org.joda.money.format.MoneyFormatterBuilder;

public class ModifyIngredientController {
    @FXML
    private Text titleText;

    @FXML
    private TextField nameField;

    @FXML
    private ComboBox unitCB;

    @FXML
    private TextField costField;

    @FXML
    private ComboBox vegCB;

    @FXML
    private ComboBox veganCB;

    @FXML
    private ComboBox glutenFreeCB;

    @FXML
    private TextField quantityField;

    @FXML
    private Text responseText;

    /**
     * The Ingredient that is being modified.
     */
    private Ingredient ingredient;

    private InventoryTable inventoryTable;

    /**
     * A formatter for readable displaying of money.
     */
    private static final MoneyFormatter MONEY_FORMATTER = new MoneyFormatterBuilder().appendAmountLocalized().toFormatter();

    /**
     * Sets the ingredient and calls the start method. This is used as an alternative to an initialize method as the
     * supplier must be obtained as a parameter.
     *
     * @param ingredient The ingredient to modify.
     */
    public void setIngredient(Ingredient ingredient) {
        this.ingredient = ingredient;
        start();
    }

    public void start() {
        titleText.setText("Now modifying Ingredient " + ingredient.getName() + "(" + ingredient.getCode() + ")");
        Business business = BusinessImpl.getInstance();
        inventoryTable = business.getInventoryTable();
        nameField.setText(ingredient.getName());
        unitCB.getItems().setAll(UnitType.values());
        unitCB.getSelectionModel().select(ingredient.getUnit());
        costField.setText(MONEY_FORMATTER.print(ingredient.getPrice()));
        vegCB.getItems().setAll(ThreeValueLogic.values());
        vegCB.getSelectionModel().select(ingredient.getIsVeg());
        veganCB.getItems().setAll(ThreeValueLogic.values());
        veganCB.getSelectionModel().select(ingredient.getIsVegan());
        glutenFreeCB.getItems().setAll(ThreeValueLogic.values());
        glutenFreeCB.getSelectionModel().select(ingredient.getIsGF());
        quantityField.setText(inventoryTable.getIngredientQuantity(ingredient).toString());
    }

    public void confirm() {
        if (fieldsAreValid()) {
            ingredient.setName(nameField.getText());
            ingredient.setUnit((UnitType) unitCB.getValue());
            ingredient.setPrice(Money.parse("NZD " + costField.getText().replace(",", "")));
            ingredient.setIsVeg((ThreeValueLogic) vegCB.getValue());
            ingredient.setIsVegan((ThreeValueLogic) veganCB.getValue());
            ingredient.setIsGF((ThreeValueLogic) glutenFreeCB.getValue());
            inventoryTable.setQuantity(ingredient, Integer.parseInt(quantityField.getText()));
            Stage stage = (Stage) titleText.getScene().getWindow();
            stage.close();
        } else {
            responseText.setVisible(true);
        }
    }

    /**
     * Checks the validity of every TextField. This includes empty fields, invalid prices, and invalid quantities.
     *
     * @return A boolean, true if all fields are valid, false otherwise.
     */
    public boolean fieldsAreValid() {
        boolean isValid = true;
        try {
            Money newPrice = Money.parse("NZD " + costField.getText().replace(",", ""));
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
        try {
            int quantity = Integer.parseInt(quantityField.getText());
            if (quantity < 0) {
                responseText.setText("Quantity cannot be negative.");
                isValid = false;
            }
        } catch (NumberFormatException e) {
            responseText.setText("Quantity should be an integer.");
            isValid = false;
        }
        if (nameField.getText().trim().length() == 0 ||
                costField.getText().trim().length() == 0 || quantityField.getText().trim().length() == 0) {
            responseText.setText("A field is empty.");
            isValid = false;
        }
        return isValid;
    }
}

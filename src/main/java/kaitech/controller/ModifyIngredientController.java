package kaitech.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
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
    private TextField codeField;

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

    private Ingredient ingredient;

    private Business business;

    private String initialCode;

    private static final MoneyFormatter MONEY_FORMATTER = new MoneyFormatterBuilder().appendAmountLocalized().toFormatter();

    public void setIngredient(Ingredient ingredient) {
        this.ingredient = ingredient;
        start();
    }

    public void start() {
        titleText.setText("Now modifying Ingredient " + ingredient.getName() + "(" + ingredient.getCode() + ")");
        business = BusinessImpl.getInstance();
        nameField.setText(ingredient.getName());
        codeField.setText(ingredient.getCode());
        initialCode = ingredient.getCode();
        unitCB.getItems().setAll(UnitType.values());
        unitCB.getSelectionModel().select(ingredient.getUnit());
        costField.setText(MONEY_FORMATTER.print(ingredient.getPrice()));
        vegCB.getItems().setAll(ThreeValueLogic.values());
        vegCB.getSelectionModel().select(ingredient.getIsVeg());
        veganCB.getItems().setAll(ThreeValueLogic.values());
        veganCB.getSelectionModel().select(ingredient.getIsVegan());
        glutenFreeCB.getItems().setAll(ThreeValueLogic.values());
        glutenFreeCB.getSelectionModel().select(ingredient.getIsGF());
        quantityField.setText(business.getIngredients().get(ingredient).toString());
    }

    public void confirm() {
        if (fieldsAreValid()) {
            ingredient.setName(nameField.getText());
            //TODO: Not supported!
            //ingredient.setCode(codeField.getText());
            ingredient.setUnit((UnitType) unitCB.getValue());
            ingredient.setPrice(Money.parse("NZD " + costField.getText()));
            ingredient.setIsVeg((ThreeValueLogic) vegCB.getValue());
            ingredient.setIsVegan((ThreeValueLogic) veganCB.getValue());
            ingredient.setIsGF((ThreeValueLogic) glutenFreeCB.getValue());
            business.getInventory().put(ingredient, Integer.parseInt(quantityField.getText()));
            Stage stage = (Stage) titleText.getScene().getWindow();
            stage.close();
        }
        else {
            responseText.setVisible(true);
        }
    }

    public boolean fieldsAreValid() {
        boolean isValid = true;
        if (codeField.getText().trim().length() == 0 || nameField.getText().trim().length() == 0 ||
                costField.getText().trim().length() == 0 || quantityField.getText().trim().length() == 0) {
            responseText.setText("A field is empty.");
            isValid = false;
        }
        for (Ingredient key : business.getInventory().keySet()) {
            if (codeField.getText().equals(key.getCode()) && !codeField.getText().equals(initialCode)) {
                responseText.setText("The inventory already has an Ingredient with that code.");
                isValid = false;
            }
        }
        try {
            Money newPrice = Money.parse("NZD " + costField.getText());
            if (newPrice.isLessThan(Money.parse("NZD 0"))) {
                responseText.setText("Price cannot be negative.");
                isValid = false;
            }
        }
        catch (IllegalArgumentException e) {
            responseText.setText("Invalid Cost value. Prices should be of the form X.XX where X is a digit");
            isValid = false;
        }
        catch (ArithmeticException e) {
            responseText.setText("Restrict the Cost value to two digits after the decimal point.");
            isValid = false;
        }
        try {
            int quantity = Integer.parseInt(quantityField.getText());
            if (quantity < 0) {
                responseText.setText("Quantity cannot be negative.");
                isValid = false;
            }
        }
        catch (NumberFormatException e) {
            responseText.setText("Quantity should be an integer.");
            isValid = false;
        }
        return isValid;
    }
}

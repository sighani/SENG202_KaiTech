package kaitech.controller;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import kaitech.api.database.IngredientTable;
import kaitech.api.model.Business;
import kaitech.model.BusinessImpl;
import kaitech.model.IngredientImpl;
import kaitech.util.ThreeValueLogic;
import kaitech.util.UnitType;
import org.joda.money.Money;

public class NewIngredientController {

    @FXML
    private TextField ingredCode;
    @FXML
    private TextField ingredName;
    @FXML
    private TextField ingredCost;
    @FXML
    private ComboBox ingredUnit;
    @FXML
    private ComboBox isVege;
    @FXML
    private ComboBox isVegan;
    @FXML
    private ComboBox isGf;
    @FXML
    private Text manualUploadText;
    @FXML
    private Text titleText;

    private IngredientTable ingredientTable;

    @FXML
    private Text responseText;

    public void initialize() {
        Business business = BusinessImpl.getInstance();
        ingredientTable = business.getIngredientTable();
    }

    public void setComboBoxes() {
        ingredUnit.getItems().setAll(UnitType.values());
        isVege.getItems().setAll(ThreeValueLogic.values());
        isVegan.getItems().setAll(ThreeValueLogic.values());
        isGf.getItems().setAll(ThreeValueLogic.values());
    }

    public void exit() {
        Stage stage = (Stage) titleText.getScene().getWindow();
        stage.close();
    }

    /**
     * This method is run when the confirm button is pressed on the ingredient form, the method then gets the relevant
     * information from the user input, creates a new ingredient from it, and then adds it to an instance of Business.
     * It also gives back informational feedback to the user to inform them that the ingredient has been added
     * successfully.
     */
    public void confirmIngredient() {
        if (fieldsAreValid()) {
            try {
                String code = ingredCode.getText();
                String name = ingredName.getText();
                UnitType unit = (UnitType) ingredUnit.getValue();
                Money cost = Money.parse("NZD " + ingredCost.getText());
                ThreeValueLogic vege = (ThreeValueLogic) isVege.getValue();
                ThreeValueLogic vegan = (ThreeValueLogic) isVegan.getValue();
                ThreeValueLogic gf = (ThreeValueLogic) isGf.getValue();

                IngredientImpl newIngredient = new IngredientImpl(code, name, unit, cost, vege, vegan, gf);

                ingredientTable.putIngredient(newIngredient);

                manualUploadText.setText("Ingredient: " + name + ", has been added.  ");
                manualUploadText.setVisible(true);
            } catch (RuntimeException e) {
                manualUploadText.setText("That code already exists, please enter a unique code.");
                manualUploadText.setVisible(true);
            }
        } else {
            manualUploadText.setVisible(true);
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
            Money newPrice = Money.parse("NZD " + ingredCost.getText());
            if (newPrice.isLessThan(Money.parse("NZD 0"))) {
                manualUploadText.setText("Price cannot be negative.");
                isValid = false;
            }
        } catch (IllegalArgumentException e) {
            manualUploadText.setText("Prices should be of the form X.XX where X is a digit");
            isValid = false;
        } catch (ArithmeticException e) {
            manualUploadText.setText("Cost should have two digits after the decimal point.");
            isValid = false;
        }
        if (ingredName.getText().trim().length() == 0 ||
                ingredCost.getText().trim().length() == 0 || ingredCode.getText().trim().length() == 0) {
            manualUploadText.setText("A field is empty.");
            isValid = false;
        }
        if (isGf.getValue() == null || isVegan.getValue() == null || isVege.getValue() == null || ingredUnit.getValue() == null) {
            manualUploadText.setText("Please choose an option for each combo box.");
            isValid = false;
        }
        return isValid;
    }
}

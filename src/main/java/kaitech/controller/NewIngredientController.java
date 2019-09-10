package kaitech.controller;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import kaitech.api.database.IngredientTable;
import kaitech.api.database.InventoryTable;
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

    private IngredientTable ingredients;

    private InventoryTable inventoryTable;

    private Business business;

    public void initialize() {

        business = BusinessImpl.getInstance();
        ingredients = business.getIngredientTable();
        inventoryTable = business.getInventoryTable();
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
    public void confirmIngredient(){
        String code = ingredCode.getText();
        String name = ingredName.getText();
        UnitType unit = (UnitType) ingredUnit.getValue();
        Money cost = Money.parse("NZD " + ingredCost.getText());
        ThreeValueLogic vege = (ThreeValueLogic) isVege.getValue();
        ThreeValueLogic vegan = (ThreeValueLogic) isVegan.getValue();
        ThreeValueLogic gf = (ThreeValueLogic) isGf.getValue();

        IngredientImpl newIngredient = new IngredientImpl(code, name, unit, cost, vege, vegan, gf);
        inventoryTable.putInventory(newIngredient, 10);


        System.out.println("Code: " + code);
        System.out.println("Name: " + name);
        System.out.println("Unit: " + unit);
        System.out.println("Cost: " + cost);
        System.out.println("Is vegetarian: " + vege);
        System.out.println("Is vegan: " + vegan);
        System.out.println("Is gf: " + gf);
        manualUploadText.setText("Ingredient: " + name + ", has been added.  ");
        manualUploadText.setVisible(true);
    }
}

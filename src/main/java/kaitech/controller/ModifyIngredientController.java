package kaitech.controller;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import kaitech.api.model.Business;
import kaitech.api.model.Ingredient;
import kaitech.model.BusinessImpl;
import kaitech.util.ThreeValueLogic;
import kaitech.util.UnitType;

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

    private Ingredient ingredient;

    private Business business;

    public void setIngredient(Ingredient ingredient) {
        this.ingredient = ingredient;
        start();
    }

    public void start() {
        titleText.setText("Now modifying Ingredient " + ingredient.getName() + "(" + ingredient.getCode() + ")");
        business = BusinessImpl.getInstance();
        codeField.setText(ingredient.getName());
        nameField.setText(ingredient.getCode());
        unitCB.getItems().setAll(UnitType.values());
        unitCB.getSelectionModel().select(ingredient.getUnit());
        costField.setText(ingredient.getPrice().toString());
        vegCB.getItems().setAll(ThreeValueLogic.values());
        vegCB.getSelectionModel().select(ingredient.isVeg());
        veganCB.getItems().setAll(ThreeValueLogic.values());
        veganCB.getSelectionModel().select(ingredient.isVegan());
        glutenFreeCB.getItems().setAll(ThreeValueLogic.values());
        glutenFreeCB.getSelectionModel().select(ingredient.isGF());
        quantityField.setText(business.getIngredients().get(ingredient).toString());
    }

    public void confirm() {
    }
}

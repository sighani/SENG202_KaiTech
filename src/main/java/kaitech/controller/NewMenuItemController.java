package kaitech.controller;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import kaitech.api.model.Business;
import kaitech.api.model.Ingredient;
import kaitech.api.model.Recipe;
import kaitech.model.BusinessImpl;
import kaitech.model.MenuItemImpl;
import kaitech.model.RecipeImpl;
import kaitech.util.MenuItemType;
import org.joda.money.Money;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NewMenuItemController {
    @FXML
    private TextField menuItemCode;

    @FXML
    private TextField menuItemName;

    @FXML
    private TextField menuItemIngredients;

    @FXML
    private TextField menuItemRecipe;

    @FXML
    private TextField menuItemPrice;

    @FXML
    private ComboBox menuItemType;

    @FXML
    private Text manualUploadText;

    @FXML
    private Text titleText;

    private Business business;


    public void initialize() {
        business = BusinessImpl.getInstance();
    }
    public void setComboBoxes() {
        menuItemType.getItems().setAll(MenuItemType.values());
    }

    public void exit() {
        Stage stage = (Stage) titleText.getScene().getWindow();
        stage.close();

    }

    public void confirm() {
        Map<Ingredient, Integer> ingredients = new HashMap<>();
        List<String> ingred = new ArrayList<>();
        String code = menuItemCode.getText();
        String name = menuItemName.getText();
        //String ingred = menuItemIngredients.getText();
        //String recipe = menuItemRecipe.getText();
        Money newPrice = Money.parse("NZD " + menuItemPrice.getText());
        MenuItemType type = (MenuItemType) menuItemType.getValue();
       // Recipe newRecipe = new RecipeImpl(ingredients, 14, 18, 20);

       // MenuItemImpl newMenuItem = new MenuItemImpl(code, name, ingred, newRecipe, newPrice, type);
       // business.addMenuItem(newMenuItem);

        System.out.println("Name: " + name);
        System.out.println("Code: " + code);
        System.out.println("Type: " + type);
        System.out.println("Ingredients: " + ingred);
        System.out.println("Recipe: ");
        System.out.println("Price: ");
        manualUploadText.setText("MenuItem: " + name + ", has been added.  ");
        manualUploadText.setVisible(true);
    }
}

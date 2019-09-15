package kaitech.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import kaitech.api.database.MenuItemTable;
import kaitech.api.model.Business;
import kaitech.api.model.Ingredient;
import kaitech.api.model.Recipe;
import kaitech.model.BusinessImpl;
import kaitech.model.MenuItemImpl;
import kaitech.model.RecipeImpl;
import kaitech.util.MenuItemType;
import org.joda.money.Money;

import java.io.IOException;
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
    private TextField menuItemPrice;

    @FXML
    private ComboBox menuItemType;

    @FXML
    private Text manualUploadText;

    @FXML
    private Text titleText;

    private Business business;
    private Recipe newRecipe;
    private MenuItemTable menuItemTable;


    public void initialize() {
        business = BusinessImpl.getInstance();
        menuItemTable = business.getMenuItemTable();
    }
    public void setComboBoxes() {
        menuItemType.getItems().setAll(MenuItemType.values());
    }
    public void setNewRecipe(Recipe recipe) {
        newRecipe = recipe;
    }

    public void exit() {
        Stage stage = (Stage) titleText.getScene().getWindow();
        stage.close();

    }

    public void confirm() {
        try {
            String code = menuItemCode.getText();
            String name = menuItemName.getText();
            Money newPrice = Money.parse("NZD " + menuItemPrice.getText());
            MenuItemType type = (MenuItemType) menuItemType.getValue();
            List<String> ingredients = newRecipe.getIngredientNames();

            MenuItemImpl newMenuItem = new MenuItemImpl(code, name, newPrice, newRecipe, type, ingredients);
            menuItemTable.putMenuItem(newMenuItem);
            System.out.println(newMenuItem.getCode());
            System.out.println(newMenuItem.getIngredients() + "Ingredients");
            System.out.println(newRecipe.getCookingTime());
            System.out.println(newRecipe.getIngredients().values());
            System.out.println(newRecipe.getIngredients().keySet());
            System.out.println(newRecipe.getIngredientNames());
            System.out.println("Name: " + name);
            System.out.println("Code: " + code);
            System.out.println("Type: " + type);
            System.out.println("Ingredients: ");
            System.out.println("Recipe: ");
            System.out.println("Price: ");
            manualUploadText.setText("MenuItem: " + name + ", has been added.  ");
            manualUploadText.setVisible(true);
        } catch (RuntimeException e) {
            manualUploadText.setText("That code already exists, please enter a unique code.");
            manualUploadText.setVisible(true);

        }
    }

}

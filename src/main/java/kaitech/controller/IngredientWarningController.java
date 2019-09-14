package kaitech.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import kaitech.api.model.Business;
import kaitech.api.model.Ingredient;
import kaitech.api.model.MenuItem;
import kaitech.model.BusinessImpl;

public class IngredientWarningController {
    @FXML
    private TextArea menuItems;

    private Business business;

    /**
     * The ingredient that has been selected for deletion.
     */
    private Ingredient ingredient;

    @FXML
    private void initialize() {
        business = BusinessImpl.getInstance();
        ingredient = InventoryController.getSelectedIngredient();
        String affectedItems = "";
        for (MenuItem item : business.getAffectedMenuItems(ingredient)) {
            affectedItems += item.getName() + "(" + item.getCode() + ")\n";
        }
        menuItems.setEditable(false);
        menuItems.setText(affectedItems);
    }

    public void confirm() {
        business.getInventoryTable().removeInventory(ingredient);
        business.getIngredientTable().removeIngredient(ingredient.getCode());
        for (MenuItem item : business.getAffectedMenuItems(ingredient)) {
            System.out.println(item.getName());
            item.getRecipe().removeIngredient(ingredient);
        }
        cancel();
    }

    public void cancel() {
        Stage stage = (Stage) menuItems.getScene().getWindow();
        stage.close();
    }
}

package kaitech.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import kaitech.api.model.Business;
import kaitech.api.model.Ingredient;
import kaitech.model.BusinessImpl;
import kaitech.model.IngredientImpl;
import kaitech.util.ThreeValueLogic;
import kaitech.util.UnitType;
import org.joda.money.Money;

import java.util.Map;

/**
 * The controller for the inventory screen for displaying the current inventory
 * and stock of the business.
 */
public class InventoryController {
    @FXML
    private TableView<Ingredient> table = new TableView<Ingredient>();

    private Business business;

    @FXML
    public void initialize() {
        business = BusinessImpl.getInstance();
        Money newIngPrice = Money.parse("NZD 0.30");
        Ingredient newIng1 = new IngredientImpl("Cheese1", "Cheese", UnitType.COUNT, newIngPrice, ThreeValueLogic.YES, ThreeValueLogic.NO, ThreeValueLogic.NO);
        Ingredient newIng2 = new IngredientImpl("Cheese2", "Cheese", UnitType.COUNT, newIngPrice, ThreeValueLogic.YES, ThreeValueLogic.NO, ThreeValueLogic.NO);
        Ingredient newIng3 = new IngredientImpl("Cheese3", "Cheese", UnitType.COUNT, newIngPrice, ThreeValueLogic.YES, ThreeValueLogic.NO, ThreeValueLogic.NO);
        Ingredient newIng4 = new IngredientImpl("Cheese4", "Cheese", UnitType.COUNT, newIngPrice, ThreeValueLogic.YES, ThreeValueLogic.NO, ThreeValueLogic.NO);
        Ingredient newIng5 = new IngredientImpl("Cheese5", "Cheese", UnitType.COUNT, newIngPrice, ThreeValueLogic.YES, ThreeValueLogic.NO, ThreeValueLogic.NO);
        Ingredient newIng6 = new IngredientImpl("Cheese6", "Cheese", UnitType.COUNT, newIngPrice, ThreeValueLogic.YES, ThreeValueLogic.NO, ThreeValueLogic.NO);
        Ingredient newIng7 = new IngredientImpl("Cheese7", "Cheese", UnitType.COUNT, newIngPrice, ThreeValueLogic.YES, ThreeValueLogic.NO, ThreeValueLogic.NO);
        Ingredient newIng8 = new IngredientImpl("Cheese8", "Cheese", UnitType.COUNT, newIngPrice, ThreeValueLogic.YES, ThreeValueLogic.NO, ThreeValueLogic.NO);
        business.addIngredient(newIng1, 30);
        business.addIngredient(newIng2, 30);
        business.addIngredient(newIng3, 30);
        business.addIngredient(newIng4, 30);
        business.addIngredient(newIng5, 30);
        business.addIngredient(newIng6, 30);
        business.addIngredient(newIng7, 30);
        business.addIngredient(newIng8, 30);

        TableColumn column1 = new TableColumn("Code");
        column1.setCellValueFactory(new PropertyValueFactory<Ingredient, String>("code"));

        TableColumn column2 = new TableColumn("Name");
        column2.setCellValueFactory(new PropertyValueFactory<Ingredient, String>("name"));

        table.getColumns().add(column1);
        table.getColumns().add(column2);

        ObservableList<Ingredient> data = FXCollections.observableArrayList();
        for (Map.Entry<Ingredient, Integer> entry : business.getIngredients().entrySet()) {
            data.add(entry.getKey());
        }
        table.setItems(data);
    }
}

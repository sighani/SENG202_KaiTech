package kaitech.controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
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

/**
 * The controller for the inventory screen for displaying the current inventory
 * and stock of the business.
 */
public class InventoryController {

    @FXML
    private TableView<Ingredient> table;

    @FXML
    private TableColumn<Ingredient, String> codeCol;

    @FXML
    private TableColumn<Ingredient, String> nameCol;

    @FXML
    private TableColumn<Ingredient, String> unitTypeCol;

    @FXML
    private TableColumn<Ingredient, String> costCol;

    @FXML
    private TableColumn<Ingredient, String> vegCol;

    @FXML
    private TableColumn<Ingredient, String> veganCol;

    @FXML
    private TableColumn<Ingredient, String> gfCol;

    @FXML
    private TableColumn<Ingredient, String> quantityCol;

    private Business business;

    @FXML
    public void initialize() {
        business = BusinessImpl.getInstance();
        Money newIngPrice = Money.parse("NZD 0.30");
        Ingredient newIng1 = new IngredientImpl("Cheese1", "Cheese", UnitType.COUNT, newIngPrice, ThreeValueLogic.YES, ThreeValueLogic.NO, ThreeValueLogic.NO);
        Ingredient newIng2 = new IngredientImpl("Cheese2", "Cheese", UnitType.COUNT, newIngPrice, ThreeValueLogic.YES, ThreeValueLogic.YES, ThreeValueLogic.UNKNOWN);
        Ingredient newIng3 = new IngredientImpl("Cheese3", "Cheese", UnitType.COUNT, newIngPrice, ThreeValueLogic.YES, ThreeValueLogic.NO, ThreeValueLogic.NO);
        Ingredient newIng4 = new IngredientImpl("Cheese4", "Cheese", UnitType.COUNT, newIngPrice, ThreeValueLogic.YES, ThreeValueLogic.NO, ThreeValueLogic.NO);
        Ingredient newIng5 = new IngredientImpl("Cheese5", "Cheese", UnitType.COUNT, newIngPrice, ThreeValueLogic.YES, ThreeValueLogic.NO, ThreeValueLogic.NO);
        Ingredient newIng6 = new IngredientImpl("Cheese6", "Cheese", UnitType.COUNT, newIngPrice, ThreeValueLogic.YES, ThreeValueLogic.NO, ThreeValueLogic.NO);
        Ingredient newIng7 = new IngredientImpl("Cheese7", "Cheese", UnitType.COUNT, newIngPrice, ThreeValueLogic.YES, ThreeValueLogic.NO, ThreeValueLogic.NO);
        Ingredient newIng8 = new IngredientImpl("Cheese8", "Cheese", UnitType.COUNT, newIngPrice, ThreeValueLogic.YES, ThreeValueLogic.NO, ThreeValueLogic.NO);
        Ingredient newIng9 = new IngredientImpl("Cheese9", "Cheese", UnitType.COUNT, newIngPrice, ThreeValueLogic.YES, ThreeValueLogic.NO, ThreeValueLogic.NO);
        Ingredient newIng10 = new IngredientImpl("Cheese10", "Cheese", UnitType.COUNT, newIngPrice, ThreeValueLogic.YES, ThreeValueLogic.YES, ThreeValueLogic.UNKNOWN);
        Ingredient newIng11 = new IngredientImpl("Cheese11", "Cheese", UnitType.COUNT, newIngPrice, ThreeValueLogic.YES, ThreeValueLogic.NO, ThreeValueLogic.NO);
        Ingredient newIng12 = new IngredientImpl("Cheese12", "Cheese", UnitType.COUNT, newIngPrice, ThreeValueLogic.YES, ThreeValueLogic.NO, ThreeValueLogic.NO);
        Ingredient newIng13 = new IngredientImpl("Cheese13", "Cheese", UnitType.COUNT, newIngPrice, ThreeValueLogic.YES, ThreeValueLogic.NO, ThreeValueLogic.NO);
        Ingredient newIng14 = new IngredientImpl("Cheese14", "Cheese", UnitType.COUNT, newIngPrice, ThreeValueLogic.YES, ThreeValueLogic.NO, ThreeValueLogic.NO);
        Ingredient newIng15 = new IngredientImpl("Cheese15", "Cheese", UnitType.COUNT, newIngPrice, ThreeValueLogic.YES, ThreeValueLogic.NO, ThreeValueLogic.NO);
        Ingredient newIng16 = new IngredientImpl("Cheese16", "Cheese", UnitType.COUNT, newIngPrice, ThreeValueLogic.YES, ThreeValueLogic.NO, ThreeValueLogic.NO);
        business.addIngredient(newIng1, 30);
        business.addIngredient(newIng2, 30);
        business.addIngredient(newIng3, 30);
        business.addIngredient(newIng4, 30);
        business.addIngredient(newIng5, 30);
        business.addIngredient(newIng6, 30);
        business.addIngredient(newIng7, 30);
        business.addIngredient(newIng8, 30);
        business.addIngredient(newIng9, 30);
        business.addIngredient(newIng10, 30);
        business.addIngredient(newIng11, 30);
        business.addIngredient(newIng12, 30);
        business.addIngredient(newIng13, 30);
        business.addIngredient(newIng14, 30);
        business.addIngredient(newIng15, 30);
        business.addIngredient(newIng16, 30);

        codeCol.setCellValueFactory(new PropertyValueFactory<>("code"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        unitTypeCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getUnit().toString()));
        costCol.setCellValueFactory(cellData -> {
            Money cost = cellData.getValue().getPrice();
            String toShow = "$" + cost.getAmountMajorInt() + "." + cost.getAmountMinorInt();
            return new SimpleStringProperty(toShow);
        });
        vegCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().isVeg().toString()));
        veganCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().isVegan().toString()));
        gfCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().isGF().toString()));
        quantityCol.setCellValueFactory(cellData -> new SimpleStringProperty(business.getIngredients().get(cellData.getValue()).toString()));

        table.setItems(FXCollections.observableArrayList(business.getIngredients().keySet()));
    }
}

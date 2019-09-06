package kaitech.model;

import kaitech.api.model.Ingredient;
import kaitech.api.model.Menu;
import kaitech.api.model.MenuItem;
import kaitech.api.model.Recipe;
import kaitech.util.ThreeValueLogic;
import kaitech.util.UnitType;
import org.joda.money.Money;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;


public class MenuTest {
    private Menu testMenu;
    private MenuItem testItem;

    @BeforeEach
    public void init() {
        testMenu = new MenuImpl("Special");
        Money price = Money.parse("NZD 2.00");
        Ingredient testIngredient = new IngredientImpl("ing1", "Something", UnitType.GRAM, price,
                ThreeValueLogic.UNKNOWN, ThreeValueLogic.UNKNOWN, ThreeValueLogic.UNKNOWN);
        Map<Ingredient, Integer> ingredientsMap = new HashMap<>();
        ingredientsMap.put(testIngredient, 1);
        Recipe testRecipe = new RecipeImpl(2, 10, 1, ingredientsMap);
        ArrayList<String> ingredientNames = new ArrayList<>();
        ingredientNames.add(testIngredient.getName());
        testItem = new MenuItemImpl("B1", "Cheese Burger", testRecipe, null, ingredientNames);
    }

    @Test
    public void addMenuItemTest() {
        assertEquals(0, testMenu.getMenuItems().size());
        testMenu.addMenuItem(testItem);
        Map<String, MenuItem> toCmp = new HashMap<>();
        toCmp.put(testItem.getCode(), testItem);
        assertEquals(toCmp, testMenu.getMenuItems());
    }

    @Test
    public void cannotAddDuplicateMenuItemTest() {
        testMenu.addMenuItem(testItem);
        testMenu.addMenuItem(testItem);
        assertEquals(1, testMenu.getMenuItems().size());
    }

    @Test
    public void removeMenuItemTest() {
        testMenu.addMenuItem(testItem);
        Map<String, MenuItem> toCmp = new HashMap<>();
        toCmp.put(testItem.getCode(), testItem);
        assertEquals(toCmp, testMenu.getMenuItems());
        testMenu.removeMenuItem(testItem);
        assertEquals(0, testMenu.getMenuItems().size());
    }
}

package kaitech.model;

import kaitech.util.ThreeValueLogic;
import kaitech.util.UnitType;
import org.joda.money.Money;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;


public class MenuTest {
    private Menu testMenu;
    private MenuItem testItem;

    @BeforeEach
    public void init() {
        testMenu = new Menu("Special", "M1");
        Money price = Money.parse("NZD 2.00");
        Ingredient testIngredient = new Ingredient("ing1", "Something", UnitType.GRAM, price,
                ThreeValueLogic.UNKNOWN, ThreeValueLogic.UNKNOWN, ThreeValueLogic.UNKNOWN);
        Map<Ingredient, Integer> ingredientsMap = new HashMap<Ingredient, Integer>();
        ingredientsMap.put(testIngredient, 1);
        Recipe testRecipe = new Recipe(ingredientsMap, 2, 10, 1);
        ArrayList<String> ingredientNames = new ArrayList<String>();
        ingredientNames.add(testIngredient.name());
        testItem = new MenuItem("B1", "Cheese Burger", ingredientNames, testRecipe);
    }

    @Test
    public void addMenuItemTest() {
        assertEquals(0, testMenu.getMenuItems().size());
        testMenu.addMenuItem(testItem);
        List<MenuItem> toCmp = new ArrayList<MenuItem>();
        toCmp.add(testItem);
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
        List<MenuItem> toCmp = new ArrayList<MenuItem>();
        toCmp.add(testItem);
        assertEquals(toCmp, testMenu.getMenuItems());
        testMenu.removeMenuItem(testItem);
        assertEquals(0, testMenu.getMenuItems().size());
    }
}

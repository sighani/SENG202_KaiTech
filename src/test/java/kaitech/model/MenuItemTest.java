package kaitech.model;

import kaitech.api.model.Business;
import kaitech.api.model.Ingredient;
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

import static org.junit.jupiter.api.Assertions.*;

public class MenuItemTest {
    private MenuItem testItem;
    private Ingredient testIngredient;

    @BeforeEach
    public void init() {
        Money price = Money.parse("NZD 3.00");
        testIngredient = new IngredientImpl("ing1", "Something", UnitType.GRAM, price,
                ThreeValueLogic.UNKNOWN, ThreeValueLogic.UNKNOWN, ThreeValueLogic.UNKNOWN);
        Map<Ingredient, Integer> ingredientsMap = new HashMap<>();
        ingredientsMap.put(testIngredient, 1);
        Recipe testRecipe = new RecipeImpl(ingredientsMap, 2, 10, 1);
        ArrayList<String> ingredientNames = new ArrayList<>();
        ingredientNames.add(testIngredient.getName());
        testItem = new MenuItemImpl("B1", "Cheese Burger", ingredientNames, testRecipe, null);

    }

    @Test
    public void addIngredientTest() {
        Money price = Money.parse("NZD 3.00");
        Ingredient testIngredient2 = new IngredientImpl("ing2", "Something2", UnitType.GRAM, price,
                ThreeValueLogic.UNKNOWN, ThreeValueLogic.UNKNOWN, ThreeValueLogic.UNKNOWN);
        testItem.addIngredientToRecipe(testIngredient2, 2);

        ArrayList<String> toCmpIngredientNames = new ArrayList<>();
        Map<Ingredient, Integer> toCmpIngredients = new HashMap<>();
        toCmpIngredientNames.add(testIngredient.getName());
        toCmpIngredientNames.add(testIngredient2.getName());
        toCmpIngredients.put(testIngredient, 1);
        toCmpIngredients.put(testIngredient2, 2);

        assertEquals(toCmpIngredientNames, testItem.ingredientNames());
        assertEquals(toCmpIngredients, testItem.getRecipe().getIngredients());
    }

    @Test
    public void cannotAddIngredientWithNonPositiveAmtTest() {
        Money price = Money.parse("NZD 3.00");
        Ingredient testIngredient2 = new IngredientImpl("ing2", "Something2", UnitType.GRAM, price,
                ThreeValueLogic.UNKNOWN, ThreeValueLogic.UNKNOWN, ThreeValueLogic.UNKNOWN);
        assertThrows(IllegalArgumentException.class, () -> testItem.addIngredientToRecipe(testIngredient2, -1));
        assertThrows(IllegalArgumentException.class, () -> testItem.addIngredientToRecipe(testIngredient2, 0));
    }

    @Test
    public void printIngredientNamesTest() {
        Money price = Money.parse("NZD 3.00");
        assertEquals("[B1(Cheese Burger): Something]", testItem.ingredients());
        Ingredient testIngredient2 = new IngredientImpl("ing2", "Cheese", UnitType.GRAM, price,
                ThreeValueLogic.UNKNOWN, ThreeValueLogic.UNKNOWN, ThreeValueLogic.UNKNOWN);
        testItem.addIngredientToRecipe(testIngredient2, 2);
        assertEquals("[B1(Cheese Burger): Something, Cheese]", testItem.ingredients());
    }

    @Test
    public void checkSufficientIngredientsTest() {
        BusinessImpl.reset();
        Money price = Money.parse("NZD 3.00");
        Business testBusiness = BusinessImpl.getInstance();
        Ingredient testIngredient = new IngredientImpl("ing1", "Something", UnitType.GRAM, price,
                ThreeValueLogic.UNKNOWN, ThreeValueLogic.UNKNOWN, ThreeValueLogic.UNKNOWN);
        testBusiness.addIngredient(testIngredient, 5);
        assertTrue(testItem.checkSufficientIngredients(testBusiness));

        Ingredient testIngredient2 = new IngredientImpl("ing2", "Something2", UnitType.GRAM, price,
                ThreeValueLogic.UNKNOWN, ThreeValueLogic.UNKNOWN, ThreeValueLogic.UNKNOWN);
        testItem.addIngredientToRecipe(testIngredient2, 2);
        testBusiness.addIngredient(testIngredient2, 2);
        assertTrue(testItem.checkSufficientIngredients(testBusiness));
    }

    @Test
    public void checkInsufficientIngredientsTest() {
        Money price = Money.parse("NZD 3.00");
        Business testBusiness = BusinessImpl.getInstance();
        Ingredient testIngredient = new IngredientImpl("ing1", "Something", UnitType.GRAM, price,
                ThreeValueLogic.UNKNOWN, ThreeValueLogic.UNKNOWN, ThreeValueLogic.UNKNOWN);
        testBusiness.addIngredient(testIngredient, 0);
        assertFalse(testItem.checkSufficientIngredients(testBusiness));
    }

    @Test
    public void calculateNumServingsTest() {
        BusinessImpl.reset();
        Money price = Money.parse("NZD 3.00");
        Business testBusiness = BusinessImpl.getInstance();
        Ingredient testIngredient = new IngredientImpl("ing1", "Something", UnitType.GRAM, price,
                ThreeValueLogic.UNKNOWN, ThreeValueLogic.UNKNOWN, ThreeValueLogic.UNKNOWN);
        testBusiness.addIngredient(testIngredient, 5);
        assertEquals(5, testItem.calculateNumServings(testBusiness));

        Ingredient testIngredient2 = new IngredientImpl("ing2", "Something2", UnitType.GRAM, price,
                ThreeValueLogic.UNKNOWN, ThreeValueLogic.UNKNOWN, ThreeValueLogic.UNKNOWN);
        testItem.addIngredientToRecipe(testIngredient2, 2);
        testBusiness.addIngredient(testIngredient2, 2);
        assertEquals(1, testItem.calculateNumServings(testBusiness));
    }

    @Test
    public void insufficientIngredientsIfIngredientNotInBusinessInventoryTest() {
        Money price = Money.parse("NZD 3.00");
        Ingredient alienIngredient = new IngredientImpl("ing3", "Something3", UnitType.GRAM, price,
                ThreeValueLogic.UNKNOWN, ThreeValueLogic.UNKNOWN, ThreeValueLogic.UNKNOWN);
        testItem.addIngredientToRecipe(alienIngredient, 2);

        Business testBusiness = BusinessImpl.getInstance();
        assertFalse(testItem.checkSufficientIngredients(testBusiness));
    }

    @Test
    public void zeroServingsIfIngredientNotInBusinessInventoryTest() {
        Money price = Money.parse("NZD 3.00");
        Ingredient alienIngredient = new IngredientImpl("ing3", "Something3", UnitType.GRAM, price,
                ThreeValueLogic.UNKNOWN, ThreeValueLogic.UNKNOWN, ThreeValueLogic.UNKNOWN);
        testItem.addIngredientToRecipe(alienIngredient, 2);

        Business testBusiness = BusinessImpl.getInstance();
        assertEquals(0, testItem.calculateNumServings(testBusiness));
    }
}

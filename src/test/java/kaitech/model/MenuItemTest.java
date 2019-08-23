package kaitech.model;

import kaitech.util.ThreeValueLogic;
import kaitech.util.UnitType;
import org.joda.money.Money;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MenuItemTest {
    private MenuItem testItem;
    private Ingredient testIngredient;

    @BeforeEach
    public void init() {
        Money price = Money.parse("NZD 3.00");
        testIngredient = new Ingredient("ing1", "Something", UnitType.GRAM, price,
                ThreeValueLogic.UNKNOWN, ThreeValueLogic.UNKNOWN, ThreeValueLogic.UNKNOWN);
        Map<Ingredient, Integer> ingredientsMap = new HashMap<Ingredient, Integer>();
        ingredientsMap.put(testIngredient, 1);
        Recipe testRecipe = new Recipe(ingredientsMap, 2, 10, 1);
        ArrayList<String> ingredientNames = new ArrayList<String>();
        ingredientNames.add(testIngredient.name());
        testItem = new MenuItem("B1", "Cheese Burger", ingredientNames, testRecipe);

    }

    @Test
    public void addIngredientTest() {
        Money price = Money.parse("NZD 3.00");
        Ingredient testIngredient2 = new Ingredient("ing2", "Something2", UnitType.GRAM, price,
                ThreeValueLogic.UNKNOWN, ThreeValueLogic.UNKNOWN, ThreeValueLogic.UNKNOWN);
        testItem.addIngredientToRecipe(testIngredient2, 2);

        ArrayList<String> toCmpIngredientNames = new ArrayList<String>();
        Map<Ingredient, Integer> toCmpIngredients = new HashMap<Ingredient, Integer>();
        toCmpIngredientNames.add(testIngredient.name());
        toCmpIngredientNames.add(testIngredient2.name());
        toCmpIngredients.put(testIngredient, 1);
        toCmpIngredients.put(testIngredient2, 2);

        assertEquals(toCmpIngredientNames, testItem.ingredientNames());
        assertEquals(toCmpIngredients, testItem.getRecipe().getIngredients());
    }

    @Test
    public void cannotAddIngredientWithNonPositiveAmtTest() {
        Money price = Money.parse("NZD 3.00");
        Ingredient testIngredient2 = new Ingredient("ing2", "Something2", UnitType.GRAM, price,
                ThreeValueLogic.UNKNOWN, ThreeValueLogic.UNKNOWN, ThreeValueLogic.UNKNOWN);
        assertThrows(IllegalArgumentException.class, () -> {
            testItem.addIngredientToRecipe(testIngredient2, -1);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            testItem.addIngredientToRecipe(testIngredient2, 0);
        });
    }

    @Test
    public void printIngredientNamesTest() {
        Money price = Money.parse("NZD 3.00");
        assertEquals("[B1(Cheese Burger): Something]", testItem.ingredients());
        Ingredient testIngredient2 = new Ingredient("ing2", "Cheese", UnitType.GRAM, price,
                ThreeValueLogic.UNKNOWN, ThreeValueLogic.UNKNOWN, ThreeValueLogic.UNKNOWN);
        testItem.addIngredientToRecipe(testIngredient2, 2);
        assertEquals("[B1(Cheese Burger): Something, Cheese]",  testItem.ingredients());
    }

    @Test
    public void checkSufficientIngredientsTest() {
        Money price = Money.parse("NZD 3.00");
        Business testBusiness = new Business();
        Ingredient testIngredient = new Ingredient("ing1", "Something", UnitType.GRAM, price,
                ThreeValueLogic.UNKNOWN, ThreeValueLogic.UNKNOWN, ThreeValueLogic.UNKNOWN);
        testBusiness.addIngredient(testIngredient, 5);
        assertTrue(testItem.checkSufficientIngredients(testBusiness));

        Ingredient testIngredient2 = new Ingredient("ing2", "Something2", UnitType.GRAM, price,
                ThreeValueLogic.UNKNOWN, ThreeValueLogic.UNKNOWN, ThreeValueLogic.UNKNOWN);
        testItem.addIngredientToRecipe(testIngredient2, 2);
        testBusiness.addIngredient(testIngredient2, 2);
        assertTrue(testItem.checkSufficientIngredients(testBusiness));
    }

    @Test
    public void checkInsufficientIngredientsTest() {
        Money price = Money.parse("NZD 3.00");
        Business testBusiness = new Business();
        Ingredient testIngredient = new Ingredient("ing1", "Something", UnitType.GRAM, price,
                ThreeValueLogic.UNKNOWN, ThreeValueLogic.UNKNOWN, ThreeValueLogic.UNKNOWN);
        testBusiness.addIngredient(testIngredient, 0);
        assertFalse(testItem.checkSufficientIngredients(testBusiness));
    }

    @Test
    public void calculateNumServingsTest() {
        Money price = Money.parse("NZD 3.00");
        Business testBusiness = new Business();
        Ingredient testIngredient = new Ingredient("ing1", "Something", UnitType.GRAM, price,
                ThreeValueLogic.UNKNOWN, ThreeValueLogic.UNKNOWN, ThreeValueLogic.UNKNOWN);
        testBusiness.addIngredient(testIngredient, 5);
        assertEquals(5, testItem.calculateNumServings(testBusiness));

        Ingredient testIngredient2 = new Ingredient("ing2", "Something2", UnitType.GRAM, price,
                ThreeValueLogic.UNKNOWN, ThreeValueLogic.UNKNOWN, ThreeValueLogic.UNKNOWN);
        testItem.addIngredientToRecipe(testIngredient2, 2);
        testBusiness.addIngredient(testIngredient2, 2);
        assertEquals(1, testItem.calculateNumServings(testBusiness));
    }

    @Test
    public void insufficientIngredientsIfIngredientNotInBusinessInventoryTest() {
        Money price = Money.parse("NZD 3.00");
        Ingredient alienIngredient = new Ingredient("ing3", "Something3", UnitType.GRAM, price,
                ThreeValueLogic.UNKNOWN, ThreeValueLogic.UNKNOWN, ThreeValueLogic.UNKNOWN);
        testItem.addIngredientToRecipe(alienIngredient, 2);

        Business testBusiness = new Business();
        assertFalse(testItem.checkSufficientIngredients(testBusiness));
    }

    @Test
    public void zeroServingsIfIngredientNotInBusinessInventoryTest() {
        Money price = Money.parse("NZD 3.00");
        Ingredient alienIngredient = new Ingredient("ing3", "Something3", UnitType.GRAM, price,
                ThreeValueLogic.UNKNOWN, ThreeValueLogic.UNKNOWN, ThreeValueLogic.UNKNOWN);
        testItem.addIngredientToRecipe(alienIngredient, 2);

        Business testBusiness = new Business();
        assertEquals(0, testItem.calculateNumServings(testBusiness));
    }
}

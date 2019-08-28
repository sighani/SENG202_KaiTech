package kaitech.model;

import kaitech.api.model.Ingredient;
import kaitech.api.model.Recipe;
import kaitech.util.ThreeValueLogic;
import kaitech.util.UnitType;
import org.joda.money.Money;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class RecipeTest {
    private Recipe testRecipe;

    @BeforeEach
    public void init() {
        Money price1 = Money.parse("NZD 2.50");
        Money price2 = Money.parse("NZD 3.00");
        Ingredient testIngredient = new IngredientImpl("ing1", "Something", UnitType.GRAM, price1,
                ThreeValueLogic.UNKNOWN, ThreeValueLogic.UNKNOWN, ThreeValueLogic.UNKNOWN);
        Ingredient testIngredient2 = new IngredientImpl("ing2", "Something2", UnitType.GRAM, price2,
                ThreeValueLogic.UNKNOWN, ThreeValueLogic.UNKNOWN, ThreeValueLogic.UNKNOWN);
        Map<Ingredient, Integer> ingredientsMap = new HashMap<>();
        ingredientsMap.put(testIngredient, 1);
        ingredientsMap.put(testIngredient2, 4);
        testRecipe = new RecipeImpl(ingredientsMap, 2, 10, 1);
    }

    @Test
    public void calculateTotalCostTest() {
        Money toCmp = Money.parse("NZD 14.50");
        assertEquals(toCmp, testRecipe.calculateTotalCost());
    }

    @Test
    public void addIngredientTest() {
        Money price = Money.parse("NZD 3.00");
        Ingredient testIngredient3 = new IngredientImpl("ing3", "Something3", UnitType.GRAM, price,
                ThreeValueLogic.UNKNOWN, ThreeValueLogic.UNKNOWN, ThreeValueLogic.UNKNOWN);
        testRecipe.addIngredient(testIngredient3, 2);
        assertTrue(testRecipe.getIngredients().containsKey(testIngredient3));
        assertEquals(testRecipe.getIngredients().get(testIngredient3), 2);
    }

    @Test
    public void cannotAddIngredientWithNonPositiveAmtTest() {
        Money price = Money.parse("NZD 3.00");
        Ingredient testIngredient2 = new IngredientImpl("ing2", "Something2", UnitType.GRAM, price,
                ThreeValueLogic.UNKNOWN, ThreeValueLogic.UNKNOWN, ThreeValueLogic.UNKNOWN);
        assertThrows(IllegalArgumentException.class, () -> testRecipe.addIngredient(testIngredient2, -1));
        assertThrows(IllegalArgumentException.class, () -> testRecipe.addIngredient(testIngredient2, 0));
    }

    @Test
    public void updateIngredientQuantityTest() {
        Money price = Money.parse("NZD 3.00");
        Ingredient testIngredient = new IngredientImpl("ing1", "Something", UnitType.GRAM, price,
                ThreeValueLogic.UNKNOWN, ThreeValueLogic.UNKNOWN, ThreeValueLogic.UNKNOWN);
        assertEquals(1, testRecipe.getIngredients().get(testIngredient));
        testRecipe.updateIngredientQuantity(testIngredient, 2);
        assertEquals(2, testRecipe.getIngredients().get(testIngredient));
    }

    @Test
    public void updateIngredientQuantityNonPositiveTest() {
        Money price = Money.parse("NZD 3.00");
        Ingredient testIngredient = new IngredientImpl("ing1", "Something", UnitType.GRAM, price,
                ThreeValueLogic.UNKNOWN, ThreeValueLogic.UNKNOWN, ThreeValueLogic.UNKNOWN);
        assertThrows(IllegalArgumentException.class, () -> {
            testRecipe.updateIngredientQuantity(testIngredient, -1);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            testRecipe.updateIngredientQuantity(testIngredient, 0);
        });
    }

    @Test
    public void cannotUpdateIngredientQuantityIfNotInRecipeTest() {
        Money price = Money.parse("NZD 3.00");
        Ingredient testIngredient = new IngredientImpl("ing3", "Unknown", UnitType.GRAM, price,
                ThreeValueLogic.UNKNOWN, ThreeValueLogic.UNKNOWN, ThreeValueLogic.UNKNOWN);
        assertThrows(IllegalArgumentException.class, () -> {
            testRecipe.updateIngredientQuantity(testIngredient, 2);
        });
    }

    @Test
    public void removeIngredientTest() {
        Money price = Money.parse("NZD 3.00");
        Ingredient testIngredient = new IngredientImpl("ing1", "Something", UnitType.GRAM, price,
                ThreeValueLogic.UNKNOWN, ThreeValueLogic.UNKNOWN, ThreeValueLogic.UNKNOWN);
        assertTrue(testRecipe.getIngredients().containsKey(testIngredient));
        testRecipe.removeIngredient(testIngredient);
        assertFalse(testRecipe.getIngredients().containsKey(testIngredient));
    }
}

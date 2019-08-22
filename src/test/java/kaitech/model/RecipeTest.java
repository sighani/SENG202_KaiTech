package kaitech.model;

import kaitech.util.ThreeValueLogic;
import kaitech.util.UnitType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class RecipeTest {
    private Recipe testRecipe;

    @BeforeEach
    public void init() {
        Ingredient testIngredient = new Ingredient("ing1", "Something", UnitType.GRAM, 200,
                ThreeValueLogic.UNKNOWN, ThreeValueLogic.UNKNOWN, ThreeValueLogic.UNKNOWN);
        Ingredient testIngredient2 = new Ingredient("ing2", "Something2", UnitType.GRAM, 300,
                ThreeValueLogic.UNKNOWN, ThreeValueLogic.UNKNOWN, ThreeValueLogic.UNKNOWN);
        Map<Ingredient, Integer> ingredientsMap = new HashMap<Ingredient, Integer>();
        ingredientsMap.put(testIngredient, 1);
        ingredientsMap.put(testIngredient2, 4);
        testRecipe = new Recipe(ingredientsMap, 2, 10, 1);
    }

    @Test
    public void calculateTotalCostTest() {
        assertEquals(1400, testRecipe.calculateTotalCost());
    }

    @Test
    public void addIngredientTest() {
        Ingredient testIngredient3 = new Ingredient("ing3", "Something3", UnitType.GRAM, 300,
                ThreeValueLogic.UNKNOWN, ThreeValueLogic.UNKNOWN, ThreeValueLogic.UNKNOWN);
        testRecipe.addIngredient(testIngredient3, 2);
        assertTrue(testRecipe.getIngredients().containsKey(testIngredient3));
        assertEquals(testRecipe.getIngredients().get(testIngredient3), 2);
    }

    @Test
    public void cannotAddIngredientWithNonPositiveAmtTest() {
        Ingredient testIngredient2 = new Ingredient("ing2", "Something2", UnitType.GRAM, 300,
                ThreeValueLogic.UNKNOWN, ThreeValueLogic.UNKNOWN, ThreeValueLogic.UNKNOWN);
        assertThrows(IllegalArgumentException.class, () -> {
            testRecipe.addIngredient(testIngredient2, -1);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            testRecipe.addIngredient(testIngredient2, 0);
        });
    }

    @Test
    public void updateIngredientQuantityTest() {
        Ingredient testIngredient = new Ingredient("ing1", "Something", UnitType.GRAM, 200,
                ThreeValueLogic.UNKNOWN, ThreeValueLogic.UNKNOWN, ThreeValueLogic.UNKNOWN);
        assertEquals(1, testRecipe.getIngredients().get(testIngredient));
        testRecipe.updateIngredientQuantity(testIngredient, 2);
        assertEquals(2, testRecipe.getIngredients().get(testIngredient));
    }

    @Test
    public void updateIngredientQuantityNonPositiveTest() {
        Ingredient testIngredient = new Ingredient("ing1", "Something", UnitType.GRAM, 200,
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
        Ingredient testIngredient = new Ingredient("ing3", "Unknown", UnitType.GRAM, 800,
                ThreeValueLogic.UNKNOWN, ThreeValueLogic.UNKNOWN, ThreeValueLogic.UNKNOWN);
        assertThrows(IllegalArgumentException.class, () -> {
            testRecipe.updateIngredientQuantity(testIngredient, 2);
        });
    }

    @Test
    public void removeIngredientTest() {
        Ingredient testIngredient = new Ingredient("ing1", "Something", UnitType.GRAM, 200,
                ThreeValueLogic.UNKNOWN, ThreeValueLogic.UNKNOWN, ThreeValueLogic.UNKNOWN);
        assertTrue(testRecipe.getIngredients().containsKey(testIngredient));
        testRecipe.removeIngredient(testIngredient);
        assertFalse(testRecipe.getIngredients().containsKey(testIngredient));
    }
}

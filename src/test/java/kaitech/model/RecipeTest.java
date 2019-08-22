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
        testRecipe = new Recipe(1, ingredientsMap, 2, 10, 1);
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
}

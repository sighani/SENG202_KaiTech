package kaitech.model;

import kaitech.util.ThreeValueLogic;
import kaitech.util.UnitType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MenuItemTest {
    private MenuItem testItem;
    private Ingredient testIngredient;

    @BeforeEach
    public void init() {
        testIngredient = new Ingredient("ing1", "Something", UnitType.GRAM, 200,
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
        Ingredient testIngredient2 = new Ingredient("ing2", "Something2", UnitType.GRAM, 300,
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
    public void printIngredientNamesTest() {
        assertEquals("[B1(Cheese Burger): Something]", testItem.ingredients());
    }

    @Test
    public void checkSufficientIngredientsTest() {
        Business testBusiness = new Business();
        Ingredient testIngredient = new Ingredient("ing1", "Something", UnitType.GRAM, 200,
                ThreeValueLogic.UNKNOWN, ThreeValueLogic.UNKNOWN, ThreeValueLogic.UNKNOWN);
        testBusiness.addIngredient(testIngredient, 5);
        assertTrue(testItem.checkSufficientIngredients(testBusiness));
    }

   /* @Test
    public void calculateNumServingsTest() {
        Business testBusiness = new Business();
        Ingredient testIngredient = new Ingredient("ing1", "Something", UnitType.GRAM, 200,
                ThreeValueLogic.UNKNOWN, ThreeValueLogic.UNKNOWN, ThreeValueLogic.UNKNOWN);
        testBusiness.addIngredient(testIngredient, 5);
        assertEquals(5, testItem.calculateNumServings());

    }*/
}

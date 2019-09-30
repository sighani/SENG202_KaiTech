package kaitech.model;

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

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MenuItemDietaryInfoTest {
    private MenuItem testItem;

    @BeforeEach
    public void init() {
        Map<Ingredient, Integer> ingredientsMap = new HashMap<>();
        Recipe testRecipe = new RecipeImpl("Recipe 1", 2, 10, 1, ingredientsMap);
        ArrayList<String> ingredientNames = new ArrayList<>();
        testItem = new MenuItemImpl("B1", "Cheese Burger", testRecipe, null, ingredientNames);
    }

    @Test
    public void testIsVeg() {
        Money price = Money.parse("NZD 3.00");
        Ingredient ing1 = new IngredientImpl("ing1", "Something1", UnitType.GRAM, price,
                ThreeValueLogic.YES, ThreeValueLogic.UNKNOWN, ThreeValueLogic.UNKNOWN);
        testItem.addIngredientToRecipe(ing1, 2);
        assertEquals(ThreeValueLogic.YES, testItem.getIsVeg());

        Ingredient ing2 = new IngredientImpl("ing2", "Something2", UnitType.GRAM, price,
                ThreeValueLogic.UNKNOWN, ThreeValueLogic.UNKNOWN, ThreeValueLogic.UNKNOWN);
        testItem.addIngredientToRecipe(ing2, 2);
        assertEquals(ThreeValueLogic.UNKNOWN, testItem.getIsVeg());

        Ingredient ing3 = new IngredientImpl("ing3", "Something3", UnitType.GRAM, price,
                ThreeValueLogic.NO, ThreeValueLogic.UNKNOWN, ThreeValueLogic.UNKNOWN);
        testItem.addIngredientToRecipe(ing3, 2);
        assertEquals(ThreeValueLogic.NO, testItem.getIsVeg());

        testItem.removeIngredientFromRecipe(ing1); // Remove YES, ingredients = [UNKNOWN, NO]
        testItem.removeIngredientFromRecipe(ing2); // Remove UNKNOWN, ingredients = [NO]
        testItem.addIngredientToRecipe(ing1, 2); // Add vegetarian ingredient onto not vegetarian menu item
        testItem.addIngredientToRecipe(ing2, 2); // Add unknown ingredient onto not vegetarian menu item
        assertEquals(ThreeValueLogic.NO, testItem.getIsVeg()); // Assert no change

        testItem.removeIngredientFromRecipe(ing3); // Remove NO, ingredients = [YES, UNKNOWN]
        assertEquals(ThreeValueLogic.UNKNOWN, testItem.getIsVeg()); // Assert change from NO to UNKNOWN
        testItem.addIngredientToRecipe(ing3, 3); // Add not vegetarian ingredient onto unknown vegetarian menu item
        assertEquals(ThreeValueLogic.NO, testItem.getIsVeg()); // Assert change from UNKNOWN to NO

        testItem.removeIngredientFromRecipe(ing2); // Remove UNKNOWN, ingredients = [YES, NO]
        testItem.removeIngredientFromRecipe(ing3); // Remove NO, ingredients = [YES]
        assertEquals(ThreeValueLogic.YES, testItem.getIsVeg()); // Assert change from NO to YES
        testItem.addIngredientToRecipe(ing3, 3); // Add not vegetarian ingredient onto vegetarian menu item
        assertEquals(ThreeValueLogic.NO, testItem.getIsVeg()); // Assert change from YES to NO
    }

    @Test
    public void testIsVegan() {
        Money price = Money.parse("NZD 3.00");
        Ingredient ing1 = new IngredientImpl("ing1", "Something1", UnitType.GRAM, price,
                ThreeValueLogic.UNKNOWN, ThreeValueLogic.YES, ThreeValueLogic.UNKNOWN);
        testItem.addIngredientToRecipe(ing1, 2);
        assertEquals(ThreeValueLogic.YES, testItem.getIsVegan());

        Ingredient ing2 = new IngredientImpl("ing2", "Something2", UnitType.GRAM, price,
                ThreeValueLogic.UNKNOWN, ThreeValueLogic.UNKNOWN, ThreeValueLogic.UNKNOWN);
        testItem.addIngredientToRecipe(ing2, 2);
        assertEquals(ThreeValueLogic.UNKNOWN, testItem.getIsVegan());

        Ingredient ing3 = new IngredientImpl("ing3", "Something3", UnitType.GRAM, price,
                ThreeValueLogic.UNKNOWN, ThreeValueLogic.NO, ThreeValueLogic.UNKNOWN);
        testItem.addIngredientToRecipe(ing3, 2);
        assertEquals(ThreeValueLogic.NO, testItem.getIsVegan());

        testItem.removeIngredientFromRecipe(ing1); // Remove YES, ingredients = [UNKNOWN, NO]
        testItem.removeIngredientFromRecipe(ing2); // Remove UNKNOWN, ingredients = [NO]
        testItem.addIngredientToRecipe(ing1, 2); // Add vegan ingredient onto not vegan menu item
        testItem.addIngredientToRecipe(ing2, 2); // Add unknown ingredient onto not vegan menu item
        assertEquals(ThreeValueLogic.NO, testItem.getIsVegan()); // Assert no change

        testItem.removeIngredientFromRecipe(ing3); // Remove NO, ingredients = [YES, UNKNOWN]
        assertEquals(ThreeValueLogic.UNKNOWN, testItem.getIsVegan()); // Assert change from NO to UNKNOWN
        testItem.addIngredientToRecipe(ing3, 3); // Add not vegan ingredient onto unknown vegan menu item
        assertEquals(ThreeValueLogic.NO, testItem.getIsVegan()); // Assert change from UNKNOWN to NO

        testItem.removeIngredientFromRecipe(ing2); // Remove UNKNOWN, ingredients = [YES, NO]
        testItem.removeIngredientFromRecipe(ing3); // Remove NO, ingredients = [YES]
        assertEquals(ThreeValueLogic.YES, testItem.getIsVegan()); // Assert change from NO to YES
        testItem.addIngredientToRecipe(ing3, 3); // Add not vegan ingredient onto vegan menu item
        assertEquals(ThreeValueLogic.NO, testItem.getIsVegan()); // Assert change from YES to NO
    }

    @Test
    public void testIsGF() {
        Money price = Money.parse("NZD 3.00");
        Ingredient ing1 = new IngredientImpl("ing1", "Something1", UnitType.GRAM, price,
                ThreeValueLogic.UNKNOWN, ThreeValueLogic.UNKNOWN, ThreeValueLogic.YES);
        testItem.addIngredientToRecipe(ing1, 2);
        assertEquals(ThreeValueLogic.YES, testItem.getIsGF());

        Ingredient ing2 = new IngredientImpl("ing2", "Something2", UnitType.GRAM, price,
                ThreeValueLogic.UNKNOWN, ThreeValueLogic.UNKNOWN, ThreeValueLogic.UNKNOWN);
        testItem.addIngredientToRecipe(ing2, 2);
        assertEquals(ThreeValueLogic.UNKNOWN, testItem.getIsGF());

        Ingredient ing3 = new IngredientImpl("ing3", "Something3", UnitType.GRAM, price,
                ThreeValueLogic.UNKNOWN, ThreeValueLogic.UNKNOWN, ThreeValueLogic.NO);
        testItem.addIngredientToRecipe(ing3, 2);
        assertEquals(ThreeValueLogic.NO, testItem.getIsGF());

        testItem.removeIngredientFromRecipe(ing1); // Remove YES, ingredients = [UNKNOWN, NO]
        testItem.removeIngredientFromRecipe(ing2); // Remove UNKNOWN, ingredients = [NO]
        testItem.addIngredientToRecipe(ing1, 2); // Add gluten free ingredient onto not gluten free menu item
        testItem.addIngredientToRecipe(ing2, 2); // Add unknown ingredient onto not gluten free menu item
        assertEquals(ThreeValueLogic.NO, testItem.getIsGF()); // Assert no change

        testItem.removeIngredientFromRecipe(ing3); // Remove NO, ingredients = [YES, UNKNOWN]
        assertEquals(ThreeValueLogic.UNKNOWN, testItem.getIsGF()); // Assert change from NO to UNKNOWN
        testItem.addIngredientToRecipe(ing3, 3); // Add not gluten free ingredient onto unknown gluten free menu item
        assertEquals(ThreeValueLogic.NO, testItem.getIsGF()); // Assert change from UNKNOWN to NO

        testItem.removeIngredientFromRecipe(ing2); // Remove UNKNOWN, ingredients = [YES, NO]
        testItem.removeIngredientFromRecipe(ing3); // Remove NO, ingredients = [YES]
        assertEquals(ThreeValueLogic.YES, testItem.getIsGF()); // Assert change from NO to YES
        testItem.addIngredientToRecipe(ing3, 3); // Add not gluten free ingredient onto gluten free menu item
        assertEquals(ThreeValueLogic.NO, testItem.getIsGF()); // Assert change from YES to NO
    }
}

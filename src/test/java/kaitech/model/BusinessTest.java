package kaitech.model;

import kaitech.util.PaymentType;
import kaitech.util.ThreeValueLogic;
import kaitech.util.UnitType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BusinessTest {
    private Business testBusiness;

    @BeforeEach
    public void init() {
        testBusiness = new Business();
    }

    @Test
    public void addSupplierTest() {
        Supplier testSupplier = new Supplier("sup1", "test", "40 Somewhere Road", "021000000");
        testBusiness.addSupplier(testSupplier);
        List testList = new ArrayList();
        testList.add(testSupplier);
        assertEquals(testList, testBusiness.getSuppliers());
    }

    @Test
    public void removeSupplierTest() {
        Supplier testSupplier = new Supplier("sup1", "test", "40 Somewhere Road", "021000000");
        testBusiness.addSupplier(testSupplier);
        assertEquals(1, testBusiness.getSuppliers().size());
        testBusiness.removeSupplier(testSupplier);
        assertEquals(0, testBusiness.getSuppliers().size());
    }

    @Test
    public void removeUnknownSupplier() {
        Supplier testSupplier = new Supplier("sup1", "test", "40 Somewhere Road", "021000000");
        testBusiness.addSupplier(testSupplier);
        List testList = new ArrayList();
        testList.add(testSupplier);

        Supplier unknownSupplier = new Supplier("Unknown", "Mysterious CO.", "40 Somewhere Road", "021000000");
        testBusiness.removeSupplier(unknownSupplier);
        assertEquals(testList, testBusiness.getSuppliers());
    }

    @Test
    public void addIngredientTest() {
        Ingredient testIngredient = new Ingredient("ing1", "Something", UnitType.GRAM, 0,
                ThreeValueLogic.UNKNOWN, ThreeValueLogic.UNKNOWN, ThreeValueLogic.UNKNOWN);
        assertFalse(testBusiness.getIngredients().containsKey(testIngredient));
        assertTrue(testBusiness.addIngredient(testIngredient));
        assertTrue(testBusiness.getIngredients().containsKey(testIngredient));
        assertEquals(0, testBusiness.getIngredients().get(testIngredient));

        assertFalse(testBusiness.addIngredient(testIngredient)); // Can't add an ingredient that is already in the list
    }

    @Test
    public void addIngredientWithAmtTest() {
        Ingredient testIngredient2 = new Ingredient("ing2", "Something2", UnitType.GRAM, 0,
                ThreeValueLogic.UNKNOWN, ThreeValueLogic.UNKNOWN, ThreeValueLogic.UNKNOWN);
        assertTrue(testBusiness.addIngredient(testIngredient2, 5));
        assertTrue(testBusiness.getIngredients().containsKey(testIngredient2));
        assertEquals(5, testBusiness.getIngredients().get(testIngredient2));
    }

    @Test
    public void cannotAddIngredientWithNegAmtTest() {
        Ingredient testIngredient = new Ingredient("ing2", "Something2", UnitType.GRAM, 0,
                ThreeValueLogic.UNKNOWN, ThreeValueLogic.UNKNOWN, ThreeValueLogic.UNKNOWN);
        assertThrows(IllegalArgumentException.class, () -> {
            testBusiness.addIngredient(testIngredient, -1);
        });
    }

    @Test
    public void increaseIngredientQuantity() {
        Ingredient testIngredient = new Ingredient("ing1", "Something", UnitType.GRAM, 0,
                ThreeValueLogic.UNKNOWN, ThreeValueLogic.UNKNOWN, ThreeValueLogic.UNKNOWN);
        testBusiness.addIngredient(testIngredient);
        assertEquals(0, testBusiness.getIngredients().get(testIngredient));
        assertTrue(testBusiness.increaseIngredientQuantity(testIngredient, 10));
        assertEquals(10, testBusiness.getIngredients().get(testIngredient));
    }

    @Test
    public void cannotIncIngredientQuantityIfNotInIngredientsTest() {
        Ingredient testIngredient2 = new Ingredient("ing2", "Something2", UnitType.GRAM, 0,
                ThreeValueLogic.UNKNOWN, ThreeValueLogic.UNKNOWN, ThreeValueLogic.UNKNOWN);
        assertFalse(testBusiness.increaseIngredientQuantity(testIngredient2, 10));
    }

    @Test
    public void cannotIncIngredientQuantityByInvalidAmt() {
        Ingredient testIngredient = new Ingredient("ing1", "Something", UnitType.GRAM, 0,
                ThreeValueLogic.UNKNOWN, ThreeValueLogic.UNKNOWN, ThreeValueLogic.UNKNOWN);
        testBusiness.addIngredient(testIngredient);
        assertThrows(IllegalArgumentException.class, () -> {
            testBusiness.increaseIngredientQuantity(testIngredient, 0);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            testBusiness.increaseIngredientQuantity(testIngredient, -1);
        });
    }

    @Test
    public void decreaseIngredientQuantity() {
        Ingredient testIngredient = new Ingredient("ing1", "Something", UnitType.GRAM, 0,
                ThreeValueLogic.UNKNOWN, ThreeValueLogic.UNKNOWN, ThreeValueLogic.UNKNOWN);
        testBusiness.addIngredient(testIngredient, 10);
        assertEquals(10, testBusiness.getIngredients().get(testIngredient));
        assertTrue(testBusiness.decreaseIngredientQuantity(testIngredient, 5));
        assertEquals(5, testBusiness.getIngredients().get(testIngredient));
        testBusiness.decreaseIngredientQuantity(testIngredient, 5);
        assertEquals(0, testBusiness.getIngredients().get(testIngredient));
    }

    @Test
    public void cannotDecIngredientQuantityIfNotInIngredientsTest() {
        Ingredient testIngredient2 = new Ingredient("ing2", "Something2", UnitType.GRAM, 0,
                ThreeValueLogic.UNKNOWN, ThreeValueLogic.UNKNOWN, ThreeValueLogic.UNKNOWN);
        assertFalse(testBusiness.decreaseIngredientQuantity(testIngredient2, 10));
    }

    @Test
    public void cannotDecIngredientQuantityByInvalidAmt() {
        Ingredient testIngredient = new Ingredient("ing1", "Something", UnitType.GRAM, 0,
                ThreeValueLogic.UNKNOWN, ThreeValueLogic.UNKNOWN, ThreeValueLogic.UNKNOWN);
        testBusiness.addIngredient(testIngredient);
        assertThrows(IllegalArgumentException.class, () -> {
            testBusiness.decreaseIngredientQuantity(testIngredient, 0);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            testBusiness.decreaseIngredientQuantity(testIngredient, -1);
        });
    }

    @Test
    public void cannotDecIngredientQuantityByAmtOverQuantityOwnedTest() {
        Ingredient testIngredient = new Ingredient("ing1", "Something", UnitType.GRAM, 0,
                ThreeValueLogic.UNKNOWN, ThreeValueLogic.UNKNOWN, ThreeValueLogic.UNKNOWN);
        testBusiness.addIngredient(testIngredient, 2);
        assertThrows(IllegalArgumentException.class, () -> {
            testBusiness.decreaseIngredientQuantity(testIngredient, 3);
        });
    }

    @Test
    public void updateTest() {
        Ingredient testIngredient = new Ingredient("ing1", "Something", UnitType.GRAM, 200,
                ThreeValueLogic.UNKNOWN, ThreeValueLogic.UNKNOWN, ThreeValueLogic.UNKNOWN);
        Map<Ingredient, Integer> ingredientsMap = new HashMap<Ingredient, Integer>();
        ingredientsMap.put(testIngredient, 1);
        Recipe testRecipe = new Recipe(ingredientsMap, 2, 10, 1);
        ArrayList<String> ingredientNames = new ArrayList<String>();
        ingredientNames.add(testIngredient.name());
        MenuItem testItem = new MenuItem("B1", "Cheese Burger", ingredientNames, testRecipe);

        LocalDate date = LocalDate.now();
        LocalTime time = LocalTime.now();
        Map<MenuItem, Integer> order = new HashMap<MenuItem, Integer>();
        order.put(testItem, 2);

        testBusiness.addIngredient(testIngredient, 5);
        assertEquals(5, testBusiness.getIngredients().get(testIngredient));
        Sale testSale = new Sale(order, date, time, PaymentType.CASH, null, 1000);
        assertEquals(3, testBusiness.getIngredients().get(testIngredient));
    }
}
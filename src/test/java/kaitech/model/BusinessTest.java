package kaitech.model;

import kaitech.util.ThreeValueLogic;
import kaitech.util.UnitType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

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
    public void addIngredientTest() {
        Ingredient testIngredient = new Ingredient("ing1", "Something", UnitType.GRAM, 0,
                ThreeValueLogic.UNKNOWN, ThreeValueLogic.UNKNOWN, ThreeValueLogic.UNKNOWN);
        assertFalse(testBusiness.getIngredients().containsKey(testIngredient));
        assertTrue(testBusiness.addIngredient(testIngredient));
        assertTrue(testBusiness.getIngredients().containsKey(testIngredient));
        assertEquals(0, testBusiness.getIngredients().get(testIngredient));

        Ingredient testIngredient2 = new Ingredient("ing2", "Something2", UnitType.GRAM, 0,
                ThreeValueLogic.UNKNOWN, ThreeValueLogic.UNKNOWN, ThreeValueLogic.UNKNOWN);
        assertTrue(testBusiness.addIngredient(testIngredient2, 5));
        assertTrue(testBusiness.getIngredients().containsKey(testIngredient2));
        assertEquals(5, testBusiness.getIngredients().get(testIngredient2));

        assertFalse(testBusiness.addIngredient(testIngredient));
    }

    @Test
    public void increaseIngredientQuantity() {
        Ingredient testIngredient = new Ingredient("ing1", "Something", UnitType.GRAM, 0,
                ThreeValueLogic.UNKNOWN, ThreeValueLogic.UNKNOWN, ThreeValueLogic.UNKNOWN);
        testBusiness.addIngredient(testIngredient);
        assertEquals(0, testBusiness.getIngredients().get(testIngredient));
        assertTrue(testBusiness.increaseIngredientQuantity(testIngredient, 10));
        assertEquals(10, testBusiness.getIngredients().get(testIngredient));

        Ingredient testIngredient2 = new Ingredient("ing2", "Something2", UnitType.GRAM, 0,
                ThreeValueLogic.UNKNOWN, ThreeValueLogic.UNKNOWN, ThreeValueLogic.UNKNOWN);
        assertFalse(testBusiness.increaseIngredientQuantity(testIngredient2, 10));
    }

    @Test
    public void decreaseIngredientQuantity() {
        Ingredient testIngredient = new Ingredient("ing1", "Something", UnitType.GRAM, 0,
                ThreeValueLogic.UNKNOWN, ThreeValueLogic.UNKNOWN, ThreeValueLogic.UNKNOWN);
        testBusiness.addIngredient(testIngredient, 10);
        assertEquals(10, testBusiness.getIngredients().get(testIngredient));
        assertTrue(testBusiness.decreaseIngredientQuantity(testIngredient, 5));
        assertEquals(5, testBusiness.getIngredients().get(testIngredient));

        Ingredient testIngredient2 = new Ingredient("ing2", "Something2", UnitType.GRAM, 0,
                ThreeValueLogic.UNKNOWN, ThreeValueLogic.UNKNOWN, ThreeValueLogic.UNKNOWN);
        assertFalse(testBusiness.decreaseIngredientQuantity(testIngredient2, 10));
    }
}

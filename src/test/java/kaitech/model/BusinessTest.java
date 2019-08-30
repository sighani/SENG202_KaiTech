package kaitech.model;

import kaitech.api.model.*;
import kaitech.util.PaymentType;
import kaitech.util.ThreeValueLogic;
import kaitech.util.UnitType;
import org.joda.money.Money;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class BusinessTest {
    private Business testBusiness;

    @BeforeEach
    public void init() {
        BusinessImpl.reset();
        testBusiness = BusinessImpl.getInstance();
    }

    @Test
    public void addSupplierTest() {
        Supplier testSupplier = new SupplierImpl("sup1", "test", "40 Somewhere Road", "021000000");
        testBusiness.addSupplier(testSupplier);
        List<Supplier> testList = new ArrayList<>();
        testList.add(testSupplier);
        assertEquals(testList, testBusiness.getSuppliers());
    }

    @Test
    public void removeSupplierTest() {
        Supplier testSupplier = new SupplierImpl("sup1", "test", "40 Somewhere Road", "021000000");
        testBusiness.addSupplier(testSupplier);
        assertEquals(1, testBusiness.getSuppliers().size());
        testBusiness.removeSupplier(testSupplier);
        assertEquals(0, testBusiness.getSuppliers().size());
    }

    @Test
    public void removeUnknownSupplier() {
        Supplier testSupplier = new SupplierImpl("sup1", "test", "40 Somewhere Road", "021000000");
        testBusiness.addSupplier(testSupplier);
        List<Supplier> testList = new ArrayList<>();
        testList.add(testSupplier);

        Supplier unknownSupplier = new SupplierImpl("Unknown", "Mysterious CO.", "40 Somewhere Road", "021000000");
        testBusiness.removeSupplier(unknownSupplier);
        assertEquals(testList, testBusiness.getSuppliers());
    }

    @Test
    public void addIngredientTest() {
        Money price = Money.parse("NZD 0");
        Ingredient testIngredient = new IngredientImpl("ing1", "Something", UnitType.GRAM, price,
                ThreeValueLogic.UNKNOWN, ThreeValueLogic.UNKNOWN, ThreeValueLogic.UNKNOWN);
        assertFalse(testBusiness.getIngredients().containsKey(testIngredient));
        assertTrue(testBusiness.addIngredient(testIngredient));
        assertTrue(testBusiness.getIngredients().containsKey(testIngredient));
        assertEquals(0, testBusiness.getIngredients().get(testIngredient));

        assertFalse(testBusiness.addIngredient(testIngredient)); // Can't add an ingredient that is already in the list
    }

    @Test
    public void addIngredientWithAmtTest() {
        Money price = Money.parse("NZD 0");
        Ingredient testIngredient2 = new IngredientImpl("ing2", "Something2", UnitType.GRAM, price,
                ThreeValueLogic.UNKNOWN, ThreeValueLogic.UNKNOWN, ThreeValueLogic.UNKNOWN);
        assertTrue(testBusiness.addIngredient(testIngredient2, 5));
        assertTrue(testBusiness.getIngredients().containsKey(testIngredient2));
        assertEquals(5, testBusiness.getIngredients().get(testIngredient2));
    }

    @Test
    public void cannotAddIngredientWithNegAmtTest() {
        Money price = Money.parse("NZD 0");
        Ingredient testIngredient = new IngredientImpl("ing2", "Something2", UnitType.GRAM, price,
                ThreeValueLogic.UNKNOWN, ThreeValueLogic.UNKNOWN, ThreeValueLogic.UNKNOWN);
        assertThrows(IllegalArgumentException.class, () -> testBusiness.addIngredient(testIngredient, -1));
    }

    @Test
    public void increaseIngredientQuantity() {
        Money price = Money.parse("NZD 0");
        Ingredient testIngredient = new IngredientImpl("ing1", "Something", UnitType.GRAM, price,
                ThreeValueLogic.UNKNOWN, ThreeValueLogic.UNKNOWN, ThreeValueLogic.UNKNOWN);
        testBusiness.addIngredient(testIngredient);
        assertEquals(0, testBusiness.getIngredients().get(testIngredient));
        assertTrue(testBusiness.increaseIngredientQuantity(testIngredient, 10));
        assertEquals(10, testBusiness.getIngredients().get(testIngredient));
    }

    @Test
    public void cannotIncIngredientQuantityIfNotInIngredientsTest() {
        Money price = Money.parse("NZD 0");
        Ingredient testIngredient2 = new IngredientImpl("ing2", "Something2", UnitType.GRAM, price,
                ThreeValueLogic.UNKNOWN, ThreeValueLogic.UNKNOWN, ThreeValueLogic.UNKNOWN);
        assertFalse(testBusiness.increaseIngredientQuantity(testIngredient2, 10));
    }

    @Test
    public void cannotIncIngredientQuantityByInvalidAmt() {
        Money price = Money.parse("NZD 0");
        Ingredient testIngredient = new IngredientImpl("ing1", "Something", UnitType.GRAM, price,
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
        Money price = Money.parse("NZD 0");
        Ingredient testIngredient = new IngredientImpl("ing1", "Something", UnitType.GRAM, price,
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
        Money price = Money.parse("NZD 0");
        Ingredient testIngredient2 = new IngredientImpl("ing2", "Something2", UnitType.GRAM, price,
                ThreeValueLogic.UNKNOWN, ThreeValueLogic.UNKNOWN, ThreeValueLogic.UNKNOWN);
        assertFalse(testBusiness.decreaseIngredientQuantity(testIngredient2, 10));
    }

    @Test
    public void cannotDecIngredientQuantityByInvalidAmt() {
        Money price = Money.parse("NZD 0");
        Ingredient testIngredient = new IngredientImpl("ing1", "Something", UnitType.GRAM, price,
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
        Money price = Money.parse("NZD 0");
        Ingredient testIngredient = new IngredientImpl("ing1", "Something", UnitType.GRAM, price,
                ThreeValueLogic.UNKNOWN, ThreeValueLogic.UNKNOWN, ThreeValueLogic.UNKNOWN);
        testBusiness.addIngredient(testIngredient, 2);
        assertThrows(IllegalArgumentException.class, () -> {
            testBusiness.decreaseIngredientQuantity(testIngredient, 3);
        });
    }

    @Test
    public void updateTest() {
        Money price = Money.parse("NZD 2.00");
        Ingredient testIngredient = new IngredientImpl("ing1", "Something", UnitType.GRAM, price,
                ThreeValueLogic.UNKNOWN, ThreeValueLogic.UNKNOWN, ThreeValueLogic.UNKNOWN);
        Map<Ingredient, Integer> ingredientsMap = new HashMap<>();
        ingredientsMap.put(testIngredient, 1);
        Recipe testRecipe = new RecipeImpl(ingredientsMap, 2, 10, 1);
        ArrayList<String> ingredientNames = new ArrayList<>();
        ingredientNames.add(testIngredient.name());
        MenuItem testItem = new MenuItemImpl("B1", "CheeseBurger", ingredientNames, testRecipe, null);

        LocalDate date = LocalDate.now();
        LocalTime time = LocalTime.now();
        Map<MenuItem, Integer> order = new HashMap<>();
        order.put(testItem, 2);

        Money totalPrice = Money.parse("NZD 10.00");
        testBusiness.addIngredient(testIngredient, 5);
        assertEquals(5, testBusiness.getIngredients().get(testIngredient));
        Sale testSale = new SaleImpl(order, date, time, PaymentType.CASH, null, totalPrice, testBusiness);
        assertEquals(3, testBusiness.getIngredients().get(testIngredient));


        Money price2 = Money.parse("NZD 3.00");
        Ingredient testIngredient2 = new IngredientImpl("ing2", "Something2", UnitType.GRAM, price2,
                ThreeValueLogic.UNKNOWN, ThreeValueLogic.UNKNOWN, ThreeValueLogic.UNKNOWN);
        Map<Ingredient, Integer> ingredientsMap2 = new HashMap<>();
        ingredientsMap2.put(testIngredient2, 1);
        ingredientsMap2.put(testIngredient, 3);
        Recipe testRecipe2 = new RecipeImpl(ingredientsMap2, 4, 11, 1);
        ArrayList<String> ingredientNames2 = new ArrayList<>();
        ingredientNames2.add(testIngredient.name());
        ingredientNames2.add(testIngredient2.name());
        MenuItem testItem2 = new MenuItemImpl("B2", "HamBurger", ingredientNames2, testRecipe2, null);

        order.put(testItem2, 3);
        testBusiness.addIngredient(testIngredient2, 3);
        testBusiness.increaseIngredientQuantity(testIngredient, 10);

        testBusiness.addIngredient(testIngredient, 5);
        Sale testSale2 = new SaleImpl(order, date, time, PaymentType.CASH, null, totalPrice, testBusiness);
        assertEquals(2, testBusiness.getIngredients().get(testIngredient));
        assertEquals(0, testBusiness.getIngredients().get(testIngredient2));
    }

    @Test
    public void setPinTest() {
        assertNull(testBusiness.getPin());
        testBusiness.setPin("0000");
        assertEquals("0000", testBusiness.getPin());
    }

    @Test
    public void setPinTooManyDigitsTest() {
        assertThrows(IllegalArgumentException.class, () -> testBusiness.setPin("00000"),
                "The pin should contain 4 digits only");
    }

    @Test
    public void setPinTooLittleDigitsTest() {
        assertThrows(IllegalArgumentException.class, () -> testBusiness.setPin("0"),
                "The pin should contain 4 digits only");
    }

    @Test
    public void setPinWithLettersTest() {
        assertThrows(IllegalArgumentException.class, () -> testBusiness.setPin("t35t"),
                "The pin should contain digits only");
    }

    @Test
    public void setPinWithNonAlphanumericValuesTest() {
        assertThrows(IllegalArgumentException.class, () -> testBusiness.setPin("@@@@"),
                "The pin should contain digits only.");
    }

    @Test
    public void logInTest() {
        testBusiness.setPin("4692");
        assertTrue(testBusiness.logIn("4692"));
    }

    @Test
    public void logInFailTest() {
        testBusiness.setPin("4692");
        assertFalse(testBusiness.logIn("4693"));
        assertFalse(testBusiness.logIn("4629"));
    }

    @Test
    public void cannotLogInWhenAlreadyLoggedInTest() {
        testBusiness.setPin("4692");
        assertTrue(testBusiness.logIn("4692"));
        assertThrows(IllegalStateException.class, () -> testBusiness.logIn("4692"),
                "The user is already logged in.");
    }

    @Test
    public void cannotLogInWhenPinNotSetTest() {
        assertThrows(IllegalStateException.class, () -> testBusiness.logIn("4692"),
                "A pin has not been set yet.");
    }

    @Test
    public void logOutTest() {
        testBusiness.setPin("4692");
        testBusiness.logIn("4692");
        assertTrue(testBusiness.isLoggedIn());
        testBusiness.logOut();
        assertFalse(testBusiness.isLoggedIn());
    }
}
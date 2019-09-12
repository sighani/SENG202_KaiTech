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
    public void setPinTooFewDigitsTest() {
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

    @Test
    public void getAffectedMenuItemsTest() {
        Money price = Money.parse("NZD 3.00");
        Ingredient testIngredient = new IngredientImpl("ing1", "Something", UnitType.GRAM, price,
                ThreeValueLogic.UNKNOWN, ThreeValueLogic.UNKNOWN, ThreeValueLogic.UNKNOWN);
        Ingredient testIngredient2 = new IngredientImpl("ing2", "Something2", UnitType.GRAM, price,
                ThreeValueLogic.UNKNOWN, ThreeValueLogic.UNKNOWN, ThreeValueLogic.UNKNOWN);
        Map<Ingredient, Integer> ingredientsMap = new HashMap<>();
        ingredientsMap.put(testIngredient, 1);
        Map<Ingredient, Integer> ingredientsMap2 = new HashMap<>();
        ingredientsMap2.put(testIngredient2, 1);
        Recipe testRecipe = new RecipeImpl(2, 10, 1, ingredientsMap);
        Recipe testRecipe2 = new RecipeImpl(2, 10, 1, ingredientsMap2);
        ArrayList<String> ingredientNames = new ArrayList<>();
        ingredientNames.add(testIngredient.getName());
        MenuItem testItem = new MenuItemImpl("B1", "Cheese Burger", testRecipe, price, ingredientNames);
        MenuItem testItem2 = new MenuItemImpl("B2", "HamBurger", testRecipe2, price, ingredientNames);
        ArrayList<MenuItem> toCmp = new ArrayList<>();
        toCmp.add(testItem);
        testBusiness.getMenuItemTable().getOrAddItem(testItem);
        testBusiness.getMenuItemTable().getOrAddItem(testItem2);
        assertTrue(testBusiness.getAffectedMenuItems(testIngredient).size() == 1);
        assertEquals(testItem.getCode(), testBusiness.getAffectedMenuItems(testIngredient).get(0).getCode());
    }

    @Test
    public void noAffectedMenuItemsTest() {
        Money price = Money.parse("NZD 3.00");
        Ingredient testIngredient = new IngredientImpl("ing1", "Something", UnitType.GRAM, price,
                ThreeValueLogic.UNKNOWN, ThreeValueLogic.UNKNOWN, ThreeValueLogic.UNKNOWN);
        Ingredient testIngredient2 = new IngredientImpl("ing2", "Something2", UnitType.GRAM, price,
                ThreeValueLogic.UNKNOWN, ThreeValueLogic.UNKNOWN, ThreeValueLogic.UNKNOWN);
        Map<Ingredient, Integer> ingredientsMap = new HashMap<>();
        ingredientsMap.put(testIngredient, 1);
        Recipe testRecipe = new RecipeImpl(2, 10, 1, ingredientsMap);
        ArrayList<String> ingredientNames = new ArrayList<>();
        ingredientNames.add(testIngredient.getName());
        MenuItem testItem = new MenuItemImpl("B1", "Cheese Burger", testRecipe, price, ingredientNames);
        ArrayList<MenuItem> toCmp = new ArrayList<>();
        toCmp.add(testItem);
        testBusiness.getMenuItemTable().getOrAddItem(testItem);
        assertEquals(0, testBusiness.getAffectedMenuItems(testIngredient2).size());
    }
}
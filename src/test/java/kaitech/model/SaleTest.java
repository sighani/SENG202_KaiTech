package kaitech.model;

import kaitech.api.model.Ingredient;
import kaitech.api.model.MenuItem;
import kaitech.api.model.Recipe;
import kaitech.api.model.Sale;
import kaitech.util.ThreeValueLogic;
import kaitech.util.UnitType;
import org.joda.money.Money;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class SaleTest {
    Map<MenuItem, Integer> itemsOrdered;

    @BeforeEach
    public void init() {
        Money price = Money.parse("NZD 3.00");
        Ingredient testIngredient = new IngredientImpl("ing1", "Something", UnitType.GRAM, price,
                ThreeValueLogic.UNKNOWN, ThreeValueLogic.UNKNOWN, ThreeValueLogic.UNKNOWN);
        Map<Ingredient, Integer> ingredientsMap = new HashMap<>();
        ingredientsMap.put(testIngredient, 1);
        Recipe testRecipe = new RecipeImpl(ingredientsMap, 2, 10, 1);
        ArrayList<String> ingredientNames = new ArrayList<>();
        ingredientNames.add(testIngredient.getName());
        Money itemPrice = Money.parse("NZD 5.00");
        Money itemPrice2 = Money.parse("NZD 3.50");
        MenuItem testItem = new MenuItemImpl("B1", "Cheese Burger", ingredientNames, testRecipe, itemPrice);
        MenuItem testItem2 = new MenuItemImpl("B2", "Hot Dog", ingredientNames, testRecipe, itemPrice2);
        HashMap<MenuItem, Integer> order = new HashMap<>();
        order.put(testItem, 1);
        order.put(testItem2, 2);
        itemsOrdered = order;
    }

    @Test
    public void calculateTotalCostTest() {
        Money expectedCost = Money.parse("NZD 12.00");
        assertEquals(expectedCost, Sale.calculateTotalCost(itemsOrdered));

        HashMap<MenuItem, Integer> emptyOrder = new HashMap<>();
        Money expectedCost2 = Money.parse("NZD 0");
        assertEquals(expectedCost2, Sale.calculateTotalCost(emptyOrder));
    }

    @Test
    public void calculateChangeTest() {
        Money total = Money.parse("NZD 6.50");
        Money amountPaid = Money.parse("NZD 10.00");
        Money expectedChange = Money.parse("NZD 3.50");
        assertEquals(expectedChange, Sale.calculateChange(total, amountPaid));

        Money amountPaid2 = Money.parse("NZD 6.50");
        Money expectedChange2 = Money.parse("NZD 0");
        assertEquals(expectedChange2, Sale.calculateChange(total, amountPaid2));
    }

    @Test
    public void insufficientPaymentTest() {
        Money total = Money.parse("NZD 6.50");
        Money amountPaid = Money.parse("NZD 5.00");
        assertThrows(IllegalArgumentException.class, () -> Sale.calculateChange(total, amountPaid));
    }
}

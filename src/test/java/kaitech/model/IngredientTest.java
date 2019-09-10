package kaitech.model;

import kaitech.api.model.Ingredient;
import kaitech.util.ThreeValueLogic;
import kaitech.util.UnitType;
import org.joda.money.Money;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class IngredientTest {
    @Test
    public void equalsTest() {
        Money price = Money.parse("NZD 0.05");
        Ingredient testIngredient = new IngredientImpl("Onion", "Diced Onion", UnitType.GRAM, price,
                ThreeValueLogic.YES, ThreeValueLogic.YES, ThreeValueLogic.UNKNOWN);
        Ingredient testIngredient2 = new IngredientImpl("Onion", "Diced Onion", UnitType.GRAM, price,
                ThreeValueLogic.YES, ThreeValueLogic.YES, ThreeValueLogic.UNKNOWN);
        assertEquals(testIngredient, testIngredient2);
    }

    @Test
    public void equalsIfCodesAreIdenticalTest() {
        Money price = Money.parse("NZD 0.05");
        Money price2 = Money.parse("NZD 0.20");
        Ingredient testIngredient = new IngredientImpl("Ing1", "Diced Onion", UnitType.GRAM, price,
                ThreeValueLogic.YES, ThreeValueLogic.YES, ThreeValueLogic.UNKNOWN);
        Ingredient testIngredient2 = new IngredientImpl("Ing1", "Vinegar", UnitType.ML, price2,
                ThreeValueLogic.NO, ThreeValueLogic.NO, ThreeValueLogic.NO);
        assertEquals(testIngredient, testIngredient2);

        Ingredient testIngredient3 = new IngredientImpl("Ing2", "Vinegar", UnitType.ML, price2,
                ThreeValueLogic.NO, ThreeValueLogic.NO, ThreeValueLogic.NO);
        assertNotEquals(testIngredient, testIngredient3);
    }
}

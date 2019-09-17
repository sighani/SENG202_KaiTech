package kaitech.io;

import kaitech.api.model.Ingredient;
import kaitech.util.ThreeValueLogic;
import kaitech.util.UnitType;
import org.joda.money.Money;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.assertEquals;

public class LoadIngredientsTest {

    private Map<String, Ingredient> loadedIngredients;

    @Before
    public void testLoadIngredientFile(){
        String fileName = "resources/data/Ingredients.xml";
        LoadData.LoadIngredients(fileName);
        loadedIngredients = LoadData.ingredientsList();
        assertEquals("Cheching all ingredients are present", 30, loadedIngredients.size());
    }

    @Test
    public void testCodes() {
        String testCode1 = loadedIngredients.get("Salt").getCode();
        String testCode2 = loadedIngredients.get("Lettuce").getCode();
        assertEquals("Checking salt ingredient has correct code", "Salt", testCode1);
        assertEquals("Checking Lettuce has correct code", "Lettuce", testCode2);
    }

    @Test
    public void testNames(){
        String testName1 = loadedIngredients.get("Chicken").getName();
        String testName2 = loadedIngredients.get("CStock").getName();
        assertEquals("Checking code chicken name", "Chicken breast, diced", testName1);
        assertEquals("Checking chicken stock name", "Chicken stock", testName2);
    }

    @Test
    public void testUnits(){
        //a good night out
        UnitType testUnit1 = loadedIngredients.get("Vodka").getUnit();
        UnitType testUnit2 = loadedIngredients.get("Herbs").getUnit();
        UnitType testUnit3 = loadedIngredients.get("Cheese").getUnit();
        assertEquals("Vodka is mesured in ml", UnitType.ML, testUnit1);
        assertEquals("Herbs are mesured in grams", UnitType.GRAM, testUnit2);
        assertEquals("Cheese is mesured in slices", UnitType.COUNT, testUnit3);
    }

    @Test
    public void testPrice(){
        Money testPrice = loadedIngredients.get("Rice").getPrice();
        assertEquals("Checking price is set to money.zero", Money.parse("NZD 0.00"), testPrice);
    }

    @Test
    public void testIsGf(){
        ThreeValueLogic testGf1 = loadedIngredients.get("BBun").getIsGF();
        ThreeValueLogic testGf2 = loadedIngredients.get("Lettuce").getIsGF();
        assertEquals("Burger bun should be not gf", ThreeValueLogic.NO, testGf1);
        assertEquals("Dont really know if lettuce is gf", ThreeValueLogic.UNKNOWN, testGf2);
    }

    @Test
    public void testIsVeg(){
        //how does our vegan/veg conversion work
        ThreeValueLogic testVeg1 = loadedIngredients.get("BPat").getIsVeg();
        ThreeValueLogic testVeg2 = loadedIngredients.get("Onion").getIsVeg();
        ThreeValueLogic testVeg3 = loadedIngredients.get("Bacon").getIsVeg();
        assertEquals("Beef patty is not veg", ThreeValueLogic.NO, testVeg1);
        assertEquals("Onions are veg", ThreeValueLogic.YES, testVeg2);
        assertEquals("Bacon is undefined as veg/not veg, should defualt to no", ThreeValueLogic.NO, testVeg3);
    }

    @Test
    public void testIsVegan(){
        ThreeValueLogic testVegan1 = loadedIngredients.get("BPat").getIsVegan();
        ThreeValueLogic testVegan2 = loadedIngredients.get("Rice").getIsVegan();
        ThreeValueLogic testVegan3 = loadedIngredients.get("LemCan").getIsVegan();
        assertEquals("Beef is not vegan", ThreeValueLogic.NO, testVegan1);
        assertEquals("Dont know if rice is vegan, defaults to no", ThreeValueLogic.NO, testVegan2);
    }

}

package kaitech.io;

import kaitech.model.Ingredient;
import kaitech.parsing.IngredientLoader;
import kaitech.util.ThreeValueLogic;
import kaitech.util.UnitType;
import org.junit.*;

import java.util.Map;

import static org.junit.Assert.assertEquals;

public class LoadIngredientsTest {

    private Map<String, Ingredient> loadedIngredients;

    @Before
    public void testLoadIngredientFile(){
        String fileName = "resources/data/Ingredients.xml";
        IngredientLoader i = new IngredientLoader(fileName, true);
        loadedIngredients = i.getIngredients();
        assertEquals("Cheching all ingredients are present", 30, loadedIngredients.size());
    }

    @Test
    public void testCodes() {
        String testCode1 = loadedIngredients.get("Salt").code();
        String testCode2 = loadedIngredients.get("Lettuce").code();
        assertEquals("Checking salt ingredient has correct code", "Salt", testCode1);
        assertEquals("Checking Lettuce has correct code", "Lettuce", testCode2);
    }

    @Test
    public void testNames(){
        String testName1 = loadedIngredients.get("Chicken").name();
        String testName2 = loadedIngredients.get("CStock").name();
        assertEquals("Checking code chicken name", "Chicken breast, diced", testName1);
        assertEquals("Checking chicken stock name", "Chicken stock", testName2);
    }

    @Test
    public void testUnits(){
        //a good night out
        UnitType testUnit1 = loadedIngredients.get("Vodka").unit();
        UnitType testUnit2 = loadedIngredients.get("Herbs").unit();
        UnitType testUnit3 = loadedIngredients.get("Cheese").unit();
        assertEquals("Vodka is mesured in ml", UnitType.ML, testUnit1);
        assertEquals("Herbs are mesured in grams", UnitType.GRAM, testUnit2);
        assertEquals("Cheese is mesured in slices", UnitType.COUNT, testUnit3);
    }

    @Test
    public void testPrice(){
        int testPrice = loadedIngredients.get("Rice").getPrice();
        assertEquals("Checking price is set to -1", -1, testPrice);
    }

    @Test
    public void testIsGf(){
        ThreeValueLogic testGf1 = loadedIngredients.get("BBun").isGF();
        ThreeValueLogic testGf2 = loadedIngredients.get("Lettuce").isGF();
        assertEquals("Burger bun should be not gf", ThreeValueLogic.NO, testGf1);
        assertEquals("Dont really know if lettuce is gf", ThreeValueLogic.UNKNOWN, testGf2);
    }

    @Test
    public void testIsVeg(){
        //how does our vegan/veg conversion work
        ThreeValueLogic testVeg1 = loadedIngredients.get("BPat").isVeg();
        ThreeValueLogic testVeg2 = loadedIngredients.get("Onion").isVeg();
        ThreeValueLogic testVeg3 = loadedIngredients.get("Bacon").isVeg();
        assertEquals("Beef patty is not veg", ThreeValueLogic.NO, testVeg1);
        assertEquals("Onions are veg", ThreeValueLogic.YES, testVeg2);
        assertEquals("Bacon is undefined as veg/not veg", ThreeValueLogic.UNKNOWN, testVeg3);
    }

    @Test
    public void testIsVegan(){
        ThreeValueLogic testVegan1 = loadedIngredients.get("BPat").isVegan();
        ThreeValueLogic testVegan2 = loadedIngredients.get("Rice").isVegan();
        ThreeValueLogic testVegan3 = loadedIngredients.get("LemCan").isVegan();
        assertEquals("Beef is not vegan", ThreeValueLogic.NO, testVegan1);
        assertEquals("Dont know if rice is vegan", ThreeValueLogic.UNKNOWN, testVegan2);
        assertEquals("Dont know if lemonade is vegan", ThreeValueLogic.UNKNOWN, testVegan3);
    }

}
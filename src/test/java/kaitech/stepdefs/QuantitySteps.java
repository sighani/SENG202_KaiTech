package kaitech.stepdefs;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.java.en.And;
import kaitech.api.model.*;
import kaitech.model.BusinessImpl;
import kaitech.model.IngredientImpl;
import kaitech.model.MenuItemImpl;
import kaitech.model.RecipeImpl;
import kaitech.util.ThreeValueLogic;
import kaitech.util.UnitType;
import org.joda.money.Money;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

public class QuantitySteps {
    private MenuItem cheeseBurger;
    private Business business;
    Ingredient cheese;
    Ingredient meat;
    Ingredient bun;

    @Given("A cheeseburger has a recipe consisting of {int} cheese slice, {int} meat patty, and {int} bun")
    public void aCheeseburgerHasARecipeConsistingOfCheeseSliceMeatPattyAndBun(Integer int1, Integer int2, Integer int3) {
        Money genericPrice = Money.parse("NZD 0.30");
        cheese = new IngredientImpl("Cheese Slice", "Cheese Slice", UnitType.COUNT, genericPrice, ThreeValueLogic.YES, ThreeValueLogic.NO, ThreeValueLogic.NO);
        meat = new IngredientImpl("Meat Patty", "Meat Patty", UnitType.COUNT, genericPrice, ThreeValueLogic.NO, ThreeValueLogic.NO, ThreeValueLogic.NO);
        bun = new IngredientImpl("Burger Bun", "Burger Bun", UnitType.COUNT, genericPrice, ThreeValueLogic.YES, ThreeValueLogic.YES, ThreeValueLogic.NO);
        HashMap<Ingredient, Integer> ingredients = new HashMap<>();
        ingredients.put(cheese, int1);
        ingredients.put(meat, int2);
        ingredients.put(bun, int3);
        Recipe cheeseBurgerRecipe = new RecipeImpl("Recipe 1", ingredients);
        Money cost = Money.parse("NZD 5");
        cheeseBurger = new MenuItemImpl("CHB", cheeseBurgerRecipe, cost);
    }

    @And("The business has {int} cheese slices, {int} meat patties, and {int} bun")
    public void theBusinessHasCheeseSlicesMeatPattiesAndBun(Integer int1, Integer int2, Integer int3) {
        business = BusinessImpl.getInstance();
        BusinessImpl.reset();
        business = BusinessImpl.getInstance();
        business.getInventoryTable().putInventory(cheese, int1);
        business.getInventoryTable().putInventory(meat, int2);
        business.getInventoryTable().putInventory(bun, int3);
    }

    @Then("The business has enough ingredients")
    public void theBusinessHasEnoughIngredients() {
        assertTrue(cheeseBurger.checkSufficientIngredients(business));
    }

    @Then("The business doesn't enough ingredients")
    public void theBusinessDoesnTEnoughIngredients() {
        assertFalse(cheeseBurger.checkSufficientIngredients(business));
    }


    @When("The business has {int} cheese slices, {int} meat patties, and {int} buns")
    public void theBusinessHasCheeseSlicesMeatPattiesAndBuns(Integer int1, Integer int2, Integer int3) {
        BusinessImpl.reset();
        business = BusinessImpl.getInstance();
        business.getInventoryTable().putInventory(cheese, int1);
        business.getInventoryTable().putInventory(meat, int2);
        business.getInventoryTable().putInventory(bun, int3);
    }

    @Then("The business can serve {int} cheeseburgers")
    public void theBusinessCanServeCheeseburgers(Integer int1) {
        assertEquals(int1, cheeseBurger.calculateNumServings(business));
    }

}

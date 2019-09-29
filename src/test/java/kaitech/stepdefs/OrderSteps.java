package kaitech.stepdefs;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import kaitech.api.model.MenuItem;
import kaitech.api.model.Sale;
import kaitech.model.MenuItemImpl;
import kaitech.model.SaleImpl;
import kaitech.util.PaymentType;
import org.joda.money.Money;

import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class OrderSteps {
    MenuItem cheeseburger;
    MenuItem drink;
    Sale order;
    HashMap<MenuItem, Integer> itemsOrdered = new HashMap<>();

    @Given("A cheeseburger costs ${double}")
    public void aCheeseburgerCosts(Double double1) {
        Money price = Money.parse("NZD " + double1);
        ArrayList<String> ingredients = new ArrayList<>();
        ingredients.add("None");
        cheeseburger = new MenuItemImpl("Burg", "CheeseBurger", null, price, ingredients);
    }

    @And("A drink costs ${double}")
    public void aDrinkCosts$(Double double1) {
        Money price2 = Money.parse("NZD " + double1);
        ArrayList<String> ingredients = new ArrayList<>();
        ingredients.add("None");
        drink = new MenuItemImpl("Drink1", "Drink", null, price2, ingredients);
    }

    @When("An order is put in for {int} cheeseburgers and {int} drink")
    public void anOrderIsPutInForCheeseburgersAndDrink(Integer int1, Integer int2) {
        itemsOrdered.put(cheeseburger, int1);
        itemsOrdered.put(drink, int2);
        Money cost = Sale.calculateTotalCost(itemsOrdered);
        order = new SaleImpl(null, null, cost, PaymentType.CASH, itemsOrdered);
    }

    @Then("The total chargeable is ${double}")
    public void theTotalChargeableIs$(Double double1) {
        Money expectedTotal = Money.parse("NZD " + double1);
        assertEquals(expectedTotal, order.getTotalPrice());
    }

    @Given("There is a sales record with a cheeseburger with quantity {int}")
    public void thereIsASalesRecordWithACheeseburgerWithQuantity(Integer int1) {
        Money price = Money.parse("NZD 5.00");
        ArrayList<String> ingredients = new ArrayList<>();
        ingredients.add("None");
        cheeseburger = new MenuItemImpl("Burg", "CheeseBurger", null, price, ingredients);
        HashMap<MenuItem, Integer> itemsOrdered = new HashMap<>();
        itemsOrdered.put(cheeseburger, int1);
        order = new SaleImpl(null, null, null, PaymentType.CASH, "", itemsOrdered);
    }

    @When("The user changes the quantity to be {int}")
    public void theUserChangesTheQuantityToBe(Integer int1) {
        int increase = int1 - order.getItemsOrdered().get(cheeseburger);
        order.changeOrderedQuantity(cheeseburger, increase);
    }

    @Then("The record now has quantity {int} for cheeseburger")
    public void theRecordNowHasQuantityForCheeseburger(Integer int1) {
        assertEquals(int1, order.getItemsOrdered().get(cheeseburger));
    }
}

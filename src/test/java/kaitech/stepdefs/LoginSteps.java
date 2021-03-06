package kaitech.stepdefs;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import kaitech.api.database.PinTable;
import kaitech.api.model.Business;
import kaitech.model.BusinessImpl;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

public class LoginSteps {
    private Business testBusiness;
    String hashAttempt;

    @Given("that the Business’s pin is {string}")
    public void given_that_the_Business_pin_is(CharSequence pin) throws Throwable {
        testBusiness = BusinessImpl.createTestBusiness(File.createTempFile("test_db", ".db"));
        testBusiness.setPin(Business.DEFAULT_USER, pin);
    }

    @When("the user logs in with the pin {string}")
    public void the_user_logs_in_with_the_pin(CharSequence attempt) {
        testBusiness.logOut();
        testBusiness.logIn(Business.DEFAULT_USER, attempt);
    }

    @Then("the user is now logged in")
    public void the_user_is_now_logged_in() {
        // Write code here that turns the phrase above into concrete actions
        assertTrue(testBusiness.isLoggedIn());
    }

    @When("the user changes the pin to {string}")
    public void the_user_changes_the_pin_to(CharSequence newPin) {
        testBusiness.setPin(Business.DEFAULT_USER, newPin);
    }

    @Then("the pin is now {string}")
    public void the_pin_is_now(CharSequence pin) {
        PinTable pinTable = testBusiness.getPinTable();
        String salt = pinTable.getSalt(Business.DEFAULT_USER);
        hashAttempt = pinTable.hashPin(pin, salt);
        assertTrue(hashAttempt.equals(testBusiness.getPinTable().getHashedPin(Business.DEFAULT_USER)));
    }

    @Then("the user is not logged in")
    public void theUserIsNotLoggedIn() {
        assertFalse(testBusiness.isLoggedIn());
    }

    @When("the user tries to change the pin to {string}")
    public void theUserTriesToChangeThePinTo(CharSequence newPin) {
        assertThrows(IllegalArgumentException.class, () -> testBusiness.setPin(Business.DEFAULT_USER, newPin));
    }
}

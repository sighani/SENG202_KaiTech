package kaitech.stepdefs;

import cucumber.api.PendingException;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import kaitech.api.model.Business;
import kaitech.model.BusinessImpl;
import org.junit.Assert;

import static org.junit.jupiter.api.Assertions.*;

public class LoginSteps {
    private Business business = BusinessImpl.getInstance();

    @Given("Given that the Business’s pin is {string}")
    public void given_that_the_Business_pin_is(String pin) {
        // Write code here that turns the phrase above into concrete actions
        business.setPin(pin);
    }

    @When("the user logs in with the pin {string}")
    public void the_user_logs_in_with_the_pin(String attempt) {
        // Write code here that turns the phrase above into concrete actions
        business.logIn(attempt);
    }

    @Then("the user is now logged in.")
    public void the_user_is_now_logged_in() {
        // Write code here that turns the phrase above into concrete actions
        assertTrue(business.isLoggedIn());
    }
}

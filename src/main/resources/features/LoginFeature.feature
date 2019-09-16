Feature: Login
    Some scenarios involving login.
    
    Scenario: Managerial login
        Given Given that the Business’s pin is 1234
        When the user logs in with the pin 1234
        Then the user is now logged in
Feature: Login
    Some scenarios involving login.
    
    Scenario: Managerial login
        Given that the Business’s pin is "1234"
        When the user logs in with the pin "1234"
        Then the user is now logged in

    Scenario: Incorrect pin
        Given that the Business’s pin is "1234"
        When the user logs in with the pin "1235"
        Then the user is not logged in

    Scenario: Set pin
        Given that the Business’s pin is "1234"
        When the user changes the pin to "0000"
        Then the pin is now "0000"

    Scenario: Set invalid pin
        Given that the Business’s pin is "1234"
        When the user tries to change the pin to "abcd"
        Then the pin is now "1234"
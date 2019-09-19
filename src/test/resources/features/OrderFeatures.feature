Feature: Order
    Some scenarios involving orders.

    Scenario: Basic order (AT-01)
        Given The sales screen is open
        And A cheeseburger costs $5.00
        When I click the cheeseburger item
        Then The total to be charged should be $5.00

    Scenario: Combo order (AT-07)
        Given A cheeseburger costs $5.00
        And A drink costs $3.00
        When An order is put in for 2 cheeseburgers and 1 drink
        Then The total chargeable is $13.00

    Scenario: Edit order (AT-06)
        Given There is a sales record with a cheeseburger with quantity 1
        When The user changes the quantity to be 2
        Then The record now has quantity 2 for cheeseburger
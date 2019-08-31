Feature: Order feature.
    Some scenarios involving orders.

    Scenario: Basic order (AT-01)
        Given The sales screen is open
        And A cheeseburger costs $5.00
        When I click the cheeseburger item
        Then The total to be charged should be $5.00

    Scenario: Combo order (AT-07)
        Given The sales screen is open
        And A cheeseburger costs $5.00
        And A drink costs $3.00
        When An order is put in for two cheeseburgers and a drink
        Then The total chargable is $13.00

    Scenario: Confirm order (AT-05)
        Given The confirm order screen is open
        And The current order is for one cheeseburger costing $5.00
        When Confirm is clicked
        Then An order with one cheeseburger is sent to current orders, and "Total cost: $5.00" gets displayed

    Scenario: Record order (AT-03)
        Given A valid order for one cheeseburger has been made
        When Confirm is clicked
        Then A record reading "Cheeseburger x 1, cost: $5.00" is made, and the ingredient stock levels are updated

    Scenario: Edit order (AT-06)
        Given The user edits an order of one cheeseburger to be two cheeseburgers
        When The user confirms the change
        Then The record in the system is changed to read "Cheeseburger x 2, cost: $10.00"

    Scenario: Edit record (AT-13)
        Given The user is on the sales screen
        And The user has taken an order with a corresponding record in the past
        When The user selects past sales, selects a record to modify, and enters a valid pin
        Then The user is able to modify the details of the record (such as alter items purchased, payment method)
Feature: Order feature.
    Some scenarios involving orders.

    Scenario: Basic burger (AT-01)
        Given The sales screen is open and a cheeseburger costs $5.00
        When I click the cheeseburger item
        Then The total to be charged should be $5.00

    Scenario: Order recording (AT-03)
        Given A cheeseburger has been selected in the sales screen and there are sufficient ingredients
        When I click confirm
        Then A record is made and the ingredients are updated


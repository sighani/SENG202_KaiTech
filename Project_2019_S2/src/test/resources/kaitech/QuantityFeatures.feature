Feature: Ingredient quantity feature.
    Some scenarios involving the viewing and updating of stock quantities.

    Scenario: Check sufficient ingredients (AT-04)
        Given The main menu is open
        When The user selects “check for sufficient ingredients” and selects cheeseburger, and there are sufficient ingredients
        Then The system will display a message saying “There are sufficient ingredients to make this item”.

    Scenario: Calculate producable quantity (AT-10)
        Given The user is on the menu items window
        When The cheeseburger item is selected and calculate servings is clicked
        Then The number of cheeseburger servings producable given the current stock levels is displayed

    Scenario: View stock (AT-12)
        Given The user is on the main menu
        When The show stock button is clicked
        Then The user is taken to a screen breaking down the stock on hand
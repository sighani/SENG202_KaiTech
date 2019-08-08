Feature: Ingredient quantity feature.
    Some scenarios involving the viewing and updating of ingredient quantities.
    
    Scenario: Check sufficient ingredients (AT-04)
        Given The main menu is open
        When The user selects “check for sufficient ingredients” and selects cheeseburger, and there are sufficient ingredients
        Then The system will display a message saying “There are sufficient ingredients to make this item”.
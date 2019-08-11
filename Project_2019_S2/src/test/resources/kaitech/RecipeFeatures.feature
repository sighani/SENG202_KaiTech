Feature: Recipe feature.
    Some scenarios involving recipes in the system.

    Scenario: Check item requirements (AT-02)
        Given The menu items screen is open and has a cheeseburger item
        When The cheeseburger is selected
        Then the cheeseburger's recipe/ingredients are displayed
    
    Scenario: Modify recipe (AT-08)
        Given A cheeseburger recipe exists in the system
        When The user clicks the modify recipe button
        Then The user is able to modify the recipe


Feature: Ingredient quantity feature.
    Some scenarios involving the viewing and updating of stock quantities.

    Scenario: Check sufficient ingredients (AT-04)
        Given A cheeseburger has a recipe consisting of 1 cheese slice, 1 meat patty, and 1 bun
        And The business has 2 cheese slices, 2 meat patties, and 1 bun
        Then The business has enough ingredients

    Scenario: Insufficient ingredients
        Given A cheeseburger has a recipe consisting of 1 cheese slice, 1 meat patty, and 1 bun
        And The business has 0 cheese slices, 2 meat patties, and 1 bun
        Then The business doesn't enough ingredients

    Scenario: Calculate producible quantity (AT-10)
        Given A cheeseburger has a recipe consisting of 1 cheese slice, 1 meat patty, and 1 bun
        And The business has 2 cheese slices, 2 meat patties, and 2 buns
        Then The business can serve 2 cheeseburgers

    Scenario: Calculate producible quantity (zero)
        Given A cheeseburger has a recipe consisting of 1 cheese slice, 1 meat patty, and 1 bun
        And The business has 0 cheese slices, 2 meat patties, and 2 buns
        Then The business can serve 0 cheeseburgers
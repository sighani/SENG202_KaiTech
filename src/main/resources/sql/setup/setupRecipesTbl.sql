CREATE TABLE IF NOT EXISTS "recipes" --Ingredients in the recipe are in recipe_ingredients
(
    "recipeID"        INTEGER,
    "preparationTime" INTEGER,
    "cookingTime"     INTEGER,
    "numServings"     INTEGER,
    PRIMARY KEY ("recipeID")
);
CREATE TABLE IF NOT EXISTS "recipe_ingredients" --Links an ingredient in a recipe to the quantity required
(
    "recipe" REFERENCES recipes ("recipeID") ON DELETE CASCADE NOT NULL,
    "ingredient" REFERENCES ingredients ("code")               NOT NULL,
    "quantity" INTEGER                                         NOT NULL,
    PRIMARY KEY ("recipe", "ingredient")
);
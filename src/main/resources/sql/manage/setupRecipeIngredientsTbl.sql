CREATE TABLE IF NOT EXISTS "recipe_ingredients" --Contains no primary key, needs anti-duplicate checking
(
    "recipe" REFERENCES recipes ("recipeID")     NOT NULL,
    "ingredient" REFERENCES ingredients ("code") NOT NULL,
    "quantity" INTEGER                           NOT NULL
);
CREATE TABLE IF NOT EXISTS "recipes"
(
    "recipeID"        INTEGER UNIQUE NOT NULL,
    "preparationTime" INTEGER        NOT NULL,
    "cookingTime"     INTEGER        NOT NULL,
    "numServings"     INTEGER        NOT NULL,
    PRIMARY KEY ("recipeID")
);
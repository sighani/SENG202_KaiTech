CREATE TABLE IF NOT EXISTS "menu_items"
(
    "code" TEXT UNIQUE NOT NULL,
    "name" TEXT UNIQUE NOT NULL,
    "recipe" REFERENCES recipes ("recipeID"),
    PRIMARY KEY ("code")
);
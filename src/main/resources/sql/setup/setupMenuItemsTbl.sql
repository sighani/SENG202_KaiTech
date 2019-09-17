CREATE TABLE IF NOT EXISTS "menu_items"
(
    "code"  TEXT UNIQUE NOT NULL,
    "name"  TEXT,
    "recipe" REFERENCES recipes ("recipeID"),
    "price" TEXT        NOT NULL,
    "type"  INTEGER,
    PRIMARY KEY ("code")
);
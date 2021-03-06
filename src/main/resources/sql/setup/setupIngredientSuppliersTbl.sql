CREATE TABLE IF NOT EXISTS "ingredient_suppliers" --Links ingredients to their suppliers
(
    "ingredient" REFERENCES ingredients ("code") ON DELETE CASCADE NOT NULL,
    "supplier" REFERENCES suppliers ("id") ON DELETE CASCADE       NOT NULL,
    PRIMARY KEY ("ingredient", "supplier")
);
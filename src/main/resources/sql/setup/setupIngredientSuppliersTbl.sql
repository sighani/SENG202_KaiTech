CREATE TABLE IF NOT EXISTS "ingredient_suppliers" --Links ingredients to their suppliers
(
    "ingredient" REFERENCES ingredients ("code") NOT NULL,
    "supplier" REFERENCES suppliers ("id")       NOT NULL,
    PRIMARY KEY ("ingredient", "supplier")
);
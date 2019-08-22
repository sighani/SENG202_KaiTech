CREATE TABLE IF NOT EXISTS "ingredient_suppliers" --Contains no primary key, needs anti-duplicate checking
(
    "ingredient" REFERENCES ingredients ("code") NOT NULL,
    "supplier" REFERENCES suppliers ("id")       NOT NULL
);
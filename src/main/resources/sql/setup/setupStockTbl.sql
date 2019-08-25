CREATE TABLE IF NOT EXISTS "stock"
(
    "ingredient" REFERENCES ingredients("code"),
    "quantity" INTEGER NOT NULL,
    PRIMARY KEY ("ingredient")
);
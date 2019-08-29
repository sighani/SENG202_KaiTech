CREATE TABLE IF NOT EXISTS "inventory"
(
    "ingredient" REFERENCES ingredients("code"),
    "quantity" INTEGER NOT NULL,
    PRIMARY KEY ("ingredient")
);
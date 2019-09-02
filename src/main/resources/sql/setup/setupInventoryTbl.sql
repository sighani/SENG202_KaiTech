CREATE TABLE IF NOT EXISTS "inventory"
(
    "ingredient" REFERENCES ingredients("code") ON DELETE CASCADE,
    "quantity" INTEGER NOT NULL,
    PRIMARY KEY ("ingredient")
);
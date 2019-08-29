CREATE TABLE IF NOT EXISTS "ingredients" --Suppliers of the ingredient are in ingredient_suppliers
(
    "code"    TEXT UNIQUE NOT NULL,
    "name"    TEXT UNIQUE NOT NULL,
    "unit"    INTEGER     NOT NULL,
    "price"   TEXT        NOT NULL,
    "isVeg"   INTEGER     NOT NULL,
    "isVegan" INTEGER     NOT NULL,
    "isGF"    INTEGER     NOT NULL,
    PRIMARY KEY ("code")
);
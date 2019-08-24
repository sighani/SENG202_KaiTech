CREATE TABLE IF NOT EXISTS "ingredients" --Suppliers of the ingredient are in ingredient_suppliers
(
    "code"    TEXT UNIQUE NOT NULL,
    "name"    TEXT UNIQUE NOT NULL,
    "unit"    INTEGER     NOT NULL,
    "price"   INTEGER     NOT NULL,
    "stock"   INTEGER     NOT NULL,
    "isVeg"   BOOLEAN     NOT NULL,
    "isVegan" BOOLEAN     NOT NULL,
    "isGF"    BOOLEAN     NOT NULL,
    PRIMARY KEY ("code")
);
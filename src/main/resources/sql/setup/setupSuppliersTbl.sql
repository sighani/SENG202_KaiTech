CREATE TABLE IF NOT EXISTS "suppliers" --Ingredients sold by suppliers are in ingredient_suppliers
(
    "id"     TEXT UNIQUE NOT NULL,
    "name"   TEXT UNIQUE,
    "addr"   TEXT UNIQUE,
    "ph"     TEXT UNIQUE,
    "phType" INTEGER,
    "email"  TEXT NOT NULL,
    "url"    TEXT NOT NULL,
    PRIMARY KEY ("id")
);
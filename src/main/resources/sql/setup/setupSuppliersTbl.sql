CREATE TABLE IF NOT EXISTS "suppliers"
(
    "id"     TEXT UNIQUE NOT NULL,
    "name"   TEXT UNIQUE NOT NULL,
    "addr"   TEXT UNIQUE NOT NULL,
    "ph"     TEXT UNIQUE NOT NULL,
    "phType" INTEGER     NOT NULL,
    "email"  TEXT UNIQUE NOT NULL,
    "url"    TEXT UNIQUE NOT NULL,
    PRIMARY KEY ("id")
);
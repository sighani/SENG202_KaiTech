CREATE TABLE IF NOT EXISTS "menus"
(
    "id"   TEXT UNIQUE NOT NULL,
    "name" TEXT        NOT NULL,
    PRIMARY KEY ("id")
);
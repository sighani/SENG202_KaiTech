CREATE TABLE IF NOT EXISTS pins
(
    "name" TEXT,
    "hash"  TEXT NOT NULL,
    "salt" TEXT NOT NULL,
    PRIMARY KEY ("name")
);
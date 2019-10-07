CREATE TABLE IF NOT EXISTS loyalty_cards
(
    "id"           INTEGER UNIQUE NOT NULL,
    "balance"      TEXT,
    "customerName" TEXT,
    "lastPurchase" DATE,
    PRIMARY KEY (id)
);
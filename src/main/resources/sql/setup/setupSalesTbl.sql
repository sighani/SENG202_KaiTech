CREATE TABLE IF NOT EXISTS "sales"
(
    "receiptNumber" INTEGER,
    "date"          DATE    NOT NULL,
    "time"          TIME    NOT NULL,
    "paymentType"   INTEGER NOT NULL,
    "notes"         TEXT,
    "totalPrice"    TEXT    NOT NULL,
    PRIMARY KEY ("receiptNumber")
);
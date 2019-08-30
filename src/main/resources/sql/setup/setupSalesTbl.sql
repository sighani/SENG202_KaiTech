CREATE TABLE IF NOT EXISTS "sales"
(
    "receiptNumber" INTEGER AUTO_INCREMENT UNIQUE NOT NULL,
    "itemsOrdered"  TEXT                          NOT NULL,
    "date"          DATE                          NOT NULL,
    "time"          TIME                          NOT NULL,
    "paymentType"   INTEGER                       NOT NULL,
    "notes"         TEXT,
    "totalPrice"    INTEGER                       NOT NULL,
    PRIMARY KEY ("receiptNumber")
);
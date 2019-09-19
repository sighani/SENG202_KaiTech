CREATE TABLE IF NOT EXISTS sale_items ( --Links a sale to the menu items ordered in that sale
    "receiptNumber" REFERENCES sales ("receiptNumber") ON DELETE CASCADE,
    "menuItem" REFERENCES menu_items ("code"),
    "quantity" INTEGER NOT NULL,
    PRIMARY KEY (receiptNumber, "menuItem")
);
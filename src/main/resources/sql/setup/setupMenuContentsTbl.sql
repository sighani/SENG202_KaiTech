CREATE TABLE IF NOT EXISTS "menu_contents" --Links a menu to the items on the menu
(
    "menuID" REFERENCES menus ("id")          NOT NULL,
    "itemCode" REFERENCES menu_items ("code") NOT NULL,
    PRIMARY KEY ("menuID", "itemCode")
);
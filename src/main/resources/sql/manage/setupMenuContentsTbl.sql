CREATE TABLE IF NOT EXISTS "menu_contents" --Contains no primary key, needs anti-duplicate checking
(
    "menuID" REFERENCES menus ("id")          NOT NULL,
    "itemCode" REFERENCES menu_items ("code") NOT NULL
);
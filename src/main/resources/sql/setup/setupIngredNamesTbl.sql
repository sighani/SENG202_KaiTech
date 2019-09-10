CREATE TABLE IF NOT EXISTS ingredient_names (
    "menuItem" REFERENCES menu_items (code),
    "name" TEXT NOT NULL,
    PRIMARY KEY (menuItem, name)
);
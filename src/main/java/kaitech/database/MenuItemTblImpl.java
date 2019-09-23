package kaitech.database;

import kaitech.api.database.AbstractTable;
import kaitech.api.database.IngredientTable;
import kaitech.api.database.MenuItemTable;
import kaitech.api.database.RecipeTable;
import kaitech.api.model.Ingredient;
import kaitech.api.model.MenuItem;
import kaitech.api.model.Recipe;
import kaitech.model.MenuItemImpl;
import kaitech.util.MenuItemType;
import org.joda.money.Money;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.Collections.singletonMap;

/**
 * MenuItemTblImpl extends AbstractTable, implements the MenuItemTable interface, and permits limited access to the
 * data stored in the menu_items table.
 *
 * @author Julia Harrison
 */
public class MenuItemTblImpl extends AbstractTable implements MenuItemTable {

    /**
     * The database tables containing data relating to the business, used by the MenuItemTable.
     */
    private final RecipeTable recipeTable;
    private final IngredientTable ingredientTable;

    /**
     * Cache for the codes of menu items.
     */
    private final Set<String> codes = new HashSet<>();

    /**
     * Cache for Menu Items, stored as a Map from code to MenuItem.
     */
    private final Map<String, MenuItem> menuItems = new HashMap<>();

    /**
     * The name of the table.
     */
    private final String tableName = "menu_items";

    /**
     * The name of the primary key column of the table..
     */
    private final String tableKey = "code";

    /**
     * Constructor for the MenuItemTable.
     * On instantiation, greedy loads the codes of menu items into cache.
     *
     * @param dbHandler       The DatabaseHandler to load the menu items from and save to.
     * @param recipeTable     The RecipeTable for the business, containing information about recipes.
     * @param ingredientTable The IngredientTable for the business, containing information about ingredients.
     */
    public MenuItemTblImpl(DatabaseHandler dbHandler, RecipeTable recipeTable, IngredientTable ingredientTable) {
        super(dbHandler);
        this.recipeTable = recipeTable;
        this.ingredientTable = ingredientTable;
        PreparedStatement getCodesQuery = dbHandler.prepareStatement("SELECT code FROM menu_items;");
        ResultSet results;
        try {
            results = getCodesQuery.executeQuery();
            while (results.next()) {
                codes.add(results.getString("code"));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to load menu items.", e);
        }
    }

    @Override
    public MenuItem getMenuItem(String code) {
        MenuItem menuItem = menuItems.get(code);

        if (menuItem == null && codes.contains(code)) {
            try {
                PreparedStatement getMIQuery = dbHandler.prepareStatement("SELECT * FROM menu_items WHERE code=?;");
                getMIQuery.setString(1, code);
                ResultSet results = getMIQuery.executeQuery();
                if (results.next()) {
                    MenuItemType[] types = MenuItemType.values();
                    Recipe recipe = recipeTable.getRecipe(results.getInt("recipe"));
                    PreparedStatement getNamesQuery = dbHandler.prepareStatement("SELECT * FROM ingredient_names " +
                            "WHERE menuItem=?;");
                    getNamesQuery.setString(1, code);
                    ResultSet nameResults = getNamesQuery.executeQuery();
                    List<String> names = new ArrayList<>();
                    while (nameResults.next()) {
                        names.add(nameResults.getString("name"));
                    }
                    menuItem = new DbMenuItem(code,
                            results.getString("name"),
                            names,
                            recipe,
                            Money.parse(results.getString("price")),
                            types[results.getInt("type")]);
                    menuItems.put(code, menuItem);
                }
            } catch (SQLException e) {
                throw new RuntimeException("Unable to retrieve menu item from database.", e);
            }
        }

        return menuItem;
    }

    @Override
    public Set<String> getAllIMenuItemCodes() {
        return Collections.unmodifiableSet(codes);
    }

    @Override
    public MenuItem putMenuItem(MenuItem from) {
        try {
            PreparedStatement stmt = dbHandler.prepareResource("/sql/modify/insert/insertMenuItem.sql");
            String code = from.getCode();
            stmt.setString(1, code);
            stmt.setString(2, from.getName());
            if (from.getRecipe() == null) {
                stmt.setObject(3, null);
            } else {
                stmt.setInt(3, from.getRecipe().getID());
            }
            stmt.setString(4, from.getPrice().toString());
            stmt.setInt(5, from.getType().ordinal());
            stmt.executeUpdate();

            MenuItem dbMenuItem = new DbMenuItem(from);
            if (from.getRecipe() != null) {
                dbMenuItem.setRecipe(from.getRecipe());
            }
            codes.add(code);
            menuItems.put(code, dbMenuItem);
            return dbMenuItem;
        } catch (SQLException e) {
            throw new RuntimeException("Unable to save menu item to the database.", e);
        }
    }

    @Override
    public void removeMenuItem(String code) {
        try {
            PreparedStatement stmt = dbHandler.prepareResource("/sql/modify/delete/deleteMenuItem.sql");
            stmt.setString(1, code);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Unable to remove menu item from the database.", e);
        }
        codes.remove(code);
        menuItems.remove(code);
    }

    @Override
    public Map<String, MenuItem> resolveAllMenuItems() {
        return codes.stream() //
                .map(this::getMenuItem) //
                .collect(Collectors.toMap(MenuItem::getCode, Function.identity()));
    }

    /**
     * Database specific implementation of a menu item, which has database updating on attribute changes.
     */
    private class DbMenuItem extends MenuItemImpl {
        private final Map<String, Object> key;

        public DbMenuItem(String code, String name, List<String> ingredients, Recipe recipe, Money price,
                          MenuItemType type) {
            super(code, name, price, recipe, type, ingredients);
            setIngredients(ingredients);
            key = singletonMap(tableKey, getCode());
        }

        public DbMenuItem(MenuItem other) {
            super(other.getCode(), other.getName(), other.getPrice(), other.getRecipe(), other.getType(), other.getIngredients()
            );
            setIngredients(other.getIngredients());
            key = singletonMap(tableKey, getCode());
        }

        @Override
        public void addIngredientToRecipe(Ingredient ing, int amt) {
            Ingredient dbIngredient = ingredientTable.getOrAddIngredient(ing);
            Recipe dbRecipe = recipeTable.getRecipe(recipe.getID());
            dbRecipe.addIngredient(dbIngredient, amt);
            ingredients.add(ing.getName());
        }

        @Override
        public void removeIngredientFromRecipe(Ingredient ing) {
            Ingredient dbIngredient = ingredientTable.getOrAddIngredient(ing);
            Recipe dbRecipe = recipeTable.getRecipe(recipe.getID());
            dbRecipe.removeIngredient(dbIngredient);
            ingredients.remove(ing.getName());
        }

        @Override
        public void setIngredients(List<String> ingredients) {
            try {
                PreparedStatement stmt = dbHandler.prepareStatement("DELETE FROM ingredient_names WHERE menuItem=?;");
                stmt.setString(1, code);
                stmt.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException("Unable to clear existing ingredients for the menu item from the database.", e);
            }

            String menuCode = getCode();
            List<List<Object>> values = new ArrayList<>();
            for (String ingredient : ingredients) {
                values.add(Arrays.asList(menuCode, ingredient));
            }
            insertRows("ingredient_names", values);
            super.setIngredients(ingredients);
        }

        @Override
        public void setName(String name) {
            updateColumn(tableName, key, "name", name);
            super.setName(name);
        }

        @Override
        public void setRecipe(Recipe recipe) {
            int recipeID = recipe.getID();
            updateColumn(tableName, key, "recipe", recipeID);
            super.setRecipe(recipeTable.getOrAddRecipe(recipe));
        }

        @Override
        public void setPrice(Money price) {
            updateColumn(tableName, key, "price", price.toString());
            super.setPrice(price);
        }

        @Override
        public void setType(MenuItemType type) {
            updateColumn(tableName, key, "type", type.ordinal());
            super.setType(type);
        }
    }
}

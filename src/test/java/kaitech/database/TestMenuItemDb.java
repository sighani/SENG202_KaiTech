package kaitech.database;

import kaitech.api.database.IngredientTable;
import kaitech.api.database.MenuItemTable;
import kaitech.api.database.RecipeTable;
import kaitech.api.model.Ingredient;
import kaitech.api.model.MenuItem;
import kaitech.api.model.Recipe;
import kaitech.model.IngredientImpl;
import kaitech.model.MenuItemImpl;
import kaitech.model.RecipeImpl;
import kaitech.util.MenuItemType;
import org.joda.money.Money;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import static org.junit.Assert.*;

public class TestMenuItemDb {
    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();

    private DatabaseHandler dbHandler;
    private MenuItemTable menuItemTable;
    private RecipeTable recipeTable;

    public void init() throws Throwable {
        dbHandler = new DatabaseHandler(tempFolder.newFile());
        PreparedStatement stmt;
        List<String> resources = Arrays.asList("/sql/setup/setupIngredientsTbl.sql",
                "/sql/setup/setupSuppliersTbl.sql",
                "/sql/setup/setupRecipesTbl.sql",
                "/sql/setup/setupRecipeIngredientsTbl.sql",
                "/sql/setup/setupMenuItemsTbl.sql",
                "/sql/setup/setupIngredNamesTbl.sql");
        for (String resource : resources) {
            stmt = dbHandler.prepareResource(resource);
            stmt.executeUpdate();
        }
        IngredientTable ingredientTable = new IngredientTblImpl(dbHandler, new SupplierTblImpl(dbHandler));
        recipeTable = new RecipeTblImpl(dbHandler, ingredientTable);
        menuItemTable = new MenuItemTblImpl(dbHandler, recipeTable, ingredientTable);
    }

    public void teardown() throws SQLException {
        dbHandler.getConn().close();
    }

    private MenuItem putMenuItem(String code, Recipe recipe, Money price) {
        MenuItem menuItem = new MenuItemImpl(code, recipe, price);
        return menuItemTable.putMenuItem(menuItem);
    }

    private MenuItem putMenuItem(String code, String name, List<String> ingredients, Recipe recipe, Money price,
                                 MenuItemType type) {
        MenuItem menuItem = new MenuItemImpl(code, name, ingredients, recipe, price, type);
        return menuItemTable.putMenuItem(menuItem);
    }

    @Test
    public void testPutMenuItem() throws Throwable {
        init();
        Ingredient ingredient = new IngredientImpl("PORK");
        ingredient.setName("Pork");
        Recipe recipe = new RecipeImpl(Collections.singletonMap(ingredient, 100));
        MenuItem menuItem = putMenuItem("BAO", recipe, Money.parse("NZD 3.00"));
        menuItem.setName("Pork Steam Bun");
        menuItem.setType(MenuItemType.ASIAN);

        PreparedStatement stmt = dbHandler.prepareStatement("SELECT * FROM menu_items WHERE code=\"BAO\";");
        ResultSet results = stmt.executeQuery();
        if (results.next()) {
            assertEquals("Pork Steam Bun", results.getString("name"));
            assertEquals(recipe.getID(), results.getInt("recipe"));
            assertEquals("NZD 3.00", results.getString("price"));
            assertEquals(MenuItemType.ASIAN.ordinal(), results.getInt("type"));
        } else {
            throw new RuntimeException("Could not retrieve menu item from database.");
        }

        stmt = dbHandler.prepareStatement("SELECT * FROM ingredient_names WHERE menuItem=\"BAO\";");
        results = stmt.executeQuery();
        if (results.next()) {
            assertEquals("Pork", results.getString("name"));
        } else {
            throw new RuntimeException("Unable to retrieve ingredient names from database.");
        }

        teardown();
    }

    @Test
    public void testGetMenuItem() throws Throwable {
        init();
        Ingredient ingredient = new IngredientImpl("PORK");
        ingredient.setName("Pork");
        Recipe recipe = new RecipeImpl(Collections.singletonMap(ingredient, 100));
        recipe = recipeTable.getOrAddRecipe(recipe);
        putMenuItem("BAO", recipe, Money.parse("NZD 3.00"));

        MenuItem menuItem = menuItemTable.getMenuItem("BAO");
        assertNull(menuItem.getName());
        assertEquals(recipeTable.getRecipe(recipe.getID()), menuItem.getRecipe());
        assertEquals(Money.parse("NZD 3.00"), menuItem.getPrice());
        assertEquals(MenuItemType.MISC, menuItem.getType());
        assertEquals(1, menuItem.getIngredients().size());
        assertTrue(menuItem.getIngredients().contains("Pork"));
        teardown();
    }

    @Test
    public void testGetAllCodes() throws Throwable {
        init();
        Ingredient ingredient = new IngredientImpl("PORK");
        Recipe recipe = new RecipeImpl(Collections.singletonMap(ingredient, 100));
        putMenuItem("BAO", recipe, Money.parse("NZD 3.00"));
        putMenuItem("BAO2", recipe, Money.parse("NZD 5.00"));
        Set<String> codes = menuItemTable.getAllIMenuItemCodes();
        assertEquals(2, codes.size());
        assertTrue(codes.contains("BAO"));
        assertTrue(codes.contains("BAO2"));
        teardown();
    }

    @Test
    public void testRemoveMenuItem() throws Throwable {
        init();
        Ingredient ingredient = new IngredientImpl("PORK");
        Recipe recipe = new RecipeImpl(Collections.singletonMap(ingredient, 100));
        putMenuItem("BAO", recipe, Money.parse("NZD 3.00"));
        assertNotNull(menuItemTable.getMenuItem("BAO"));
        PreparedStatement stmt = dbHandler.prepareStatement("SELECT * FROM menu_items WHERE code=\"BAO\";");
        ResultSet results = stmt.executeQuery();
        assertTrue(results.next());

        menuItemTable.removeMenuItem("BAO");
        assertNull(menuItemTable.getMenuItem("BAO"));
        results = stmt.executeQuery();
        assertFalse(results.next());
        teardown();
    }

    @Test
    public void testResolveAllMenuItems() throws Throwable {
        init();
        Ingredient ingredient = new IngredientImpl("PORK");
        Recipe recipe = new RecipeImpl(Collections.singletonMap(ingredient, 100));
        MenuItem bao = putMenuItem("BAO", recipe, Money.parse("NZD 3.00"));
        MenuItem bao2 = putMenuItem("BAO2", recipe, Money.parse("NZD 5.00"));
        Map<String, MenuItem> menuItems = menuItemTable.resolveAllMenuItems();
        assertEquals(bao, menuItems.get("BAO"));
        assertEquals(bao2, menuItems.get("BAO2"));
        teardown();
    }
}

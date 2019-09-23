package kaitech.database;

import kaitech.api.database.IngredientTable;
import kaitech.api.database.MenuItemTable;
import kaitech.api.database.RecipeTable;
import kaitech.api.database.SupplierTable;
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
                "/sql/setup/setupIngredientSuppliersTbl.sql",
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

    @Test
    public void testPutMenuItem() throws Throwable {
        init();
        Ingredient ingredient = new IngredientImpl("PORK");
        ingredient.setName("Pork");
        Recipe recipe = new RecipeImpl("Pork Bao Bun", Collections.singletonMap(ingredient, 100));
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
        Recipe recipe = new RecipeImpl("Pork Bao Bun", Collections.singletonMap(ingredient, 100));
        recipe = recipeTable.getOrAddRecipe(recipe);
        MenuItem tmp = putMenuItem("BAO", recipe, Money.parse("NZD 3.00"));
        tmp.setPrice(Money.parse("NZD 2.50"));

        MenuItem menuItem = menuItemTable.getMenuItem("BAO");
        assertNull(menuItem.getName());
        assertEquals(recipeTable.getRecipe(recipe.getID()), menuItem.getRecipe());
        assertEquals(Money.parse("NZD 2.50"), menuItem.getPrice());
        assertEquals(MenuItemType.MISC, menuItem.getType());
        assertEquals(1, menuItem.getIngredients().size());
        assertTrue(menuItem.getIngredients().contains("Pork"));

        SupplierTable otherSupplierTable = new SupplierTblImpl(dbHandler);
        IngredientTable otherIngredientTable = new IngredientTblImpl(dbHandler, otherSupplierTable);
        RecipeTable otherRecipeTable = new RecipeTblImpl(dbHandler, otherIngredientTable);
        MenuItemTable otherMenuItemTable = new MenuItemTblImpl(dbHandler, otherRecipeTable, otherIngredientTable);
        MenuItem dbMenuItem = otherMenuItemTable.getMenuItem(menuItem.getCode());
        assertNotNull("MenuItem is null.", dbMenuItem);

        assertEquals(menuItem.getCode(), dbMenuItem.getCode());
        assertEquals(menuItem.getName(), dbMenuItem.getName());
        assertEquals(menuItem.getRecipe(), dbMenuItem.getRecipe());
        assertEquals(menuItem.getPrice(), dbMenuItem.getPrice());
        assertEquals(menuItem.getType(), dbMenuItem.getType());
        assertEquals(menuItem.getIngredients(), dbMenuItem.getIngredients());
        assertEquals(menuItem.getIsVeg(), dbMenuItem.getIsVeg());
        assertEquals(menuItem.getIsVegan(), dbMenuItem.getIsVegan());
        assertEquals(menuItem.getIsGF(), dbMenuItem.getIsGF());

        teardown();
    }

    @Test
    public void testGetMenuItemNullRecipe() throws Throwable {
        init();
        MenuItem menuItem = new MenuItemImpl("BAO", "Pork Buns", Money.parse("NZD 3.00"), MenuItemType.MISC,
                Collections.singletonList("Pork"));
        menuItemTable.putMenuItem(menuItem);

        MenuItem dbMenuItem = menuItemTable.getMenuItem("BAO");
        assertEquals("Pork Buns", dbMenuItem.getName());
        assertNull(dbMenuItem.getRecipe());
        assertEquals(Money.parse("NZD 3.00"), dbMenuItem.getPrice());
        assertEquals(MenuItemType.MISC, dbMenuItem.getType());
        assertEquals(1, dbMenuItem.getIngredients().size());
        assertTrue(dbMenuItem.getIngredients().contains("Pork"));

        SupplierTable otherSupplierTable = new SupplierTblImpl(dbHandler);
        IngredientTable otherIngredientTable = new IngredientTblImpl(dbHandler, otherSupplierTable);
        RecipeTable otherRecipeTable = new RecipeTblImpl(dbHandler, otherIngredientTable);
        MenuItemTable otherMenuItemTable = new MenuItemTblImpl(dbHandler, otherRecipeTable, otherIngredientTable);
        MenuItem dbMenuItem2 = otherMenuItemTable.getMenuItem(dbMenuItem.getCode());
        assertNotNull("MenuItem is null.", dbMenuItem2);

        assertEquals(dbMenuItem.getCode(), dbMenuItem2.getCode());
        assertEquals(dbMenuItem.getName(), dbMenuItem2.getName());
        assertEquals(dbMenuItem.getRecipe(), dbMenuItem2.getRecipe());
        assertEquals(dbMenuItem.getPrice(), dbMenuItem2.getPrice());
        assertEquals(dbMenuItem.getType(), dbMenuItem2.getType());
        assertEquals(dbMenuItem.getIngredients(), dbMenuItem2.getIngredients());
        assertEquals(dbMenuItem.getIsVeg(), dbMenuItem2.getIsVeg());
        assertEquals(dbMenuItem.getIsVegan(), dbMenuItem2.getIsVegan());
        assertEquals(dbMenuItem.getIsGF(), dbMenuItem2.getIsGF());

        teardown();
    }

    @Test
    public void testAddRemoveIngredientToRecipe() throws Throwable {
        init();

        Ingredient pork = new IngredientImpl("PORK");
        pork.setName("Pork");
        Ingredient flour = new IngredientImpl("FLOUR");
        flour.setName("Flour");
        Recipe recipe = new RecipeImpl("Pork Bao Bun", Collections.singletonMap(pork, 100));
        recipe = recipeTable.getOrAddRecipe(recipe);
        putMenuItem("BAO", recipe, Money.parse("NZD 3.00"));

        MenuItem menuItem = menuItemTable.getMenuItem("BAO");
        assertEquals(1, recipe.getIngredientNames().size());
        menuItem.addIngredientToRecipe(flour, 100);
        assertEquals(2, recipe.getIngredientNames().size());
        assertTrue(recipe.getIngredients().containsKey(flour));
        menuItem.removeIngredientFromRecipe(flour);
        assertFalse(recipe.getIngredients().containsKey(flour));

        teardown();
    }

    @Test
    public void testGetAllCodes() throws Throwable {
        init();
        Ingredient ingredient = new IngredientImpl("PORK");
        Recipe recipe = new RecipeImpl("Pork Bao Bun", Collections.singletonMap(ingredient, 100));
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
        Recipe recipe = new RecipeImpl("Pork Bao Bun", Collections.singletonMap(ingredient, 100));
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
        Recipe recipe = new RecipeImpl("Pork Bao Bun", Collections.singletonMap(ingredient, 100));
        MenuItem bao = putMenuItem("BAO", recipe, Money.parse("NZD 3.00"));
        MenuItem bao2 = putMenuItem("BAO2", recipe, Money.parse("NZD 5.00"));
        Map<String, MenuItem> menuItems = menuItemTable.resolveAllMenuItems();
        assertEquals(bao, menuItems.get("BAO"));
        assertEquals(bao2, menuItems.get("BAO2"));
        teardown();
    }
}

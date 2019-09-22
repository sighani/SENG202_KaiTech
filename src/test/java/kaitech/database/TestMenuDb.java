package kaitech.database;

import kaitech.api.database.IngredientTable;
import kaitech.api.database.MenuItemTable;
import kaitech.api.database.MenuTable;
import kaitech.api.database.RecipeTable;
import kaitech.api.model.Ingredient;
import kaitech.api.model.Menu;
import kaitech.api.model.MenuItem;
import kaitech.api.model.Recipe;
import kaitech.model.IngredientImpl;
import kaitech.model.MenuImpl;
import kaitech.model.MenuItemImpl;
import kaitech.model.RecipeImpl;
import org.joda.money.Money;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import static org.junit.Assert.*;

public class TestMenuDb {
    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();

    private DatabaseHandler dbHandler;
    private MenuTable menuTable;
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
                "/sql/setup/setupIngredNamesTbl.sql",
                "/sql/setup/setupMenusTbl.sql",
                "/sql/setup/setupMenuContentsTbl.sql");
        for (String resource : resources) {
            stmt = dbHandler.prepareResource(resource);
            stmt.executeUpdate();
        }
        IngredientTable ingredientTable = new IngredientTblImpl(dbHandler, new SupplierTblImpl(dbHandler));
        recipeTable = new RecipeTblImpl(dbHandler, ingredientTable);
        menuItemTable = new MenuItemTblImpl(dbHandler, recipeTable, ingredientTable);
        menuTable = new MenuTblImpl(dbHandler, menuItemTable);
    }

    public void teardown() throws SQLException {
        dbHandler.getConn().close();
    }

    private Menu putMenu(String title) {
        Menu menu = new MenuImpl(title);
        return menuTable.putMenu(menu);
    }

    @Test
    public void testPutMenu() throws Throwable {
        init();
        Menu menu = putMenu("Dim Sum");
        menu.setDescription("Bite-sized steamed dishes.");
        Ingredient ingredient = new IngredientImpl("PORK");
        Recipe recipe = new RecipeImpl("Pork Bao Bun", Collections.singletonMap(ingredient, 100));
        MenuItem menuItem = menuItemTable.putMenuItem(new MenuItemImpl("BAO", recipe, Money.parse("NZD 3.00")));
        Map<String, MenuItem> menuItems = Collections.singletonMap("BAO", menuItem);
        menu.setMenuItems(menuItems);

        PreparedStatement stmt = dbHandler.prepareStatement("SELECT * FROM menus WHERE id=?;");
        stmt.setInt(1, menu.getID());
        ResultSet results = stmt.executeQuery();
        if (results.next()) {
            assertEquals(menu.getTitle(), results.getString("title"));
            assertEquals(menu.getDescription(), results.getString("description"));
        } else {
            throw new RuntimeException("Unable to retrieve menu from database.");
        }

        stmt = dbHandler.prepareStatement("SELECT * FROM menu_contents WHERE menuID=?;");
        stmt.setInt(1, menu.getID());
        results = stmt.executeQuery();
        if (results.next()) {
            assertEquals("BAO", results.getString("itemCode"));
        } else {
            throw new RuntimeException("Unable to retrieve menu's items from database.");
        }
        teardown();
    }

    @Test
    public void testGetMenu() throws Throwable {
        init();
        Menu menu = putMenu("Dim Sum");
        Ingredient ingredient = new IngredientImpl("PORK");
        Recipe recipe = new RecipeImpl("PorK Bao Bun", Collections.singletonMap(ingredient, 100));
        MenuItem menuItem = menuItemTable.putMenuItem(new MenuItemImpl("BAO", recipe, Money.parse("NZD 3.00")));
        menu.setMenuItems(Collections.singletonMap("BAO", menuItem));

        Menu retrievedMenu = menuTable.getMenu(menu.getID());
        assertEquals(menu.getTitle(), retrievedMenu.getTitle());
        assertEquals(menu.getDescription(), retrievedMenu.getDescription());
        assertEquals(menu.getMenuItems(), retrievedMenu.getMenuItems());
        teardown();
    }

    @Test
    public void testGetAllMenuIDs() throws Throwable {
        init();
        Menu dimSum = putMenu("Dim Sum");
        Menu desserts = putMenu("Desserts");
        Set<Integer> menuIDs = menuTable.getAllMenuIDs();
        assertTrue(menuIDs.contains(dimSum.getID()));
        assertTrue(menuIDs.contains(desserts.getID()));
        teardown();
    }

    @Test
    public void testRemoveMenu() throws Throwable {
        init();
        Menu menu = putMenu("Dim Sum");
        int id = menu.getID();
        assertNotNull(menuTable.getMenu(id));
        PreparedStatement stmt = dbHandler.prepareStatement("SELECT * FROM menus WHERE id=?;");
        stmt.setInt(1, id);
        ResultSet results = stmt.executeQuery();
        assertTrue(results.next());

        menuTable.removeMenu(id);
        assertNull(menuTable.getMenu(id));
        results = stmt.executeQuery();
        assertFalse(results.next());

        teardown();
    }

    @Test
    public void testResolveAllMenus() throws Throwable {
        init();
        Menu dimSum = putMenu("Dim Sum");
        Menu desserts = putMenu("Desserts");
        Map<Integer, Menu> menus = menuTable.resolveAllMenus();
        assertEquals(dimSum, menus.get(dimSum.getID()));
        assertEquals(desserts, menus.get(desserts.getID()));
        teardown();
    }
}

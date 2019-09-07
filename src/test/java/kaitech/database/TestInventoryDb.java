package kaitech.database;

import kaitech.api.database.IngredientTable;
import kaitech.api.database.InventoryTable;
import kaitech.api.database.SupplierTable;
import kaitech.api.model.Ingredient;
import kaitech.model.IngredientImpl;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.*;

public class TestInventoryDb {
    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();

    private DatabaseHandler dbHandler;
    private InventoryTable inventoryTable;
    private IngredientTable ingredientTable;
    private SupplierTable supplierTable;

    public void init() throws Throwable {
        dbHandler = new DatabaseHandler(tempFolder.newFile());

        PreparedStatement suppTblStmt = dbHandler.prepareResource("/sql/setup/setupSuppliersTbl.sql");
        suppTblStmt.executeUpdate();
        supplierTable = new SupplierTblImpl(dbHandler);
        PreparedStatement ingTblStmt = dbHandler.prepareResource("/sql/setup/setupIngredientsTbl.sql");
        ingTblStmt.executeUpdate();
        ingredientTable = new IngredientTblImpl(dbHandler, supplierTable);
        PreparedStatement invTblStmt = dbHandler.prepareResource("/sql/setup/setupInventoryTbl.sql");
        invTblStmt.executeUpdate();
        inventoryTable = new InventoryTblImpl(dbHandler, ingredientTable);

    }

    public void teardown() throws SQLException {
        dbHandler.getConn().close();
    }

    private void putInventory(Ingredient ingredient, int quantity) {
        inventoryTable.putInventory(ingredient, quantity);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPutInventory() throws Throwable {
        init();
        Ingredient ingredient = new IngredientImpl("CAB");

        // Test putting negative quantity fails
        putInventory(ingredient, -1);
        PreparedStatement stmt = dbHandler.prepareStatement("SELECT * FROM inventory WHERE ingredient=\"CAB\";");
        ResultSet results = stmt.executeQuery();
        assertFalse(results.next());

        // Test putting valid quantity succeeds
        putInventory(ingredient, 0);
        results = stmt.executeQuery();
        if (results.next()) {
            assertEquals("CAB", results.getString("ingredient"));
            assertEquals(0, results.getInt("quantity"));
        } else {
            throw new RuntimeException("Unable to retrieve ingredient quantity from the database.");
        }

        teardown();
    }

    @Test
    public void testGetIngredientQuantity() throws Throwable {
        init();
        Ingredient ingredient = new IngredientImpl("CAB");
        putInventory(ingredient, 5);
        Integer quantity = inventoryTable.getIngredientQuantity(ingredient);
        assertEquals(5, (int) quantity);
        teardown();
    }

    @Test
    public void testGetAllStockCodes() throws Throwable {
        init();
        Ingredient ingredient1 = new IngredientImpl("CAB");
        putInventory(ingredient1, 0);
        Ingredient ingredient2 = new IngredientImpl("BOK");
        putInventory(ingredient2, 0);
        Set<String> ingredientCodes = inventoryTable.getAllStockCodes();
        assertEquals(2, ingredientCodes.size());
        assertTrue(ingredientCodes.contains("CAB"));
        assertTrue(ingredientCodes.contains("BOK"));
        teardown();
    }

    @Test
    public void testRemoveInventory() throws Throwable {
        init();
        Ingredient ingredient = new IngredientImpl("CAB");
        putInventory(ingredient, 0);
        assertNotNull(inventoryTable.getIngredientQuantity(ingredient)); // Check it exists in cache and database
        PreparedStatement stmt = dbHandler.prepareStatement("SELECT * FROM inventory WHERE ingredient=\"CAB\";");
        ResultSet results = stmt.executeQuery();
        assertTrue(results.next());

        inventoryTable.removeInventory(ingredient);
        assertNull(inventoryTable.getIngredientQuantity(ingredient));
        results = stmt.executeQuery();
        assertFalse(results.next());
        teardown();
    }

    @Test
    public void testResolveInventory() throws Throwable {
        init();

        Ingredient ingredient1 = new IngredientImpl("CAB");
        ingredientTable.putIngredient(ingredient1);
        putInventory(ingredient1, 0);

        Ingredient ingredient2 = new IngredientImpl("BOK");
        ingredientTable.putIngredient(ingredient2);
        putInventory(ingredient2, 0);

        Map<Ingredient, Integer> inventory = inventoryTable.resolveInventory();

        // Since the ingredient table returns a special DbIngredient, we have to get the ingredient from the
        // ingredients table to check that it resolved the inventory correctly
        assertTrue(inventory.containsKey(ingredientTable.getIngredient("CAB")));
        assertTrue(inventory.containsKey(ingredientTable.getIngredient("BOK")));
        teardown();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpdateQuantity() throws Throwable {
        init();
        Ingredient ingredient = new IngredientImpl("CAB");
        putInventory(ingredient, 1);
        Ingredient notSavedIngredient = new IngredientImpl("BOK");

        // Test negative change and ingredient doesn't exist, should throw IllegalArgumentException
        inventoryTable.updateQuantity(notSavedIngredient, -2);

        // Test negative change greater than current stock can allow, should throw IllegalArgumentException
        inventoryTable.updateQuantity(ingredient, -2);

        // Test positive change and ingredient doesn't yet exist
        inventoryTable.updateQuantity(notSavedIngredient, 4);
        assertEquals(4, (int) inventoryTable.getIngredientQuantity(notSavedIngredient));
        assertNotNull(ingredientTable.getIngredient(notSavedIngredient.getCode()));

        // Test valid change amount for ingredient which does exist
        inventoryTable.updateQuantity(ingredient, 2);
        assertEquals(3, (int) inventoryTable.getIngredientQuantity(ingredient));
        inventoryTable.updateQuantity(ingredient, -3);
        assertEquals(0, (int) inventoryTable.getIngredientQuantity(ingredient));

        teardown();
    }
}
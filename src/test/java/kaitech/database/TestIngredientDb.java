package kaitech.database;

import kaitech.api.model.Ingredient;
import kaitech.api.model.Supplier;
import kaitech.model.IngredientImpl;
import kaitech.model.SupplierImpl;
import kaitech.util.ThreeValueLogic;
import kaitech.util.UnitType;
import org.joda.money.Money;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.*;

public class TestIngredientDb {
    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();

    private DatabaseHandler dbHandler;
    private IngredientTblImpl ingredientTbl;

    public void init() throws Throwable {
        dbHandler = new DatabaseHandler(tempFolder.newFile());
        PreparedStatement stmt = dbHandler.prepareResource("/sql/setup/setupIngredientsTbl.sql");
        stmt.executeUpdate();
        stmt = dbHandler.prepareResource("/sql/setup/setupIngredientSuppliersTbl.sql");
        stmt.executeUpdate();
        ingredientTbl = new IngredientTblImpl(dbHandler);
    }

    public void teardown() throws SQLException {
        dbHandler.getConn().close();
    }

    private Ingredient putIngredient(String code) {
        Ingredient ing = new IngredientImpl(code);
        return ingredientTbl.putIngredient(ing);
    }

    @Test
    public void testPutIngredient() throws Throwable {
        init();
        Ingredient ing = putIngredient("CAB"); // Special ingredient object which updates the database
        ing.setName("Cabbage");
        ing.setUnit(UnitType.GRAM);
        ing.setPrice(Money.parse("USD 3.99"));
        ing.setIsVegan(ThreeValueLogic.NO);
        ing.setIsGF(ThreeValueLogic.YES);

        Supplier supplier = new SupplierImpl("SID");
        ing.addSupplier(supplier);

        // Test that the database actually got updated from the setters
        PreparedStatement stmt = dbHandler.prepareStatement("SELECT * FROM ingredients WHERE code=\"CAB\";");
        ResultSet results = stmt.executeQuery();
        if (results.next()) {
            assertEquals("Cabbage", results.getString("name"));
            assertEquals(UnitType.GRAM.ordinal(), results.getInt("unit"));
            assertEquals("USD 3.99", results.getString("price"));
            assertEquals(ThreeValueLogic.UNKNOWN.ordinal(), results.getInt("isVeg"));
            assertEquals(ThreeValueLogic.NO.ordinal(), results.getInt("isVegan"));
            assertEquals(ThreeValueLogic.YES.ordinal(), results.getInt("isGF"));
        }

        PreparedStatement supplierStmt = dbHandler.prepareStatement("SELECT * FROM ingredient_suppliers WHERE " +
                "ingredient=\"CAB\" AND supplier=\"SID\";");
        ResultSet supplierResults = supplierStmt.executeQuery();
        assertTrue(supplierResults.next());

        ing.removeSupplier(supplier);
        supplierResults = supplierStmt.executeQuery();
        assertFalse(supplierResults.next());
        teardown();
    }

    @Test
    public void testGetIngredient() throws Throwable {
        init();
        putIngredient("CAB");
        Ingredient ing = ingredientTbl.getIngredient("CAB");
        assertEquals("CAB", ing.getCode());
        assertEquals(ThreeValueLogic.UNKNOWN, ing.getIsVeg());
        assertEquals(ThreeValueLogic.UNKNOWN, ing.getIsVegan());
        assertEquals(ThreeValueLogic.UNKNOWN, ing.getIsGF());
        assertNull(ing.getName());
        assertEquals(UnitType.UNKNOWN, ing.getUnit());
        assertEquals(Money.parse("USD -1"), ing.getPrice());
        assertTrue(ing.getSuppliers().isEmpty());
        teardown();
    }

    @Test
    public void testGetAllIngredientCodes() throws Throwable  {
        init();
        putIngredient("CAB");
        putIngredient("BOK");
        Set<String> codes = ingredientTbl.getAllIngredientCodes();
        assertEquals(2, codes.size());
        assertTrue(codes.contains("CAB"));
        assertTrue(codes.contains("BOK"));
        teardown();
    }

    @Test
    public void testRemoveIngredient() throws Throwable {
        init();
        putIngredient("CAB");
        assertNotNull(ingredientTbl.getIngredient("CAB"));
        PreparedStatement stmt = dbHandler.prepareStatement("SELECT * FROM ingredients WHERE code=\"CAB\"");
        ResultSet results = stmt.executeQuery();
        assertTrue(results.next());

        ingredientTbl.removeIngredient("CAB");
        assertNull(ingredientTbl.getIngredient("CAB"));
        results = stmt.executeQuery();
        assertFalse(results.next());
        teardown();
    }

    @Test
    public void testResolveAllIngredients() throws Throwable  {
        init();
        putIngredient("CAB");
        putIngredient("BOK");
        Map<String, Ingredient> ingredients = ingredientTbl.resolveAllIngredients();
        assertTrue(ingredients.containsKey("CAB"));
        assertTrue(ingredients.containsKey("BOK"));
        teardown();
    }
}

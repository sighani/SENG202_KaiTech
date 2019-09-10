package kaitech.database;

import kaitech.api.database.SupplierTable;
import kaitech.api.model.Supplier;
import kaitech.model.SupplierImpl;
import kaitech.util.PhoneType;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.*;

public class TestSupplierDb {
    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();

    private DatabaseHandler dbHandler;
    private SupplierTable supplierTbl;

    public void init() throws Throwable {
        dbHandler = new DatabaseHandler(tempFolder.newFile());
        PreparedStatement stmt = dbHandler.prepareResource("/sql/setup/setupSuppliersTbl.sql");
        stmt.executeUpdate();
        supplierTbl = new SupplierTblImpl(dbHandler);
    }

    public void teardown() throws SQLException {
        dbHandler.getConn().close();
    }

    private Supplier putSupplier(String id) {
        Supplier supplier = new SupplierImpl(id);
        return supplierTbl.putSupplier(supplier);
    }

    @Test
    public void testPutSupplier() throws Throwable {
        init();
        Supplier supplier = putSupplier("SID"); // Special supplier object which updates the database
        supplier.setName("Sid Supplies");
        supplier.setAddress("Somewhere");
        supplier.setPhone("0800 SID SUPS");
        supplier.setPhoneType(PhoneType.WORK);
        supplier.setEmail("sid@sidsupplies.com");
        supplier.setUrl("www.sidsupplies.com");

        // Test the database got updated from the setters
        PreparedStatement stmt = dbHandler.prepareStatement("SELECT * FROM suppliers WHERE id=\"SID\";");
        ResultSet results = stmt.executeQuery();
        if (results.next()) {
            assertEquals("Sid Supplies", results.getString("name"));
            assertEquals("Somewhere", results.getString("addr"));
            assertEquals("0800 SID SUPS", results.getString("ph"));
            assertEquals(PhoneType.WORK.ordinal(), results.getInt("phType"));
            assertEquals("sid@sidsupplies.com", results.getString("email"));
            assertEquals("www.sidsupplies.com", results.getString("url"));
        } else {
            throw new RuntimeException("Could not retrieve supplier from database.");
        }

        teardown();
    }

    @Test
    public void testGetSupplier() throws Throwable {
        init();
        putSupplier("SID");
        Supplier supplier = supplierTbl.getSupplier("SID");
        assertNull(supplier.getName());
        assertNull(supplier.getAddress());
        assertNull(supplier.getPhone());
        assertNull(supplier.getPhoneType());
        assertEquals(Supplier.UNKNOWN_EMAIL, supplier.getEmail());
        assertEquals(Supplier.UNKNOWN_URL, supplier.getUrl());
        teardown();
    }

    @Test
    public void testGetAllSupplierIDs() throws Throwable {
        init();
        putSupplier("SID");
        putSupplier("CID");
        Set<String> ids = supplierTbl.getAllSupplierIDs();
        assertEquals(2, ids.size());
        assertTrue(ids.contains("SID"));
        assertTrue(ids.contains("CID"));
        teardown();
    }

    @Test
    public void testRemoveSupplier() throws Throwable {
        init();
        putSupplier("SID");
        assertNotNull(supplierTbl.getSupplier("SID"));
        PreparedStatement stmt = dbHandler.prepareStatement("SELECT * FROM suppliers WHERE id=\"SID\";");
        ResultSet results = stmt.executeQuery();
        assertTrue(results.next());

        supplierTbl.removeSupplier("SID");
        assertNull(supplierTbl.getSupplier("SID"));
        results = stmt.executeQuery();
        assertFalse((results.next()));
        teardown();
    }

    @Test
    public void testResolveAllSuppliers() throws Throwable {
        init();
        putSupplier("SID");
        putSupplier("CID");
        Map<String, Supplier> suppliers = supplierTbl.resolveAllSuppliers();
        assertTrue(suppliers.containsKey("SID"));
        assertTrue(suppliers.containsKey("CID"));
        teardown();
    }
}

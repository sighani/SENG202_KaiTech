package kaitech.database;

import kaitech.api.database.AbstractTable;
import kaitech.api.database.SupplierTable;
import kaitech.api.model.Supplier;
import kaitech.model.SupplierImpl;
import kaitech.util.PhoneType;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Collections.singletonMap;

/**
 * SupplierTblImpl extends AbstractTable, implements the SupplierTable interface, and permits limited access to the
 * data stored in the suppliers table.
 *
 * @author Julia Harrison
 */
public class SupplierTblImpl extends AbstractTable implements SupplierTable {
    private final Set<String> ids = new HashSet<>();
    private final Map<String, Supplier> suppliers = new HashMap<>();
    private final String tableName = "suppliers";
    private final String tableKey = "id";

    protected SupplierTblImpl(DatabaseHandler dbHandler) {
        super(dbHandler);
        PreparedStatement getIDsQuery = dbHandler.prepareStatement("SELECT id FROM suppliers;");
        ResultSet results;
        try {
            results = getIDsQuery.executeQuery();
            while (results.next()) {
                ids.add(results.getString("id"));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to load suppliers.", e);
        }
    }

    @Override
    public Supplier getSupplier(String id) {
        Supplier supplier = suppliers.get(id);

        if (supplier == null && suppliers.containsKey(id)) {
            try {
                PreparedStatement getSuppQuery = dbHandler.prepareStatement("SELECT * FROM suppliers WHERE id=?;");
                getSuppQuery.setString(1, id);
                ResultSet results = getSuppQuery.executeQuery();
                if (results.next()) {
                    PhoneType[] phTypes = PhoneType.values();
                    supplier = new DbSupplier(results.getString("id"),
                            results.getString("name"),
                            results.getString("addr"),
                            results.getString("ph"),
                            phTypes[results.getInt("phType")],
                            results.getString("email"),
                            results.getString("url"));
                    suppliers.put(id, supplier);
                }
            } catch (SQLException e) {
                throw new RuntimeException("Unable to retrieve ingredient from database.", e);
            }
        }

        return supplier;
    }

    @Override
    public Set<String> getAllSupplierIDs() {
        return Collections.unmodifiableSet(ids);
    }

    @Override
    public Supplier putSupplier(Supplier from) {
        try {
            PreparedStatement putSuppStmt = dbHandler.prepareResource("/sql/modify/insert/insertSupplier.sql");
            String id = from.getID();
            putSuppStmt.setString(1, id);
            putSuppStmt.setString(2, from.getName());
            putSuppStmt.setString(3, from.getAddress());
            putSuppStmt.setString(4, from.getPhone());
            putSuppStmt.setInt(5, from.getPhoneType().ordinal());
            putSuppStmt.setString(6, from.getEmail());
            putSuppStmt.setString(7, from.getURL());
            putSuppStmt.executeUpdate();
            Supplier dbSupplier = new DbSupplier(from);
            ids.add(id);
            suppliers.put(id, dbSupplier);
            return dbSupplier;
        } catch (SQLException e) {
            throw new RuntimeException("Unable to save supplier to database.", e);
        }
    }

    @Override
    public void removeSupplier(String id) {
        try {
            PreparedStatement removeSuppStmt = dbHandler.prepareResource("/sql/modify/delete/deleteSupplier.sql");
            removeSuppStmt.setString(1, id);
            removeSuppStmt.executeUpdate();
            ids.remove(id);
            suppliers.remove(id);
        } catch (SQLException e) {
            throw new RuntimeException("Unable to delete supplier from database.", e);
        }
    }

    @Override
    public Map<String, Supplier> resolveAllSuppliers() {
        return ids.stream() //
                .map(this::getSupplier) //
                .collect(Collectors.toMap(Supplier::getID, e -> e));
    }

    private class DbSupplier extends SupplierImpl {
        private final Map<String, Object> key;

        public DbSupplier(String id) {
            super(id);
            key = singletonMap(tableKey, getID());
        }

        public DbSupplier(String id, String name) {
            super(id, name);
            key = singletonMap(tableKey, getID());
        }

        public DbSupplier(String id, String name, String addr, String ph) {
            super(id, name, addr, ph);
            key = singletonMap(tableKey, getID());
        }

        public DbSupplier(String id, String n, String addr, String ph, PhoneType pt, String email, String url) {
            super(id, n, addr, ph, pt, email, url);
            key = singletonMap(tableKey, getID());
        }

        public DbSupplier(Supplier from) {
            super(from.getID(), from.getName(), from.getAddress(), from.getPhone(), from.getPhoneType(),
                    from.getEmail(), from.getEmail());
            key = singletonMap(tableKey, getID());
        }

        @Override
        public void setName(String name) {
            updateColumn(tableName, key, "name", name);
            super.setName(name);
        }

        @Override
        public void setAddress(String address) {
            updateColumn(tableName, key, "addr", address);
            super.setAddress(address);
        }

        @Override
        public void setPhone(String phone) {
            updateColumn(tableName, key, "ph", phone);
            super.setPhone(phone);
        }

        @Override
        public void setPhoneType(PhoneType pt) {
            updateColumn(tableName, key, "phType", pt);
            super.setPhoneType(pt);
        }

        @Override
        public void setEmail(String email) {
            updateColumn(tableName, key, "email", email);
            super.setEmail(email);
        }

        @Override
        public void setURL(String url) {
            updateColumn(tableName, key, "url", url);
            super.setURL(url);
        }
    }
}

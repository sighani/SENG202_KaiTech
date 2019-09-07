package kaitech.api.database;

import kaitech.api.model.Supplier;

import java.util.Map;
import java.util.Set;

/**
 * The SupplierTable interface declares required functionality to store, retrieve, and modify suppliers in the
 * database.
 *
 * @author Julia Harrison
 */
public interface SupplierTable {

    /**
     * Retrieves a supplier from the cache. If the supplier does not currently exist in the cache, query the
     * database for the supplier. If the query is successful, add the supplier to the cache and return it.
     *
     * @param id The string ID of the supplier to retrieve.
     * @return A Supplier if the supplier exists, null if it does not or cannot be retrieved.
     */
    Supplier getSupplier(String id);

    /**
     * Get all supplier IDs currently in the database.
     *
     * @return A set of supplier codes.
     */
    Set<String> getAllSupplierIDs();

    /**
     * Saves a given supplier to the database, and returns a database managed Supplier which is responsible for
     * the automatic saving of changes on setter calls.
     *
     * @param from The Supplier to be saved.
     * @return Database managed Supplier responsible for automatic saving on setter calls.
     */
    Supplier putSupplier(Supplier from);

    /**
     * Remove a supplier from the database and cache.
     *
     * @param id The ID of the supplier to be removed.
     */
    void removeSupplier(String id);

    /**
     * Gets all supplier objects from the database.
     * Warning: this can be a costly operation when everything isn't already cached, please use with care.
     *
     * @return A map from code to Supplier.
     */
    Map<String, Supplier> resolveAllSuppliers();

    /**
     * From a given Supplier, gets or creates the database equivalent.
     *
     * @param from The Supplier to have its database equivalent retrieved or added.
     * @return Database managed Supplier responsible for automatic saving on setter calls.
     */
    default Supplier getOrAddSupplier(Supplier from) {
        Supplier existing = getSupplier(from.getID());
        if (existing == null) {
            existing = putSupplier(from);
        }
        return existing;
    }
}

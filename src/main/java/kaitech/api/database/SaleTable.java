package kaitech.api.database;

import kaitech.api.model.Sale;

import java.util.Map;
import java.util.Set;

/**
 * The SaleTable interface declares required functionality to store, retrieve, and modify sales in the database.
 *
 * @author Julia Harrison
 */
public interface SaleTable {

    /**
     * Retrieves a sale from the cache. If the sale does not currently exist in the cache, query the database for the
     * sale. If the query is successful, add the sale to the cache and return it.
     *
     * @param receiptNo The integer receipt number of the sale to be retrieved.
     * @return A Sale if the sale exists, null if it does not.
     */
    Sale getSale(int receiptNo);

    /**
     * Get all receipt numbers for sales currently in the database.
     *
     * @return A set of all integer receipt numbers.
     */
    Set<Integer> getAllReceiptNo();

    /**
     * Saves a given sale to the database, and returns a database managed Sale which is responsible for the automatic
     * saving of changes on setter calls.
     *
     * @param from The Sale to be saved.
     * @return Database managed Sale responsible for automatic saving on setter calls.
     */
    Sale putSale(Sale from);

    /**
     * Remove a sale from the database and cache.
     *
     * @param receiptNo The receipt number of the sale to be removed.
     */
    void removeSale(int receiptNo);

    /**
     * Gets all sale objects from the database.
     * Warning: this can be a costly operation when everything isn't already cached, please use with care.
     *
     * @return A map from receipt number to Sale.
     */
    Map<Integer, Sale> resolveAllSales();
}

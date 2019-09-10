package kaitech.api.database;

import kaitech.api.model.MenuItem;

import java.util.Map;
import java.util.Set;

/**
 * The MenuItemTable interface declares required functionality to store, retrieve, and modify MenuItems in the
 * database.
 *
 * @author Julia Harrison
 */
public interface MenuItemTable {

    /**
     * Retrieves a MenuItem from the cache. If the MenuItem does not currently exist in the cache, query the
     * database for the MenuItem. If the query is successful, add the MenuItem to the cache and return it.
     *
     * @param code The string code of the ingredient to retrieve.
     * @return An Ingredient if the ingredient exists, null if it does not or cannot be retrieved.
     */
    MenuItem getMenuItem(String code);

    /**
     * Get all MenuItem codes currently in the database.
     *
     * @return A set of MenuItem codes.
     */
    Set<String> getAllIMenuItemCodes();

    /**
     * Saves a given MenuItem to the database, and returns a database managed MenuItem which is responsible for
     * the automatic saving of changes on setter calls.
     *
     * @param from The MenuItem to be saved.
     * @return Database managed MenuItem responsible for automatic saving on setter calls.
     */
    MenuItem putMenuItem(MenuItem from);

    /**
     * Remove a MenuItem from the database and cache.
     *
     * @param code The code of the MenuItem to be removed.
     */
    void removeMenuItem(String code);

    /**
     * Gets all MenuItem objects from the database.
     * Warning: this can be a costly operation when everything isn't already cached, please use with care.
     *
     * @return A map from code to MenuItem.
     */
    Map<String, MenuItem> resolveAllMenuItems();

    /**
     * From a given MenuItem, gets or creates the database equivalent.
     *
     * @param from The MenuItem to have its database equivalent retrieved or added.
     * @return Database managed MenuItem responsible for automatic saving on setter calls.
     */
    default MenuItem getOrAddItem(MenuItem from) {
        MenuItem existing = getMenuItem(from.getCode());
        if (existing == null) {
            existing = putMenuItem(from);
        }
        return existing;
    }
}

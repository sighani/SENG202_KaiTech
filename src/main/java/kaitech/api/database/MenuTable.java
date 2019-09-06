package kaitech.api.database;

import kaitech.api.model.Menu;

import java.util.Map;
import java.util.Set;

/**
 * The MenuTable interface declares required functionality to store, retrieve, and modify menus in the database.
 *
 * @author Julia Harrison
 */
public interface MenuTable {

    /**
     * Retrieves a menu from the cache. If the menu does not currently exist in the cache, query the database for the
     * menu. If the query is successful, add the menu to the cache and return it.
     *
     * @param id The integer ID of the menu to be retrieved.
     * @return A Menu if the menu exists, null if it does not.
     */
    Menu getMenu(int id);

    /**
     * Get all IDs for menus currently in the database.
     *
     * @return A set of all String IDs.
     */
    Set<Integer> getAllMenuIDs();

    /**
     * Saves a given menu to the database, and returns a database managed menu which is responsible for the automatic
     * saving of changes on setter calls.
     *
     * @param from The menu to be saved.
     * @return Database managed menu responsible for automatic saving on setter calls.
     */
    Menu putMenu(Menu from);

    /**
     * Remove a menu from the database and cache.
     *
     * @param id The integer ID of the menu to be removed.
     */
    void removeMenu(int id);

    /**
     * Gets all menu objects from the database.
     * Warning: this can be a costly operation when everything isn't already cached, please use with care.
     *
     * @return A map from receipt number to menu.
     */
    Map<Integer, Menu> resolveAllMenus();
}

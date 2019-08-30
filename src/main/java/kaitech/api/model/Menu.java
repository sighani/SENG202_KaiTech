package kaitech.api.model;

import java.util.Map;

/**
 * This interface implements Menus. A business may have multiple menus such as
 * a normal menu and a vegetarian menu. The main purpose of Menu is to collect
 * a list of MenuItems that belong together to assist in organisation.
 *
 * @author Julia Harrison
 */
public interface Menu {
    /**
     * Adds a MenuItem to the map of menu item codes to menu items.
     *
     * @param item The {@link MenuItem} to be added.
     */
    void addMenuItem(MenuItem item);

    /**
     * Removes the given MenuItem from the menu.
     *
     * @param item The {@link MenuItem} to be removed.
     */
    void removeMenuItem(MenuItem item);

    /**
     * @return A map of all the items in the menu, which is a map from their String names to the MenuItem.
     */
    Map<String, MenuItem> getMenuItems();

    /**
     * @return The String title of the menu.
     */
    String getTitle();

    /**
     * @return The String description of the menu.
     */
    String getDescription();

    /**
     * @return The ID of the menu, as a String.
     */
    String getId();

    /**
     * Sets the String ID of the menu.
     *
     * @param id The new String ID of the menu.
     */
    void setId(String id);
}

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
     * @return The ID of the menu, as an integer.
     */
    int getID();

    /**
     * @return The String title of the menu.
     */
    String getTitle();

    /**
     * @return The String description of the menu.
     */
    String getDescription();

    /**
     * @return A map of all the items in the menu, which is a map from their code to the MenuItem.
     */
    Map<String, MenuItem> getMenuItems();

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

    void setTitle(String title);

    void setDescription(String description);

    void setMenuItems(Map<String, MenuItem> menuItems);
}

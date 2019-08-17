package kaitech.model;

import java.util.HashMap;
import java.util.Map;

/**
 * This class implements Menus. A business may have multiple menus such as
 * a normal menu and a vegetarian menu. The main purpose of Menu is to collect
 * a list of MenuItems that belong together to assist in organisation.
 */
public class Menu {
    /**
     * The name of the menu
     */
    private String name;

    /**
     * A unique code for the menu
     */
    private String id;

    /**
     * A map of all the items in the menu, which is a map from their String names to the MenuItem
     */
    private Map<String, MenuItem> menuItems;

    public Menu(String name, String id) {
        this.name = name;
        this.id = id;
        menuItems = new HashMap<String, MenuItem>();
    }
}

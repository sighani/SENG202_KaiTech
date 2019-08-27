package kaitech.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
    private String title, description;

    /**
     * A unique code for the menu
     */
    private String id;

    /**
     * A map of all the items in the menu, which is a map from their String names to the MenuItem
     */
    private Map<String, MenuItem> menuItems;

    //overloading constructor because this is needed
    public Menu(String title, String description, Map<String, MenuItem> menuItems){
        this.title = title;
        this.description = description;
        this.menuItems = menuItems;
    }

    public Menu(String name, String id) {
        this.title = name;
        this.id = id;
        menuItems = new HashMap<String, MenuItem>();
    }

    public void addMenuItem(MenuItem item) {
        //need to fix
    }

    public void removeMenuItem(MenuItem item) {
        menuItems.remove(item);
    }

    public Map<String, MenuItem> getMenuItems() {
        return menuItems;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }
}

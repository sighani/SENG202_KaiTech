package kaitech.model;

import kaitech.api.model.Menu;
import kaitech.api.model.MenuItem;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MenuImpl implements Menu {
    /**
     * A unique code for the menu
     */
    private final int id;

    /**
     * The name of the menu
     */
    private String title;

    /**
     * A description of the menu
     */
    private String description;

    /**
     * A map of all the items in the menu, which is a map from their code to the MenuItem
     */
    private final Map<String, MenuItem> menuItems = new HashMap<>();

    public MenuImpl(String title, String description, Map<String, MenuItem> menuItems) {
        this.id = -1;
        this.title = title;
        this.description = description;
        this.menuItems.putAll(menuItems);
    }

    public MenuImpl(int id, String title, String description, Map<String, MenuItem> menuItems) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.menuItems.putAll(menuItems);
    }

    public MenuImpl(String title) {
        this.id = -1;
        this.title = title;
    }

    public MenuImpl(int id, String title) {
        this.id = id;
        this.title = title;
    }

    @Override
    public int getID() {
        return id;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public Map<String, MenuItem> getMenuItems() {
        //Unmodifiable so database can easily track changes.
        return Collections.unmodifiableMap(menuItems);
    }

    @Override
    public void addMenuItem(MenuItem item) {
        menuItems.put(item.getCode(), item);
    }

    @Override
    public void removeMenuItem(MenuItem item) {
        menuItems.remove(item.getCode(), item);
    }

    @Override
    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public void setMenuItems(Map<String, MenuItem> menuItems) {
        this.menuItems.clear();
        this.menuItems.putAll(menuItems);
    }

    @Override
    public boolean equals(Object obj) {
        if (super.equals(obj)) return true;
        if (!(obj instanceof MenuImpl)) return false;
        MenuImpl other = (MenuImpl) obj;
        return Objects.equals(other.getID(), getID())
                && Objects.equals(other.getTitle(), getTitle()) //
                && Objects.equals(other.getDescription(), getDescription()) //
                && Objects.equals(other.getMenuItems(), getMenuItems());
    }

    @Override
    public int hashCode() {
        int i = 0;
        i = 31 * i + getID();
        i = 31 * i + (getTitle() == null ? 0 : getTitle().hashCode());
        i = 31 * i + (getDescription() == null ? 0 : getDescription().hashCode());
        i = 31 * i + (getMenuItems() == null ? 0 : getMenuItems().hashCode());
        return i;
    }
}

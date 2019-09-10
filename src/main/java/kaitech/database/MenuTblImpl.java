package kaitech.database;

import kaitech.api.database.AbstractTable;
import kaitech.api.database.MenuItemTable;
import kaitech.api.database.MenuTable;
import kaitech.api.model.Menu;
import kaitech.api.model.MenuItem;
import kaitech.model.MenuImpl;
import org.apache.commons.lang3.tuple.Pair;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.Collections.singletonMap;

public class MenuTblImpl extends AbstractTable implements MenuTable {
    private final MenuItemTable menuItemTable;
    private final Set<Integer> menuIDs = new HashSet<>();
    private final Map<Integer, Menu> menus = new HashMap<>();
    private final String tableName = "menus";
    private final String tableKey = "id";

    public MenuTblImpl(DatabaseHandler dbHandler, MenuItemTable menuItemTable) { //TODO: Throw exception GUI can catch
        super(dbHandler);
        this.menuItemTable = menuItemTable;
        PreparedStatement stmt = dbHandler.prepareStatement("SELECT id FROM menus");
        ResultSet results;
        try {
            results = stmt.executeQuery();
            while (results.next()) {
                menuIDs.add(results.getInt("id"));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Unable to retrieve menus.", e);
        }
    }

    private Map<String, MenuItem> getMenuItems(int menuID) {
        Map<String, MenuItem> menuItems = new HashMap<>();
        try {
            PreparedStatement stmt = dbHandler.prepareStatement("SELECT * FROM menu_contents WHERE menuID=?;");
            stmt.setInt(1, menuID);
            ResultSet results = stmt.executeQuery();
            while (results.next()) {
                String code = results.getString("itemCode");
                menuItems.put(code, menuItemTable.getMenuItem(code));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Unable to retrieve menu items for specified menu.", e);
        }

        return menuItems;
    }

    @Override
    public Menu getMenu(int id) {
        Menu menu = menus.get(id);

        if (menu == null && menuIDs.contains(id)) {
            try {
                PreparedStatement stmt = dbHandler.prepareStatement("SELECT * FROM menus WHERE id=?;");
                stmt.setInt(1, id);
                ResultSet results = stmt.executeQuery();
                if (results.next()) {
                    Map<String, MenuItem> menuItems = getMenuItems(id);
                    menu = new DbMenu(id,
                            results.getString("title"),
                            results.getString("description"),
                            menuItems
                    );
                    menus.put(id, menu);
                }
            } catch (SQLException e) {
                throw new RuntimeException("Unable to retrieve menu from database.", e);
            }
        }

        return menu;
    }

    @Override
    public Set<Integer> getAllMenuIDs() {
        return Collections.unmodifiableSet(menuIDs);
    }

    @Override
    public Menu putMenu(Menu from) {
        try {
            PreparedStatement stmt = dbHandler.prepareResource("/sql/modify/insert/insertMenu.sql");
            stmt.setObject(1, null);
            stmt.setString(2, from.getTitle());
            stmt.setString(3, from.getDescription());
            stmt.executeUpdate();

            int menuID;
            ResultSet results = stmt.getGeneratedKeys();
            if (results.next()) {
                menuID = results.getInt(1);
            } else {
                throw new RuntimeException("Could not retrieve auto incremented ID for menu.");
            }

            Menu menu = new DbMenu(menuID, from);
            menu.setMenuItems(from.getMenuItems());
            menuIDs.add(menuID);
            menus.put(menuID, menu);
            return menu;
        } catch (SQLException e) {
            throw new RuntimeException("Unable to save menu to database.", e);
        }
    }

    @Override
    public void removeMenu(int id) {
        try {
            PreparedStatement stmt = dbHandler.prepareResource("/sql/modify/delete/deleteMenu.sql");
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Unable to delete menu from the database.", e);
        }
        menuIDs.remove(id);
        menus.remove(id);
    }

    @Override
    public Map<Integer, Menu> resolveAllMenus() {
        return menuIDs.stream() //
                .map(this::getMenu) //
                .collect(Collectors.toMap(Menu::getID, Function.identity()));
    }

    private class DbMenu extends MenuImpl {
        private final Map<String, Object> key;

        public DbMenu(int id, String title, String description, Map<String, MenuItem> menuItems) {
            super(id, title, description, menuItems);
            key = singletonMap(tableKey, getID());
        }

        public DbMenu(int id, Menu other) {
            super(id, other.getTitle(), other.getDescription(), other.getMenuItems());
            key = singletonMap(tableKey, getID());
        }

        @Override
        public void setTitle(String title) {
            updateColumn(tableName, key, "title", title);
            super.setTitle(title);
        }

        @Override
        public void setDescription(String description) {
            updateColumn(tableName, key, "description", description);
            super.setDescription(description);
        }

        @Override
        public void setMenuItems(Map<String, MenuItem> menuItems) {
            int menuID = getID();
            List<List<Object>> values = new ArrayList<>();
            for (String code : menuItems.keySet()) {
                values.add(Arrays.asList(menuID, code));
            }
            insertRows("menu_contents", values);
            super.setMenuItems(menuItems.entrySet().stream() //
                    .map(e -> Pair.of(menuItemTable.getOrAddItem(e.getValue()), e.getKey())) //
                    .collect(Collectors.toMap(Pair::getValue, Pair::getKey)) //
            );
        }

        @Override
        public void addMenuItem(MenuItem item) {
            try {
                PreparedStatement stmt = dbHandler.prepareResource("/sql/modify/insert/insertMenuContents.sql");
                stmt.setInt(1, getID());
                stmt.setString(2, item.getCode());
                stmt.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException("Unable to add menu item to menu.", e);
            }
            super.addMenuItem(menuItemTable.getOrAddItem(item));
        }

        @Override
        public void removeMenuItem(MenuItem item) {
            try {
                PreparedStatement stmt = dbHandler.prepareResource("/sql/modify/delete/deleteMenuContents.sql");
                stmt.setInt(1, getID());
                stmt.setString(2, item.getCode());
                stmt.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException("Unable to remove menu item from menu.", e);
            }
            super.removeMenuItem(item);
        }
    }
}

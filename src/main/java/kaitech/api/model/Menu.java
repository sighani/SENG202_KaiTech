package kaitech.api.model;

import java.util.Map;

public interface Menu {
    void addMenuItem(MenuItem item);

    void removeMenuItem(MenuItem item);

    Map<String, MenuItem> getMenuItems();

    String getTitle();

    String getDescription();
}

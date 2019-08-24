package kaitech;

import kaitech.database.DatabaseHandler;

public class TestStuff {
    public static void main(String[] args) {
        DatabaseHandler dbHandler = new DatabaseHandler();
        dbHandler.setup();
    }
}

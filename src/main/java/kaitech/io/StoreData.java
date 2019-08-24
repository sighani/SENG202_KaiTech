package kaitech.io;

import java.sql.*;

import kaitech.model.Ingredient;
import kaitech.model.MenuItem;
import kaitech.model.Supplier;

/**
 * Class to store input data persistently in the SQL database.
 *
 * @author Julia Harrison
 */

public class StoreData {

    //TODO: Determine if this class is "correct". Store vs update.

    /**
     * Adds the ingredient and its quantity to the (local) SQL database if it does not already exist in the db.
     * If the ingredient already exists, updates the quantity of the item.
     */
    public static void storeIngredient() {} //TODO: Determine whether update to equal passed in num, or to change by passed num

    /**
     * Adds a new menu item to the (local) SQL database if the menu item does not already exist.
     * If the menu item already exists, updates the database with any changes.
     */
    public static void storeMenu() {}

    /**
     * Adds the new supplier to the (local) SQL database if the supplier does not already exist.
     * If the supplier already exists, updates the database with any changes.
     */
    public static void storeSupplier() {}
}

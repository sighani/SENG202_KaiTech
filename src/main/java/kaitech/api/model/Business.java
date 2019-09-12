package kaitech.api.model;

import kaitech.api.database.*;

import java.util.ArrayList;

/**
 * Main interface for the business. Keeps track of the model classes (suppliers
 * etc.) that we have as well as performing major functions.
 *
 * @author Julia Harrison
 */
public interface Business {

    IngredientTable getIngredientTable();

    InventoryTable getInventoryTable();

    MenuItemTable getMenuItemTable();

    MenuTable getMenuTable();

    RecipeTable getRecipeTable();

    SaleTable getSaleTable();

    SupplierTable getSupplierTable();

    /**
     * Logs the user in given that the pin is correct.
     *
     * @param attempt The user's entered int for the pin
     * @return A boolean, true is the user is now logged in, false otherwise
     * @throws IllegalStateException If the user is already logged in or the pin is unset.
     */
    boolean logIn(String attempt) throws IllegalStateException;

    /**
     * A method that logs the user out. This is done instead of a basic setter to increase security, such that calls
     * like "business.setLoggedIn = true" are not possible.
     */
    void logOut();

    /**
     * @return A boolean for whether the user is currently logged in.
     */
    boolean isLoggedIn();

    /**
     * Sets the Business' PIN to a new value, which must be 4 in length and contain digits only.
     *
     * @param pin The new String PIN
     * @throws IllegalArgumentException If the PIN contains non-numeric values or is shorter or longer than 4
     */
    void setPin(String pin) throws IllegalArgumentException;

    /**
     * Gets the PIN for the business that must be entered to access restricted actions.
     * Note that this is done for testing purposes. This getter should not be used anywhere else for security reasons.
     *
     * @return The PIN, as a string.
     */
    String getPin();

    /**
     * @return True if the business PIN is null, false if not.
     */
    boolean getPinIsNull();

    ArrayList<MenuItem> getAffectedMenuItems(Ingredient ingredient);
}

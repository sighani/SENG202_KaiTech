package kaitech.api.model;

import kaitech.api.database.*;

import java.util.List;

/**
 * Main interface for the business. Keeps track of the model classes (suppliers
 * etc.) that we have as well as performing major functions.
 *
 * @author Julia Harrison
 */
public interface Business {

    String DEFAULT_USER = "manager";

    IngredientTable getIngredientTable();

    InventoryTable getInventoryTable();

    MenuItemTable getMenuItemTable();

    MenuTable getMenuTable();

    RecipeTable getRecipeTable();

    SaleTable getSaleTable();

    SupplierTable getSupplierTable();

    PinTable getPinTable();

    /**
     * Logs the user in given that the pin is correct.
     *
     * @param name The name of the user.
     * @param pin  The user's entered CharSequence pin.
     * @return A boolean, true is the user is now logged in, false otherwise.
     * @throws IllegalStateException If the user is already logged in or the pin is unset.
     */
    boolean logIn(String name, CharSequence pin) throws IllegalStateException;

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
     * @param name The name of the user associated with the pin.
     * @param pin  The new CharSequence PIN.
     * @throws IllegalArgumentException If the PIN contains non-numeric values or is shorter or longer than 4
     */
    void setPin(String name, CharSequence pin) throws IllegalArgumentException;

    /**
     * @return True if the business PIN is null, false if not.
     */
    boolean getIsPinEmpty(String name);

    List<MenuItem> getAffectedMenuItems(Ingredient ingredient);
}

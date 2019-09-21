package kaitech.api.model;

import kaitech.api.database.*;

import java.util.List;

/**
 * Main interface for the business. Keeps track of the model classes (suppliers
 * etc.) that we have as well as performing major functions.
 */
public interface Business {

    /**
     * Default name for PIN.
     */
    String DEFAULT_USER = "manager";

    /**
     * @return The IngredientTable for the business.
     */
    IngredientTable getIngredientTable();

    /**
     * @return The InventoryTable for the business.
     */
    InventoryTable getInventoryTable();

    /**
     * @return The MenuItemTable for the business.
     */
    MenuItemTable getMenuItemTable();

    /**
     * @return The MenuTable for the business.
     */
    MenuTable getMenuTable();

    /**
     * @return The RecipeTable for the business.
     */
    RecipeTable getRecipeTable();

    /**
     * @return The SaleTable for the business.
     */
    SaleTable getSaleTable();

    /**
     * @return The SupplierTable for the business.
     */
    SupplierTable getSupplierTable();

    /**
     * @return The PinTable for the business.
     */
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

    /**
     * Get a list of the MenuItems which contain the given ingredient.
     *
     * @param ingredient The Ingredient to search for in the Recipe of MenuItems.
     * @return A List of MenuItems which have a Recipe containing the given ingredient.
     */
    List<MenuItem> getAffectedMenuItems(Ingredient ingredient);
}

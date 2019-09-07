package kaitech.api.model;

import kaitech.api.database.*;

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

    void nukeDatabase();
//    /**
//     * Adds a specified supplier from the list
//     *
//     * @param s The Supplier to add
//     */
//    void addSupplier(Supplier s);
//
//    /**
//     * Removes a specified supplier from the list
//     *
//     * @param s The Supplier to remove
//     */
//    void removeSupplier(Supplier s);
//
//    /**
//     * Increases the quantity of a given ingredient by a given amount. Returns True if the ingredient
//     * is in the ingredients HashMap, False otherwise.
//     *
//     * @param i   The ingredient to update
//     * @param amt The amount to increase by
//     * @return A boolean depending on whether the ingredient is in the map
//     */
//    boolean increaseIngredientQuantity(Ingredient i, int amt);
//
//    /**
//     * Decreases the quantity of a given ingredient by a given amount. Returns True if the ingredient
//     * is in the ingredients HashMap, False otherwise.
//     *
//     * @param i   The Ingredient to update
//     * @param amt The int amount to decrease by
//     * @return A boolean depending on whether the ingredient is in the map
//     */
//    boolean decreaseIngredientQuantity(Ingredient i, int amt);
//
//    /**
//     * Adds a new ingredient with a quantity of zero. Returns true if the ingredient is not already in
//     * the map, false otherwise.
//     *
//     * @param i The new ingredient to add
//     * @return A boolean depending on whether the ingredient is in the map
//     */
//    boolean addIngredient(Ingredient i);
//
//    /**
//     * Adds a new ingredient with a quantity of of the specified amount. Returns true if the ingredient is not already
//     * in the map, false otherwise.
//     *
//     * @param i   The new Ingredient to add
//     * @param amt The int initial amount
//     * @return A boolean depending on whether the ingredient is in the map
//     */
//    boolean addIngredient(Ingredient i, int amt);

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

//    /**
//     * @return A list of all Suppliers known to the business.
//     */
//    List<Supplier> getSuppliers();
//
//    /**
//     * @return A map of an ingredient's code (String) to the Ingredient, for all ingredients known to the business.
//     */
//    Map<String, Ingredient> getIngredients();
//
//    /**
//     * @return A map of an Ingredient to its integer quantity in stock.
//     */
//    Map<Ingredient, Integer> getInventory();
//
//    /**
//     * Sets the ingredients known to the business to the passed in map of ingredient code to Ingredient.
//     *
//     * @param ingredients A map of ingredient code (String) to Ingredient.
//     */
//    void setIngredients(Map<String, Ingredient> ingredients);
//
//    /**
//     * Sets the inventory of the business to the passed in map of Ingredient to its integer quantity.
//     *
//     * @param inventory A map of Ingredient to its integer quantity in stock.
//     */
//    void setInventory(Map<Ingredient, Integer> inventory);
//
//    /**
//     * Sets the Suppliers known to the business to the passed in list.
//     *
//     * @param s A list of Suppliers.
//     */
//    void setSuppliers(List<Supplier> s);
}

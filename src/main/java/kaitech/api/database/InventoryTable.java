package kaitech.api.database;

import kaitech.api.model.Ingredient;
import kaitech.api.model.MenuItem;

import java.util.Map;
import java.util.Set;

/**
 * The InventoryTable interface declares required functionality to store, retrieve, and modify ingredients and their
 * quantity in the database.
 *
 * @author Julia Harrison
 */
public interface InventoryTable {

    /**
     * Retrieves an ingredient's quantity in stock from the cache. If the ingredient does not currently exist in the
     * cache, query the database for the quantity. If the query is successful, add the ingredient and its quantity
     * to the cache and return it.
     *
     * @param ingredient The Ingredient to retrieve.
     * @return The integer quantity of the ingredient in stock if it exists, null if it does not or cannot be retrieved.
     */
    Integer getIngredientQuantity(Ingredient ingredient);

    /**
     * Get the codes of all ingredients currently in stock.
     *
     * @return A set of ingredient codes.
     */
    Set<String> getAllStockCodes();

    /**
     * Saves a given ingredient and its quantity to the database, assuming that the ingredient already exists in the
     * ingredients table.
     *
     * @param ingredient The Ingredient to be saved.
     * @param quantity   The quantity of the ingredient in stock.
     */
    void putInventory(Ingredient ingredient, int quantity);

    /**
     * Remove an ingredient and its quantity from the database and cache.
     *
     * @param ingredient The Ingredient to be removed.
     */
    void removeInventory(Ingredient ingredient);

    /**
     * Gets all ingredients and their quantities from the database, given the ingredients table.
     * Warning: this can be a costly operation when everything isn't already cached, please use with care.
     *
     * @return A map from Ingredient to integer quantity.
     */
    Map<Ingredient, Integer> resolveInventory();

    /**
     * Directly set the quantity of the ingredient in the inventory table.
     * Throws IllegalArgumentException if the amount is negative.
     *
     * @param ingredient The Ingredient to set the quantity of.
     * @param newAmt The new amount for the Ingredient.
     */
    void setQuantity(Ingredient ingredient, int newAmt);

    /**
     * Updates the given ingredient's quantity by the amount specified, which may be positive or negative.
     * Throws IllegalArgumentException if the amount is negative and would result in the ingredient having a negative
     * quantity.
     *
     * @param ingredient The Ingredient to update the quantity of.
     * @param change     The signed integer amount to update the ingredient's quantity by.
     */
    void updateQuantity(Ingredient ingredient, int change) throws IllegalArgumentException;

    /**
     * Updates the inventory to remove the items used in the recipes of menu items from sales.
     *
     * @param itemsOrdered A map of MenuItem to Integer, where the integer is the number of MenuItems sold. The
     *                     ingredients required for each MenuItem will have their inventory quantity decreased by the
     *                     appropriate amount.
     */
    void updateQuantities(Map<MenuItem, Integer> itemsOrdered);
}

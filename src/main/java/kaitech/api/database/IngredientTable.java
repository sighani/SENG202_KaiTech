package kaitech.api.database;

import kaitech.api.model.Ingredient;

import java.util.Map;
import java.util.Set;

/**
 * The IngredientTable interface declares required functionality to store, retrieve, and modify ingredients in the
 * database.
 *
 * @author Julia Harrison
 */
public interface IngredientTable {

    /**
     * Retrieves an ingredient from the cache. If the ingredient does not currently exist in the cache, query the
     * database for the ingredient. If the query is successful, add the ingredient to the cache and return it.
     *
     * @param code The string code of the ingredient to retrieve.
     * @return An Ingredient if the ingredient exists, null if it does not or cannot be retrieved.
     */
    Ingredient getIngredient(String code);

    /**
     * Get all ingredient codes currently in the database.
     *
     * @return A set of ingredient codes.
     */
    Set<String> getAllIngredientCodes();

    /**
     * Saves a given ingredient to the database, and returns a database managed Ingredient which is responsible for
     * the automatic saving of changes on setter calls.
     *
     * @param from The Ingredient to be saved.
     * @return Database managed Ingredient responsible for automatic saving on setter calls.
     */
    Ingredient putIngredient(Ingredient from);

    /**
     * Remove an ingredient from the database and cache.
     *
     * @param code The code of the ingredient to be removed.
     */
    void removeIngredient(String code);

    /**
     * Gets all ingredient objects from the database.
     * Warning: this can be a costly operation when everything isn't already cached, please use with care.
     *
     * @return A map from code to Ingredient.
     */
    Map<String, Ingredient> resolveAllIngredients();
}

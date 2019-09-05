package kaitech.api.database;

import kaitech.api.model.Recipe;

import java.util.Map;
import java.util.Set;

/**
 * The RecipeTable interface declares required functionality to store, retrieve, and modify recipes in the database.
 *
 * @author Julia Harrison
 */
public interface RecipeTable {

    /**
     * Retrieves a recipe from the cache. If the recipe does not currently exist in the cache, query the database for the
     * recipe. If the query is successful, add the recipe to the cache and return it.
     *
     * @param id The integer ID of the recipe to be retrieved.
     * @return A Recipe if the recipe exists, null if it does not.
     */
    Recipe getRecipe(int id);

    /**
     * Get all IDs for recipes currently in the database.
     *
     * @return A set of all integer recipe IDs.
     */
    Set<Integer> getAllIDNo();

    /**
     * Saves a given recipe to the database, and returns a database managed Recipe which is responsible for the automatic
     * saving of changes on setter calls.
     *
     * @param from The Recipe to be saved.
     * @return Database managed Recipe responsible for automatic saving on setter calls.
     */
    Recipe putRecipe(Recipe from);

    /**
     * Remove a recipe from the database and cache.
     *
     * @param id The ID number of the recipe to be removed.
     */
    void removeRecipe(int id);

    /**
     * Gets all recipe objects from the database.
     * Warning: this can be a costly operation when everything isn't already cached, please use with care.
     *
     * @return A map from recipe ID to Recipe.
     */
    Map<Integer, Recipe> resolveAllRecipes();
}

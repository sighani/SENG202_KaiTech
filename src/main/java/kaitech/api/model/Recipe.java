package kaitech.api.model;

import org.joda.money.Money;

import java.util.Map;

/**
 * This interface declares the required functionality for a menu item's recipe. A menu item may or may not have
 * a recipe depending if the user has specified a recipe. The recipe contains information about how to make the
 * MenuItem, such as a list of ingredients and their quantities.
 *
 * @author Julia Harrison
 */
public interface Recipe {
    /**
     * Calculates the total cost of the recipe based on the ingredients and their quantities. Returns
     * the integer amount
     *
     * @return An integer total cost
     */
    Money calculateTotalCost();

    /**
     * Adds an ingredient and the required quantity to the recipe, only if the Ingredient is not already
     * in the recipe.
     *
     * @param i   The Ingredient to add
     * @param amt The int amount of the Ingredient that is required
     * @return A boolean, true if the ingredient was successfully added, false otherwise.
     */
    boolean addIngredient(Ingredient i, int amt);

    /**
     * Changes the amount of the given ingredient to the given amount, only if the ingredient is in the recipe and the
     * new amount is a positive amount
     *
     * @param i   The Ingredient to modify the amount of
     * @param amt The new int amount
     */
    void updateIngredientQuantity(Ingredient i, int amt);

    /**
     * Removes the ingredient from the recipe
     *
     * @param i The ingredient to remove
     */
    void removeIngredient(Ingredient i);

    /**
     * @return A map of Ingredient to its integer quantity required for the recipe.
     */
    Map<Ingredient, Integer> getIngredients();

    /**
     * @return The integer ID of the recipe.
     */
    int getRecipeID();

    /**
     * Sets the ID of the recipe.
     *
     * @param recipeID The new integer ID of the recipe.
     */
    void setRecipeID(int recipeID);

    /**
     * @return The preparation time of the recipe, in integer minutes.
     */
    int getPreparationTime();

    /**
     * @return The cooking time of the recipe, in integer minutes.
     */
    int getCookingTime();

    /**
     * @return The number of servings the recipe will make, as an integer.
     */
    int getNumServings();
}

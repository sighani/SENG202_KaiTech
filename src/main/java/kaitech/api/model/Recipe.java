package kaitech.api.model;

import kaitech.util.ThreeValueLogic;
import org.joda.money.Money;

import java.util.List;
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
     * @return The integer ID of the recipe.
     */
    int getID();

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

    /**
     * @return A ThreeValueLogic value for whether the recipe is vegetarian, based on its ingredients.
     */
    ThreeValueLogic getIsVeg();

    /**
     * @return A ThreeValueLogic value for whether the recipe is vegan, based on its ingredients.
     */
    ThreeValueLogic getIsVegan();

    /**
     * @return A ThreeValueLogic value for whether the recipe is gluten free, based on its ingredients.
     */
    ThreeValueLogic getIsGF();

    /**
     * @return A map of Ingredient to its integer quantity required for the recipe.
     */
    Map<Ingredient, Integer> getIngredients();

    /**
     * @return A List of String ingredient names for ingredients in the recipe.
     */
    List<String> getIngredientNames();

    /**
     * Set the preparation time of the recipe.
     *
     * @param preparationTime The preparation time, as an int.
     */
    void setPreparationTime(int preparationTime);

    /**
     * Set the cooking time of the recipe.
     *
     * @param cookingTime The cooking time, as an int.
     */
    void setCookingTime(int cookingTime);

    /**
     * Set the number of servings the recipe makes.
     *
     * @param numServings The number of servings the recipe makes, as an int.
     */
    void setNumServings(int numServings);

    /**
     * Sets the ingredients and their quantity required for the recipe.
     *
     * @param ingredients A map of Ingredient to Integer quantity.
     */
    void setIngredients(Map<Ingredient, Integer> ingredients);

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
     * new amount is a positive amount.
     *
     * @param i   The Ingredient to modify the amount of
     * @param amt The new int amount
     */
    void updateIngredientAmount(Ingredient i, int amt);

    /**
     * Removes the ingredient from the recipe
     *
     * @param i The ingredient to remove
     */
    void removeIngredient(Ingredient i);

    /**
     * Calculates the total cost of the recipe based on the ingredients and their quantities. Returns
     * the integer amount as a Money object.
     *
     * @return The total cost, as a Money object.
     */
    Money calculateTotalCost();
}

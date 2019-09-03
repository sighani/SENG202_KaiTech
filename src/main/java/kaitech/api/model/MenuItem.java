package kaitech.api.model;

import kaitech.util.MenuItemType;
import org.joda.money.Money;

import java.util.List;

/**
 * An interface to keep track of menu items and their ingredients.
 *
 * @author Julia Harrison
 */
public interface MenuItem {
    /**
     * Adds an ingredient to the list of ingredient names and to the recipe. Forwards the boolean returned by
     * addIngredient in Recipe
     *
     * @param i   The Ingredient to add
     * @param amt The int amount of the Ingredient that is required
     * @return A boolean, true if the ingredient was successfully added, false otherwise.
     */
    boolean addIngredientToRecipe(Ingredient i, int amt);

    void removeIngredientFromRecipe(Ingredient ing);

    /**
     * Returns the comma-separated ingredients of the MenuItem for utility.
     *
     * @return A String listing the ingredients
     */
    String getCSVIngredients();

    /**
     * Checks whether the given Business has enough ingredients to create the MenuItem. Note that if the Business does
     * not have an Ingredient in its map, this is considered to be insufficient ingredients. Returns true if there are
     * sufficient ingredients, false otherwise.
     *
     * @param toCheck The Business to check against
     * @return A boolean, true if sufficient, false otherwise
     */
    boolean checkSufficientIngredients(Business toCheck);

    /**
     * Checks how many servings of the MenuItem the Business can create. Note that if the Business does
     * not have an Ingredient in its map, this is considered to be zero servings possible.
     *
     * @param toCheck The Business to check against
     * @return An int amount of servings possible
     */
    int calculateNumServings(Business toCheck);

    /**
     * Sets the name of the menu item to the given String.
     *
     * @param name The new String name of the menu item.
     */
    void setName(String name);

    /**
     * Sets the ingredients of the menu item.
     *
     * @param ingredients A list of the String names of the ingredients.
     */
    void setIngredients(List<String> ingredients);

    /**
     * Sets the recipe of the menu item.
     *
     * @param recipe A Recipe which the menu item follows.
     */
    void setRecipe(Recipe recipe);

    /**
     * Sets the price of the menu item.
     *
     * @param price The price of the menu item, as a {@link Money} object.
     */
    void setPrice(Money price);

    /**
     * Sets the type of the menu item.
     *
     * @param type The type of the menu item, as a {@link MenuItemType} value.
     */
    void setType(MenuItemType type);

    /**
     * @return The code of the menu item, as a String.
     */
    String getCode();

    /**
     * @return The name of the menu item.
     */
    String getName();

    /**
     * @return A list of all ingredients used in the menu item (ingredient names only).
     */
    List<String> getIngredients();

    /**
     * @return The {@link Recipe} of the menu item.
     */
    Recipe getRecipe();

    /**
     * @return The selling price of the menu item, as a {@link Money} object.
     */
    Money getPrice();

    /**
     * @return The {@link MenuItemType} of the menu item.
     */
    MenuItemType getType();
}

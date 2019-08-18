package kaitech.model;

import java.util.Map;

/**
 * This class implements a menu item's recipe. A menu item may or may not have
 * a recipe depending if the user has specified a recipe. The recipe contains
 * information about how to make the MenuItem, such as a list of ingredients
 * and their quantities.
 */
public class Recipe {
    /**
     * A map specifying what ingredients are needed and their quantities. Maps an Ingredient to
     * a quantity integer.
     */
    private Map<Ingredient, Integer> ingredients;

    /**
     * The time it takes to prepare the MenuItem in minutes.
     */
    private int preparationTime;

    /**
     * The time it takes to cook the MenuItem in minutes.
     */
    private int cookingTime;

    /**
     * How many servings the recipe makes.
     */
    private int numServings;

    public Recipe(Map<Ingredient, Integer> ingredients, int preparationTime, int cookingTime, int numServings) {
        this.ingredients = ingredients;
        this.preparationTime = preparationTime;
        this.cookingTime = cookingTime;
        this.numServings = numServings;
    }

    /**
     * Calculates the total cost of the recipe based on the ingredients and their quantities. Returns
     * the integer amount
     * @return An integer total cost
     */
    public int calculateTotalCost() {
        int total = 0;
        for (Map.Entry<Ingredient, Integer> entry : ingredients.entrySet()) {
            total += entry.getValue() * entry.getKey().getPrice();
        }
        return total;
    }
}

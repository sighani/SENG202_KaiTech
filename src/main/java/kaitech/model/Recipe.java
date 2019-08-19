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

    public Map<Ingredient, Integer> getIngredients() {
        return ingredients;
    }

    /**
     * Adds an ingredient and the required quantity to the recipe, only if the Ingredient is not already
     * in the recipe.
     * @param i The Ingredient to add
     * @param amt The int amount of the Ingredient that is required
     * @return A boolean, true if the ingredient was successfully added, false otherwise.
     */
    public boolean addIngredient(Ingredient i, int amt) {
        if (!ingredients.containsKey(i)) {
            ingredients.put(i, amt);
            return true;
        }
        return false;
    }
}

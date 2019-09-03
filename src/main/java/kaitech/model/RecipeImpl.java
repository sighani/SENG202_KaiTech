package kaitech.model;

import kaitech.api.model.Ingredient;
import kaitech.api.model.Recipe;
import org.joda.money.Money;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class RecipeImpl implements Recipe {
    /**
     * The ID number of the recipe.
     */
    private int recipeID;

    /**
     * A map specifying what ingredients are needed and their quantities in whatever unit is specified in the Ingredient.
     * Maps an Ingredient to a quantity integer.
     */
    private final Map<Ingredient, Integer> ingredients = new HashMap<>();

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

    public RecipeImpl(Map<Ingredient, Integer> ingredients, int preparationTime, int cookingTime, int numServings) {
        this.ingredients.putAll(ingredients);
        this.preparationTime = preparationTime;
        this.cookingTime = cookingTime;
        this.numServings = numServings;
    }

    @Override
    public Money calculateTotalCost() {
        Money total = Money.parse("NZD 0");
        for (Map.Entry<Ingredient, Integer> entry : ingredients.entrySet()) {
            total = total.plus(entry.getKey().getPrice().multipliedBy(entry.getValue()));
        }
        return total;
    }

    @Override
    public boolean addIngredient(Ingredient i, int amt) {
        if (amt <= 0) {
            throw new IllegalArgumentException("Quantity must be a positive number.");
        }
        if (!ingredients.containsKey(i)) {
            ingredients.put(i, amt);
            return true;
        }
        return false;
    }

    @Override
    public void updateIngredientQuantity(Ingredient i, int amt) {
        if (!ingredients.containsKey(i)) {
            throw new IllegalArgumentException("Ingredient to modify is not in the recipe.");
        }
        if (amt <= 0) {
            throw new IllegalArgumentException("New quantity must be a positive number.");
        }
        ingredients.put(i, amt);
    }

    @Override
    public void removeIngredient(Ingredient i) {
        ingredients.remove(i);
    }

    @Override
    public Map<Ingredient, Integer> getIngredients() {
        return Collections.unmodifiableMap(ingredients);
    }

    @Override
    public int getRecipeID() {
        return recipeID;
    }

    @Override
    public void setRecipeID(int recipeID) {
        this.recipeID = recipeID;
    }

    @Override
    public int getPreparationTime() {
        return preparationTime;
    }

    @Override
    public int getCookingTime() {
        return cookingTime;
    }

    @Override
    public int getNumServings() {
        return numServings;
    }
}

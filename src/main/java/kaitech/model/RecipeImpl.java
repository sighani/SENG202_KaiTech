package kaitech.model;

import kaitech.api.model.Ingredient;
import kaitech.api.model.Recipe;
import org.joda.money.Money;

import java.util.*;
import java.util.stream.Collectors;

public class RecipeImpl implements Recipe {
    /**
     * The ID number of the recipe.
     */
    protected int recipeID;

    /**
     * A map specifying what ingredients are needed and their quantities in whatever unit is specified in the Ingredient.
     * Maps an Ingredient to a quantity integer.
     */
    protected final Map<Ingredient, Integer> ingredients = new HashMap<>();

    /**
     * The time it takes to prepare the MenuItem in minutes.
     */
    protected int preparationTime;

    /**
     * The time it takes to cook the MenuItem in minutes.
     */
    protected int cookingTime;

    /**
     * How many servings the recipe makes.
     */
    protected int numServings;

    public RecipeImpl(Map<Ingredient, Integer> ingredients) {
        this.ingredients.putAll(ingredients);
        this.recipeID = -1; // -1 implies the recipe ID has not yet been assigned by the database
    }

    public RecipeImpl(Map<Ingredient, Integer> ingredients, int preparationTime, int cookingTime, int numServings) {
        this.ingredients.putAll(ingredients);
        this.preparationTime = preparationTime;
        this.cookingTime = cookingTime;
        this.numServings = numServings;
        this.recipeID = -1;
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
    public boolean addIngredient(Ingredient ingredient, int amt) {
        if (amt <= 0) {
            throw new IllegalArgumentException("Quantity must be a positive number.");
        }
        if (!ingredients.containsKey(ingredient)) {
            ingredients.put(ingredient, amt);
            return true;
        }
        return false;
    }

    @Override
    public void updateIngredientAmount(Ingredient ingredient, int amt) {
        if (!ingredients.containsKey(ingredient)) {
            throw new IllegalArgumentException("Ingredient to modify is not in the recipe.");
        }
        if (amt <= 0) {
            throw new IllegalArgumentException("New quantity must be a positive number.");
        }
        ingredients.put(ingredient, amt);
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
    public List<String> getIngredientNames() {
        return ingredients.keySet().stream() //
                .map(Ingredient::getDisplayName) //
                .collect(Collectors.toList());
    }

    @Override
    public int getID() {
        return recipeID;
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

    @Override
    public void setPreparationTime(int preparationTime) {
        this.preparationTime = preparationTime;
    }

    @Override
    public void setCookingTime(int cookingTime) {
        this.cookingTime = cookingTime;
    }

    @Override
    public void setNumServings(int numServings) {
        this.numServings = numServings;
    }

    @Override
    public boolean equals(Object obj) {
        if (super.equals(obj)) return true;
        if (!(obj instanceof RecipeImpl)) return false;
        RecipeImpl other = (RecipeImpl) obj;
        return Objects.equals(other.getID(), getID()) //
                && Objects.equals(other.getPreparationTime(), getPreparationTime()) //
                && Objects.equals(other.getCookingTime(), getCookingTime()) //
                && Objects.equals(other.getNumServings(), getNumServings()) //
                && Objects.equals(other.getIngredients(), getIngredients());
    }

    @Override
    public int hashCode() {
        int i = 0;
        i = 31 * i + getID();
        i = 31 * i + getPreparationTime();
        i = 31 * i + getCookingTime();
        i = 31 * i + getNumServings();
        i = 31 * i + (getIngredients() == null ? 0 : getIngredients().hashCode());
        return i;
    }
}

package kaitech.model;

import kaitech.api.model.Ingredient;
import kaitech.api.model.Recipe;
import kaitech.util.ThreeValueLogic;
import org.joda.money.Money;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Implements the {@link Recipe} interface; used to store details about a Recipe.
 */
public class RecipeImpl implements Recipe {
    /**
     * The ID number of the recipe.
     */
    protected final int recipeID;

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

    /**
     * ThreeValueLogic values for whether the whole recipe is vegetarian, vegan, and/or gluten free.
     * Based on the ingredients in the recipe.
     * If the value is null, it has not yet been determined whether the recipe is, isn't, or is unknown.
     */
    protected ThreeValueLogic isVeg = null;
    protected ThreeValueLogic isVegan = null;
    protected ThreeValueLogic isGF = null;

    /**
     * A map specifying what ingredients are needed and their quantities in whatever unit is specified in the Ingredient.
     * Maps an Ingredient to a quantity integer.
     */
    protected final Map<Ingredient, Integer> ingredients = new HashMap<>();

    public RecipeImpl(Map<Ingredient, Integer> ingredients) {
        this.recipeID = -1;
        this.ingredients.putAll(ingredients);
        calculateDietaryInfo();
    }

    public RecipeImpl(int preparationTime, int cookingTime, int numServings, Map<Ingredient, Integer> ingredients) {
        this.recipeID = -1;
        this.preparationTime = preparationTime;
        this.cookingTime = cookingTime;
        this.numServings = numServings;
        this.ingredients.putAll(ingredients);
        calculateDietaryInfo();
    }

    public RecipeImpl(int recipeID, int preparationTime, int cookingTime, int numServings, Map<Ingredient,
            Integer> ingredients) {
        this.recipeID = recipeID;
        this.preparationTime = preparationTime;
        this.cookingTime = cookingTime;
        this.numServings = numServings;
        this.ingredients.putAll(ingredients);
        calculateDietaryInfo();
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
    public ThreeValueLogic getIsVeg() {
        if (isVeg == null) {
            calculateIsVeg();
        }
        return isVeg;
    }

    @Override
    public ThreeValueLogic getIsVegan() {
        if (isVegan == null) {
            calculateIsVegan();
        }
        return isVegan;
    }

    @Override
    public ThreeValueLogic getIsGF() {
        if (isGF == null) {
            calculateIsGF();
        }
        return isGF;
    }

    @Override
    public Map<Ingredient, Integer> getIngredients() {
        //Unmodifiable so database can easily track changes.
        return Collections.unmodifiableMap(ingredients);
    }

    @Override
    public List<String> getIngredientNames() {
        return ingredients.keySet().stream() //
                .map(Ingredient::getDisplayName) //
                .collect(Collectors.toList());
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
    public void setIngredients(Map<Ingredient, Integer> ingredients) {
        this.ingredients.clear();
        this.ingredients.putAll(ingredients);
        calculateDietaryInfo();
    }

    /**
     * Determine whether the recipe is vegetarian, based on its ingredients.
     */
    private void calculateIsVeg() {
        List<ThreeValueLogic> values = ingredients.keySet().stream() // List since Set does not allow null values
                .map(Ingredient::getIsVeg) //
                .collect(Collectors.toList());
        if (values.contains(ThreeValueLogic.NO)) {
            isVeg = ThreeValueLogic.NO;
        } else if (values.contains(ThreeValueLogic.UNKNOWN) || values.contains(null)) {
            isVeg = ThreeValueLogic.UNKNOWN;
        } else {
            isVeg = ThreeValueLogic.YES;
        }
    }

    /**
     * Determine whether the recipe is vegan, based on its ingredients.
     */
    private void calculateIsVegan() {
        List<ThreeValueLogic> values = ingredients.keySet().stream() //
                .map(Ingredient::getIsVegan) //
                .collect(Collectors.toList());
        if (values.contains(ThreeValueLogic.NO)) {
            isVegan = ThreeValueLogic.NO;
        } else if (values.contains(ThreeValueLogic.UNKNOWN) || values.contains(null)) {
            isVegan = ThreeValueLogic.UNKNOWN;
        } else {
            isVegan = ThreeValueLogic.YES;
        }
    }

    /**
     * Determine whether the recipe is gluten free, based on its ingredients.
     */
    private void calculateIsGF() {
        List<ThreeValueLogic> values = ingredients.keySet().stream() //
                .map(Ingredient::getIsGF) //
                .collect(Collectors.toList());
        if (values.contains(ThreeValueLogic.NO)) {
            isGF = ThreeValueLogic.NO;
        } else if (values.contains(ThreeValueLogic.UNKNOWN) || values.contains(null)) {
            isGF = ThreeValueLogic.UNKNOWN;
        } else {
            isGF = ThreeValueLogic.YES;
        }
    }

    /**
     * Calculate the dietary information (vegetarian, vegan, gluten free) for the recipe.
     */
    private void calculateDietaryInfo() {
        calculateIsVeg();
        calculateIsVegan();
        calculateIsGF();
    }

    @Override
    public boolean addIngredient(Ingredient ingredient, int amt) {
        if (amt <= 0) {
            throw new IllegalArgumentException("Quantity must be a positive number.");
        }
        if (!ingredients.containsKey(ingredient)) {
            ingredients.put(ingredient, amt);
            calculateDietaryInfo();
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
        calculateDietaryInfo();
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

package kaitech.model;

import kaitech.api.model.*;
import kaitech.util.MenuItemType;
import org.joda.money.Money;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A class to keep track of menu items and their ingredients.
 */
public class MenuItemImpl implements MenuItem {
    /**
     * The unique code of the menuItem
     */
    private String code;

    /**
     * The name of the menuItem
     */
    private String name;

    /**
     * A simple list of the names of the ingredients.
     */
    private List<String> ingredients;

    /**
     * The recipe of the MenuItem.
     */
    private Recipe recipe;

    /**
     * The selling price of the MenuItem.
     */
    private Money price;

    /**
     * The category that this MenuItem falls under
     */
    private MenuItemType type;

    public MenuItemImpl(String code, String name, List<String> ingredients, Recipe recipe, Money price) {
        this.code = code;
        this.name = name;
        this.ingredients = ingredients;
        this.recipe = recipe;
        this.price = price;
        type = MenuItemType.MISC;
    }

    public MenuItemImpl(String code, String name, List<String> ingredients, Recipe recipe, Money price, MenuItemType type) {
        this.code = code;
        this.name = name;
        this.ingredients = ingredients;
        this.recipe = recipe;
        this.price = price;
        this.type = type;
    }

    /**
     * Adds an ingredient to the list of ingredient names and to the recipe. Forwards the boolean returned by
     * addIngredient in Recipe
     *
     * @param i   The Ingredient to add
     * @param amt The int amount of the Ingredient that is required
     * @return A boolean, true if the ingredient was successfully added, false otherwise.
     */
    @Override
    public boolean addIngredientToRecipe(Ingredient i, int amt) {
        ingredients.add(i.name());
        return recipe.addIngredient(i, amt);
    }

    @Override
    public List<String> ingredientNames() {
        return ingredients;
    }

    /**
     * Prints the comma-separated ingredients of the MenuItem for utility.
     *
     * @return A String listing the ingredients
     */
    @Override
    public String ingredients() {
        StringBuilder recipeText;
        recipeText = new StringBuilder("[" + code + "(" + name + "):");
        int i = 0;
        for (String s : ingredients) {
            i++;
            recipeText.append(" ").append(s);
            if (i < ingredients.size()) {
                recipeText.append(",");
            }
        }
        recipeText.append("]");
        return recipeText.toString();
    }

    /**
     * Checks whether the given Business has enough ingredients to create the MenuItem. Note that if the Business does
     * not have an Ingredient in its map, this is considered to be insufficient ingredients. Returns true if there are
     * sufficient ingredients, false otherwise.
     *
     * @param toCheck The Business to check against
     * @return A boolean, true if sufficient, false otherwise
     */
    @Override
    public boolean checkSufficientIngredients(Business toCheck) {
        boolean result = true;
        HashMap<Ingredient, Integer> inventory = toCheck.getIngredients();
        for (Map.Entry<Ingredient, Integer> entry : recipe.getIngredients().entrySet()) {
            if (!toCheck.getIngredients().containsKey(entry.getKey()) || inventory.get(entry.getKey()) < entry.getValue()) {
                result = false;
                break;
            }
        }
        return result;
    }

    /**
     * Checks how many servings of the MenuItem the Business can create. Note that if the Business does
     * not have an Ingredient in its map, this is considered to be zero servings possible.
     *
     * @param toCheck The Business to check against
     * @return An int amount of servings possible
     */
    @Override
    public int calculateNumServings(Business toCheck) {
        int result = 0;
        boolean first = true;
        for (Map.Entry<Ingredient, Integer> entry : recipe.getIngredients().entrySet()) {
            if (!toCheck.getIngredients().containsKey(entry.getKey())) {
                return 0;
            }
            int candidate = toCheck.getIngredients().get(entry.getKey()) / entry.getValue();
            if (candidate < result || first) {
                result = candidate;
                first = false;
            }
        }
        return result;
    }

    @Override
    public Recipe getRecipe() {
        return recipe;
    }

    @Override
    public Money getPrice() {
        return price;
    }

    @Override
    public String toString() {
        return name;
    }

    /**
     * Overrides equals such that two MenuItems are equivalent if they have the same code. Returns true if they are
     * equal, false otherwise
     *
     * @param other The Object to compare to, which must be casted to a MenuItem
     * @return A boolean, true if they are equal, false otherwise
     */
    @Override
    public boolean equals(Object other) {
        if (super.equals(other)) {
            return true;
        }
        if (!(other instanceof Menu)) {
            return false;
        }
        MenuItemImpl otherItem = (MenuItemImpl) other;
        return this.code.equals(otherItem.code);
    }

    @Override
    public List<String> getIngredients() {
        return ingredients;
    }

    @Override
    public void setIngredients(List<String> ingredients) {
        this.ingredients = ingredients;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public MenuItemType getType() {
        return type;
    }

    @Override
    public String getCode() {
        return code;
    }
}
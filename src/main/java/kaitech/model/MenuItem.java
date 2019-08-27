package kaitech.model;

import kaitech.util.MenuItemType;
import org.joda.money.Money;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A class to keep track of menu items and their ingredients. Only a sample --
 * needs lots more detail -- such as some methods :-)
 */
public class MenuItem {
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

    public MenuItem(String code, String name, List<String> ingredients, Recipe recipe, Money price) {
        this.code = code;
        this.name = name;
        this.ingredients = ingredients;
        this.recipe = recipe;
        this.price = price;
        type = MenuItemType.MISC;
    }

    public MenuItem(String code, String name, List<String> ingredients, Recipe recipe, Money price, MenuItemType type) {
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
     * @param i The Ingredient to add
     * @param amt The int amount of the Ingredient that is required
     * @return A boolean, true if the ingredient was successfully added, false otherwise.
     */
    public boolean addIngredientToRecipe(Ingredient i, int amt) {
        ingredients.add(i.name());
        return recipe.addIngredient(i, amt);
    }

    public List<String> ingredientNames() {
        return ingredients;
    }

    /**
     * Prints the comma-separated ingredients of the MenuItem for utility.
     * @return A String listing the ingredients
     */
    public String ingredients() {
        String recipeText;
        recipeText = "[" + code + "(" + name + "):";
        int i = 0;
        for(String s:ingredients) {
            i++;
            recipeText += " " + s;
            if (i < ingredients.size()) {
                recipeText += ",";
            }
        }
        recipeText += "]";
        return recipeText;
    }

    /**
     * Checks whether the given Business has enough ingredients to create the MenuItem. Note that if the Business does
     * not have an Ingredient in its map, this is considered to be insufficient ingredients. Returns true if there are
     * sufficient ingredients, false otherwise.
     * @param toCheck The Business to check against
     * @return A boolean, true if sufficient, false otherwise
     */
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
     * @param toCheck The Business to check against
     * @return An int amount of servings possible
     */
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

    public Recipe getRecipe() {
        return recipe;
    }

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
     * @param other The Object to compare to, which must be casted to a MenuItem
     * @return A boolean, true if they are equal, false otherwise
     */
    @Override
    public boolean equals(Object other) {
        MenuItem otherItem;
        otherItem = (MenuItem) other;
        return this.code == otherItem.code;
    }
}
package kaitech.model;

import kaitech.api.model.*;
import kaitech.util.MenuItemType;
import org.joda.money.Money;

import java.util.List;
import java.util.Map;

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

    @Override
    public boolean addIngredientToRecipe(Ingredient i, int amt) {
        ingredients.add(i.getName());
        return recipe.addIngredient(i, amt);
    }

    @Override
    public String getCSVIngredients() {
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

    @Override
    public boolean checkSufficientIngredients(Business toCheck) {
        boolean result = true;
        Map<Ingredient, Integer> inventory = toCheck.getInventory();
        for (Map.Entry<Ingredient, Integer> entry : recipe.getIngredients().entrySet()) {
            if (!toCheck.getInventory().containsKey(entry.getKey()) || inventory.get(entry.getKey()) < entry.getValue()) {
                result = false;
                break;
            }
        }
        return result;
    }

    @Override
    public int calculateNumServings(Business toCheck) {
        int result = 0;
        boolean first = true;
        for (Map.Entry<Ingredient, Integer> entry : recipe.getIngredients().entrySet()) {
            if (!toCheck.getInventory().containsKey(entry.getKey())) {
                return 0;
            }
            int candidate = toCheck.getInventory().get(entry.getKey()) / entry.getValue();
            if (candidate < result || first) {
                result = candidate;
                first = false;
            }
        }
        return result;
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

    @Override
    public Recipe getRecipe() {
        return recipe;
    }

    @Override
    public Money getPrice() {
        return price;
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
    public String toString() {
        return name;
    }
}
package kaitech.model;

import kaitech.api.model.Business;
import kaitech.api.model.Ingredient;
import kaitech.api.model.MenuItem;
import kaitech.api.model.Recipe;
import kaitech.util.MenuItemType;
import org.joda.money.Money;

import java.util.*;

public class MenuItemImpl implements MenuItem {
    /**
     * The unique code of the menuItem
     */
    protected String code;

    /**
     * The name of the menuItem
     */
    protected String name;


    /**
     * The recipe of the MenuItem.
     */
    protected Recipe recipe;

    /**
     * The selling price of the MenuItem.
     */
    protected Money price;

    /**
     * The category that this MenuItem falls under
     */
    protected MenuItemType type;

    /**
     * A simple list of the names of the ingredients.
     */
    protected final List<String> ingredients = new ArrayList<>();

    public MenuItemImpl(String code, Recipe recipe, Money price) {
        this.code = code;
        this.recipe = recipe;
        this.price = price;
        this.type = MenuItemType.MISC;
        if (recipe != null) {
            this.ingredients.addAll(recipe.getIngredientNames());
        }
    }

    public MenuItemImpl(String code, String name, Recipe recipe, Money price, List<String> ingredients) {
        this.code = code;
        this.name = name;
        this.recipe = recipe;
        this.price = price;
        type = MenuItemType.MISC;
        this.ingredients.addAll(ingredients);
    }

    public MenuItemImpl(String code, String name, Money price, Recipe recipe, MenuItemType type, List<String> ingredients) {
        this.code = code;
        this.name = name;
        this.recipe = recipe;
        this.price = price;
        this.type = type;
        this.ingredients.addAll(ingredients);
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getName() {
        return name;
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
    public MenuItemType getType() {
        return type;
    }

    @Override
    public List<String> getIngredients() {
        //Unmodifiable so database can easily track changes.
        return Collections.unmodifiableList(ingredients);
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }

    @Override
    public void setPrice(Money price) {
        this.price = price;
    }

    @Override
    public void setType(MenuItemType type) {
        this.type = type;
    }

    @Override
    public void setIngredients(List<String> ingredients) {
        this.ingredients.clear();
        this.ingredients.addAll(ingredients);
    }

    @Override
    public void addIngredientToRecipe(Ingredient i, int amt) {
        ingredients.add(i.getName());
        recipe.addIngredient(i, amt);
    }

    @Override
    public void removeIngredientFromRecipe(Ingredient ing) {
        ingredients.remove(ing.getName());
        recipe.removeIngredient(ing);
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

    /**
     * Overrides the default equals.
     *
     * @param obj The Object to compare to, which must be cast to a MenuItem
     * @return A boolean, true if they are equal, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (super.equals(obj)) return true;
        if (!(obj instanceof MenuItemImpl)) return false;
        MenuItemImpl other = (MenuItemImpl) obj;
        return Objects.equals(other.getCode(), getCode()) //
                && Objects.equals(other.getName(), getName()) //
                && Objects.equals(other.getIngredients(), getIngredients()) //
                && Objects.equals(other.getRecipe(), getRecipe()) //
                && Objects.equals(other.getPrice(), getPrice()) //
                && Objects.equals(other.getType(), getType());
    }

    @Override
    public int hashCode() {
        int i = 0;
        i = 31 * i + (getCode() == null ? 0 : getCode().hashCode());
        i = 31 * i + (getName() == null ? 0 : getName().hashCode());
        i = 31 * i + (getIngredients() == null ? 0 : getIngredients().hashCode());
        i = 31 * i + (getRecipe() == null ? 0 : getRecipe().hashCode());
        i = 31 * i + (getPrice() == null ? 0 : getPrice().hashCode());
        i = 31 * i + (getType() == null ? 0 : getType().hashCode());
        return i;
    }

    @Override
    public String toString() {
        return name;
    }
}
package kaitech.model;

import java.util.List;

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
     * Constructor for class.
     */
    public MenuItem(String code, String name, List<String> ingredients) {
        this.code = code;
        this.name = name;
        this.ingredients = ingredients;
    }

    public void addIngredient(String it) {
        ingredients.add(it);
    }

    public List<String> ingredientNames() {
        return ingredients;
    }

    public String ingredients() {
        String recipeText;
        recipeText = "[" + code + "(" + name + "):";
        for(String s:ingredients) {
            recipeText += " " + s;
        }
        recipeText += "]";
        return recipeText;
    }
}
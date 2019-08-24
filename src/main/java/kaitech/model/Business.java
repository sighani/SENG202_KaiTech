package kaitech.model;

import java.util.*;

/**
 * Main class for the business. Keeps track of the model classes (suppliers
 * etc.) that we have as well as performing major functions.
 */
public class Business implements Observer {
    /**
     * A list of suppliers that trade with the Business.
     */
    List<Supplier> suppliers;

    /**
     * A map from each Ingredient the business uses and their quantities as integers
     * owned by the business.
     */
    private HashMap<Ingredient, Integer> inventory;

    /**
     * A list of all sales the business has made. A temporary solution, as this list
     * can become very large.
     */
    private List<Sale> salesRecords;

    /**
     * A list of all menus that the business offers.
     */
    private List<Menu> menus;

    public Business() {
        suppliers = new ArrayList<Supplier>();
        inventory = new HashMap<Ingredient, Integer>();
        salesRecords = new ArrayList<Sale>();
        menus = new ArrayList<Menu>();
    }

    public void setSuppliers(List<Supplier> s) {
        suppliers = s;
    }

    /**
     * Adds a specified supplier from the list
     * @param s The Supplier to add
     */
    public void addSupplier(Supplier s) {
        suppliers.add(s);
    }

    /**
     * Removes a specified supplier from the list
     * @param s The Supplier to remove
     */
    public void removeSupplier(Supplier s) {
        suppliers.remove(s);
    }

    /**
     * Increases the quantity of a given ingredient by a given amount. Returns True if the ingredient
     * is in the ingredients HashMap, False otherwise.
     * @param i The ingredient to update
     * @param amt The amount to increase by
     * @return A boolean depending on whether the ingredient is in the map
     */
    public boolean increaseIngredientQuantity(Ingredient i, int amt) {
        if (amt <= 0) {
            throw new IllegalArgumentException("Amount to increase by must be a positive number.");
        }
        if (inventory.containsKey(i)) {
            inventory.put(i, inventory.get(i) + amt);
            return true;
        }
        return false;
    }

    /**
     * Decreases the quantity of a given ingredient by a given amount. Returns True if the ingredient
     * is in the ingredients HashMap, False otherwise.
     * @param i The Ingredient to update
     * @param amt The int amount to decrease by
     * @return A boolean depending on whether the ingredient is in the map
     */
    public boolean decreaseIngredientQuantity(Ingredient i, int amt) {
        if (amt <= 0) {
            throw new IllegalArgumentException("Amount to decrease by must be a positive number.");
        }
        if (inventory.containsKey(i)) {
            if (inventory.get(i) - amt < 0) {
                throw new IllegalArgumentException("Cannot decrease by an amount greater than what the Business owns.");
            }
            inventory.put(i, inventory.get(i) - amt);
            return true;
        }
        return false;
    }

    public List<Supplier> getSuppliers() {
        return suppliers;
    }

    /**
     * Adds a new ingredient with a quantity of zero. Returns true if the ingredient is not already in
     * the map, false otherwise.
     * @param i The new ingredient to add
     * @return A boolean depending on whether the ingredient is in the map
     */
    public boolean addIngredient(Ingredient i) {
        if (!inventory.containsKey(i)) {
            inventory.put(i, 0);
            return true;
        }
        return false;
    }

    /**
     * Adds a new ingredient with a quantity of of the specified amount. Returns true if the ingredient is not already
     * in the map, false otherwise.
     * @param i The new Ingredient to add
     * @param amt The int initial amount
     * @return A boolean depending on whether the ingredient is in the map
     */
    public boolean addIngredient(Ingredient i, int amt) {
        if (amt < 0) {
            throw new IllegalArgumentException("Amount must be a positive number.");
        }
        if (!inventory.containsKey(i)) {
            inventory.put(i, amt);
            return true;
        }
        return false;
    }

    public HashMap<Ingredient, Integer> getIngredients() {
        return inventory;
    }

    public void setIngredients(HashMap<Ingredient, Integer> ingredients) {
        this.inventory = ingredients;
    }

    /**
     * The update method, called whenever a Sale object is constructed. Takes the Sale object and a map of the MenuItems
     * that were ordered and the quantities of each as parameters. Updates the inventory of the Business as necessary.
     * Note that it is not the job of update to check that there are sufficient ingredients to make the order, it is
     * the job of methods in MenuItem to achieve this.
     * @param sale The Sale object that triggered the update
     * @param map The Object that is the map of MenuItems to amount, which must be casted first
     */
    public void update(Observable sale, Object map) {
        HashMap<MenuItem, Integer> itemsOrdered;
        itemsOrdered = (HashMap<MenuItem, Integer>) map;
        for (Map.Entry<MenuItem, Integer> entry : itemsOrdered.entrySet()) {
            for (int i = 0; i < entry.getValue(); i++) {
                for (Map.Entry<Ingredient, Integer> entry2 : entry.getKey().getRecipe().getIngredients().entrySet()) {
                    inventory.put(entry2.getKey(), (inventory.get(entry2.getKey()) - entry2.getValue()));
                }
            }
        }
    }
}
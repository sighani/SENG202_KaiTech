package kaitech.model;

import kaitech.api.model.*;

import java.util.*;

/**
 * Main class for the business. Keeps track of the model classes (suppliers
 * etc.) that we have as well as performing major functions.
 */
public class BusinessImpl implements Business {
    /**
     * A list of suppliers that trade with the Business.
     */
    List<Supplier> suppliers;

    /**
     * A list of all the MenuItems that Business serves.
     */
    List<MenuItem> menuItems;

    /**
     * A map from each Ingredient the business uses and their quantities as integers in whatever unit is specified in
     * the Ingredient
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

    /**
     * The pin for the business that must be entered to access restricted actions.
     */
    private String pin;

    /**
     * Whether or not the user has entered their pin.
     */
    private boolean loggedIn = false;

    public BusinessImpl() {
        suppliers = new ArrayList<Supplier>();
        inventory = new HashMap<Ingredient, Integer>();
        salesRecords = new ArrayList<Sale>();
        menus = new ArrayList<Menu>();
    }

    @Override
    public void setSuppliers(List<Supplier> s) {
        suppliers = s;
    }

    /**
     * Adds a specified supplier from the list
     *
     * @param s The Supplier to add
     */
    @Override
    public void addSupplier(Supplier s) {
        suppliers.add(s);
    }

    /**
     * Removes a specified supplier from the list
     *
     * @param s The Supplier to remove
     */
    @Override
    public void removeSupplier(Supplier s) {
        suppliers.remove(s);
    }

    /**
     * Increases the quantity of a given ingredient by a given amount. Returns True if the ingredient
     * is in the ingredients HashMap, False otherwise.
     *
     * @param i   The ingredient to update
     * @param amt The amount to increase by
     * @return A boolean depending on whether the ingredient is in the map
     */
    @Override
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
     *
     * @param i   The Ingredient to update
     * @param amt The int amount to decrease by
     * @return A boolean depending on whether the ingredient is in the map
     */
    @Override
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

    @Override
    public List<Supplier> getSuppliers() {
        return suppliers;
    }

    /**
     * Adds a new ingredient with a quantity of zero. Returns true if the ingredient is not already in
     * the map, false otherwise.
     *
     * @param i The new ingredient to add
     * @return A boolean depending on whether the ingredient is in the map
     */
    @Override
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
     *
     * @param i   The new Ingredient to add
     * @param amt The int initial amount
     * @return A boolean depending on whether the ingredient is in the map
     */
    @Override
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

    @Override
    public HashMap<Ingredient, Integer> getIngredients() {
        return inventory;
    }

    @Override
    public void setIngredients(HashMap<Ingredient, Integer> ingredients) {
        this.inventory = ingredients;
    }

    /**
     * The update method, called whenever a Sale object is constructed. Takes the Sale object and a map of the MenuItems
     * that were ordered and the quantities of each as parameters. Updates the inventory of the Business as necessary.
     * Note that it is not the job of update to check that there are sufficient ingredients to make the order, it is
     * the job of methods in MenuItem to achieve this.
     *
     * @param sale The Sale object that triggered the update
     * @param map  The Object that is the map of MenuItems to amount, which must be cast first
     */
    @Override
    public void update(Observable sale, Object map) {
        HashMap<MenuItem, Integer> itemsOrdered;
        Sale saleRecord;
        itemsOrdered = (HashMap<MenuItem, Integer>) map;
        saleRecord = (Sale) sale;
        for (Map.Entry<MenuItem, Integer> entry : itemsOrdered.entrySet()) {
            for (int i = 0; i < entry.getValue(); i++) {
                for (Map.Entry<Ingredient, Integer> entry2 : entry.getKey().getRecipe().getIngredients().entrySet()) {
                    inventory.put(entry2.getKey(), (inventory.get(entry2.getKey()) - entry2.getValue()));
                }
            }
        }
        salesRecords.add(saleRecord);
    }

    /**
     * Sets the Business' pin to a new value, which must be 4 in length and contain digits only.
     *
     * @param pin The new String pin
     * @throws IllegalArgumentException If the pin contains non-numeric values or is shorter or longer than 4
     */
    @Override
    public void setPin(String pin) throws IllegalArgumentException {
        if (!pin.matches("[0-9]+")) {
            throw new IllegalArgumentException("The pin should contain digits only.");
        }
        if (pin.length() != 4) {
            throw new IllegalArgumentException("The pin should contain 4 digits only.");
        }
        this.pin = pin;
    }

    /**
     * Logs the user in given that the pin is correct.
     *
     * @param attempt The user's entered int for the pin
     * @return A boolean, true is the user is now logged in, false otherwise
     * @throws IllegalStateException If the user is already logged in or the pin is unset.
     */
    @Override
    public boolean logIn(String attempt) throws IllegalStateException {
        if (loggedIn) {
            throw new IllegalStateException("The user is already logged in.");
        }
        if (pin == null) {
            throw new IllegalStateException("A pin has not been set yet.");
        }
        if (attempt.equals(pin)) {
            loggedIn = true;
        }
        return loggedIn;
    }

    /**
     * A method that logs the user out. This is done instead of a basic setter to increase security, such that calls
     * like "business.setLoggedIn = true" are not possible.
     */
    @Override
    public void logOut() {
        loggedIn = false;
    }

    /*
    Note that this is done for testing purposes. This getter should not be used anywhere else for security purposes.
     */
    @Override
    public String getPin() {
        return pin;
    }

    @Override
    public boolean isLoggedIn() {
        return loggedIn;
    }
}
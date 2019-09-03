package kaitech.model;

import kaitech.api.model.*;

import java.util.*;

/**
 * Implementation of the {@link Business} interface.
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
    private Map<Ingredient, Integer> inventory;

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
     * The single BusinessImpl object in the system, following the Singleton approach. Used by controllers to get
     * access to BusinessImpl attributes and methods.
     */
    private static BusinessImpl business = null;

    /**
     * Whether or not the user has entered their pin.
     */
    private boolean loggedIn = false;

    /**
     * A map of code to ingredient for all ingredients known to the business.
     */
    private Map<String, Ingredient> ingredients;

    private BusinessImpl() {
        suppliers = new ArrayList<>();
        inventory = new HashMap<>();
        salesRecords = new ArrayList<>();
        menus = new ArrayList<>();
    }

    @Override
    public void addSupplier(Supplier s) {
        suppliers.add(s);
    }

    @Override
    public void removeSupplier(Supplier s) {
        suppliers.remove(s);
    }

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
    public boolean addIngredient(Ingredient i) {
        if (!inventory.containsKey(i)) {
            inventory.put(i, 0);
            return true;
        }
        return false;
    }

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

    @Override
    public void logOut() {
        loggedIn = false;
    }

    @Override
    public boolean isLoggedIn() {
        return loggedIn;
    }

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

    /*
    Note that this is done for testing purposes. This getter should not be used anywhere else for security purposes.
     */
    @Override
    public String getPin() {
        return pin;
    }

    @Override
    public Map<String, Ingredient> getIngredients() {
        return ingredients;
    }

    /**
     * A method to obtain the main BusinessImpl object to implement the Singleton
     * design pattern. If business is null, a new BusinessImpl is created. Returns
     * the business, mainly for use by controllers.
     * @return The Business object
     */
    public static Business getInstance() {
        if (business == null) {
            business = new BusinessImpl();
        }
        return business;
    }

    /**
     * A method to set the business back to null, mainly for testing purposes.
     * Should be used with caution.
     */
    public static void reset() {
        business = null;
    }

    /**
     * A getter to determine if a pin has been set yet, useful for the SetPinController
     * @return A boolean, true if the pin is null, false otherwise
     */
    public boolean getPinIsNull() {
        return pin == null;
    }

    @Override
    public Map<Ingredient, Integer> getInventory() {
        return inventory;
    }

    @Override
    public List<Supplier> getSuppliers() {
        return suppliers;
    }

    @Override
    public void setIngredients(Map<String, Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    @Override
    public void setInventory(Map<Ingredient, Integer> ingredients) {
        this.inventory = ingredients;
    }

    @Override
    public void setSuppliers(List<Supplier> s) {
        suppliers = s;
    }
}
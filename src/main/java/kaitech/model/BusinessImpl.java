package kaitech.model;

import kaitech.api.database.*;
import kaitech.api.model.Business;
import kaitech.api.model.Ingredient;
import kaitech.api.model.MenuItem;
import kaitech.database.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Implementation of the {@link Business} interface; used to maintain state.
 */
public class BusinessImpl implements Business {

    /**
     * The single Business object in the system, following the Singleton approach. Used by controllers to get
     * access to Business attributes and methods.
     */
    private static Business business = null;

    /**
     * Whether or not the user has entered their pin.
     */
    private boolean loggedIn = false;

    private SupplierTable supplierTable;

    private IngredientTable ingredientTable;

    private InventoryTable inventoryTable;

    private RecipeTable recipeTable;

    private MenuItemTable menuItemTable;

    private MenuTable menuTable;

    private PinTable pinTable;

    private SaleTable saleTable;

    private DatabaseHandler databaseHandler;

    private LoyaltyCardTable loyaltyCardTable;

    private LoyaltyCardSettingsTable loyaltyCardSettingsTable;

    private BusinessImpl() {
        File dbFile = new File("kaitech.db");
        initSQLiteDatabase(dbFile);
    }

    /**
     * Constructor for the BusinessImpl class, given a specific File for use by the database.
     *
     * @param dbFile A File for use by the database.
     */
    private BusinessImpl(File dbFile) {
        initSQLiteDatabase(dbFile);
    }

    @Override
    public SupplierTable getSupplierTable() {
        return supplierTable;
    }

    /**
     * Permanently clears the database. Should only be used for testing purposes.
     */
    private void nukeDatabase() {
        if (databaseHandler != null) {
            databaseHandler.dropAllTables();
            initSQLiteDatabase(databaseHandler.getDbFile());
        }
    }

    @Override
    public IngredientTable getIngredientTable() {
        return ingredientTable;
    }

    @Override
    public LoyaltyCardTable getLoyaltyCardTable() {
        return loyaltyCardTable;
    }

    @Override
    public InventoryTable getInventoryTable() {
        return inventoryTable;
    }

    @Override
    public RecipeTable getRecipeTable() {
        return recipeTable;
    }

    @Override
    public MenuItemTable getMenuItemTable() {
        return menuItemTable;
    }

    @Override
    public LoyaltyCardSettingsTable getLoyaltyCardSettingsTable() {
        return loyaltyCardSettingsTable;
    }

    @Override
    public MenuTable getMenuTable() {
        return menuTable;
    }

    @Override
    public SaleTable getSaleTable() {
        return saleTable;
    }

    @Override
    public PinTable getPinTable() {
        return pinTable;
    }

    @Override
    public boolean logIn(String name, CharSequence attempt) throws IllegalStateException {
        if (loggedIn) {
            throw new IllegalStateException("The user is already logged in.");
        }
        if (getIsPinEmpty(name)) {
            throw new IllegalStateException(String.format("A pin for user %s has not been set yet.", name));
        }
        String salt = pinTable.getSalt(name);
        String hash = pinTable.hashPin(attempt, salt);
        if (hash.equals(pinTable.getHashedPin(name))) {
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
    public void setPin(String name, CharSequence pin) throws IllegalArgumentException {
        if (!Pattern.matches("[0-9]+", pin)) {
            throw new IllegalArgumentException("The pin should contain digits only.");
        }
        if (pin.length() != 4) {
            throw new IllegalArgumentException("The pin should contain 4 digits only.");
        }
        String salt = pinTable.generateSalt(64); // Generate 64 character salt
        if (pinTable.getAllNames().contains(name)) {
            pinTable.updatePin(name, pinTable.hashPin(pin, salt));
            pinTable.updateSalt(name, salt);
        } else {
            pinTable.putPin(name, pinTable.hashPin(pin, salt), salt);
        }
    }

    /**
     * A method to obtain the main BusinessImpl object to implement the Singleton
     * design pattern. If business is null, a new BusinessImpl is created. Returns
     * the business, mainly for use by controllers.
     *
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
        if (business != null) {
            ((BusinessImpl) business).nukeDatabase();
        }
        business = null;
    }

    /**
     * A getter to determine if a pin has been set yet, useful for the SetPinController
     *
     * @return A boolean, true if the pin is null, false otherwise
     */
    @Override
    public boolean getIsPinEmpty(String name) {
        return !pinTable.getAllNames().contains(name);
    }

    /**
     * Initializes the SQLite database for persistent data storage.
     *
     * @param dbFile The file for the database to use.
     */
    private void initSQLiteDatabase(File dbFile) {
        databaseHandler = new DatabaseHandler(dbFile);
        databaseHandler.setup();
        pinTable = new PinTblImpl(databaseHandler);
        supplierTable = new SupplierTblImpl(databaseHandler);
        ingredientTable = new IngredientTblImpl(databaseHandler, supplierTable);
        inventoryTable = new InventoryTblImpl(databaseHandler, ingredientTable);
        recipeTable = new RecipeTblImpl(databaseHandler, ingredientTable);
        menuItemTable = new MenuItemTblImpl(databaseHandler, recipeTable, ingredientTable);
        menuTable = new MenuTblImpl(databaseHandler, menuItemTable);
        saleTable = new SaleTblImpl(databaseHandler, menuItemTable, inventoryTable);
        loyaltyCardTable = new LoyaltyCardTblImpl(databaseHandler);
        loyaltyCardSettingsTable = new LoyaltyCardSettingsTblImpl(databaseHandler);
    }

    @Override
    public List<MenuItem> getAffectedMenuItems(Ingredient ingredient) {
        List<MenuItem> affectedItems = new ArrayList<>();
        for (MenuItem item : business.getMenuItemTable().resolveAllMenuItems().values()) {
            if (item.getRecipe() != null && item.getRecipe().getIngredients().containsKey(ingredient)) {
                affectedItems.add(item);
            }
        }
        return affectedItems;
    }

    /**
     * FOR TESTING PURPOSES ONLY!
     * Creates a new BusinessImpl object given a temporary file for the database.
     *
     * @param tempFile A temporary file for use by the database.
     * @return A new BusinessImpl object
     */
    public static Business createTestBusiness(File tempFile) {
        business = new BusinessImpl(tempFile);
        return business;
    }
}
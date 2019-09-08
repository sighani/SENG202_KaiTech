package kaitech.model;

import kaitech.api.database.*;
import kaitech.api.model.Business;
import kaitech.database.*;

import java.io.File;

/**
 * Implementation of the {@link Business} interface.
 */
public class BusinessImpl implements Business {

    /**
     * The pin for the business that must be entered to access restricted actions.
     */
    private String pin;

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

    private SaleTable saleTable;

    private DatabaseHandler databaseHandler;

    private BusinessImpl() {
        File dbFile = new File("kaitech.db");
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
    public MenuTable getMenuTable() {
        return menuTable;
    }

    @Override
    public SaleTable getSaleTable() {
        return saleTable;
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
    public boolean getPinIsNull() {
        return pin == null;
    }


    private void initSQLiteDatabase(File dbFile) {
        databaseHandler = new DatabaseHandler(dbFile);
        databaseHandler.setup();
        supplierTable = new SupplierTblImpl(databaseHandler);
        ingredientTable = new IngredientTblImpl(databaseHandler, supplierTable);
        inventoryTable = new InventoryTblImpl(databaseHandler, ingredientTable);
        recipeTable = new RecipeTblImpl(databaseHandler, ingredientTable);
        menuItemTable = new MenuItemTblImpl(databaseHandler, recipeTable, ingredientTable);
        menuTable = new MenuTblImpl(databaseHandler, menuItemTable);
        saleTable = new SaleTblImpl(databaseHandler, menuItemTable, inventoryTable);
    }
}
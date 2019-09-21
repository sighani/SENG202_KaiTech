package kaitech.io;

import kaitech.api.database.MenuItemTable;
import kaitech.api.model.*;
import kaitech.model.BusinessImpl;
import kaitech.parsing.IngredientLoader;
import kaitech.parsing.MenuLoader;
import kaitech.parsing.SupplierLoader;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.util.Map;

/**
 * Class to demonstrate the basics of using SAX 2.0 as implemented in JAXP. For
 * clarity, the various classes have been written separately. It is not uncommon
 * for the main class to be combined with the application-specfic handler
 * classes. In that case, this class would extend DefaultHandler or another
 * handler interface. Similarly, we might have an error handler as a nested
 * (private static) inner class.
 *
 * @author Neville Churcher
 */

public class LoadData {

    /**
     * Should extend to handle URLs too.
     */
    private static String pathName = null;

    private static Business business;
    /**
     * We'll allow an option to toggle validation.
     */
    private static boolean validating = true;

    /* Just stash the loaded data locally for now */
    private static Map<String, Supplier> suppsLoaded;
    private static Map<Ingredient, Integer> ingredientsLoaded;
    private static Map<String, MenuItem> menuItemsLoaded;
    private static Menu menuLoaded;

    /**
     * Entry point method sorts out the arguments, then hands over the SAX work to a
     * custom content handler. The handler configures the SAX parser, arranges for
     * the right object to be notified of parse events and then initiates the parse.
     */

    public static void loadSuppliers(String supplierFile) throws SAXException {
        if (checkFileOK(supplierFile)) {
            SupplierLoader supplierLoader = new SupplierLoader(pathName, validating);
            supplierLoader.parseInput();
            suppsLoaded = supplierLoader.getSuppliers();
        }
    }

    public static void loadMenu(String menuFile) throws SAXException {
        if (checkFileOK(menuFile)) {
            MenuLoader menuLoader = new MenuLoader(pathName, validating);
            menuLoader.parseInput();
            menuLoaded = menuLoader.getMenu();
            menuItemsLoaded = menuLoaded.getMenuItems();
        }
    }

    /**
     * Checking file validity and loading
     * Ingredients into static variables with ingredientLoader
     */
    public static void LoadIngredients(String ingredientsFile) throws SAXException {
        if (checkFileOK(ingredientsFile)) {
            IngredientLoader ingredientLoader = new IngredientLoader(pathName, validating);
            ingredientLoader.parseInput();
            ingredientsLoaded = ingredientLoader.getIngredients();
        }
    }

    /**
     * Saving Loaded ingredients to database
     */
    public static void saveIngredients() {
        business = BusinessImpl.getInstance();
        for (Ingredient ingredient : ingredientsLoaded.keySet()) {
            business.getIngredientTable().putIngredient(ingredient);
            business.getInventoryTable().putInventory(ingredient, ingredientsLoaded.get(ingredient));
        }
    }

    /**
     * Saving loaded menu into the database
     */
    public static void saveMenu() {
        business = BusinessImpl.getInstance();
        // putMenu already puts the menu items contained in the menu into the database
        business.getMenuTable().putMenu(menuLoaded);
    }

    /**
     * Saving loaded suppliers into the database
     */
    public static void saveSuppliers() {
        business = BusinessImpl.getInstance();
        for (Supplier supplier : suppsLoaded.values()) {
            business.getSupplierTable().putSupplier(supplier);
        }
    }

    //TODO
    private static boolean checkFileOK(String fName) {
        try {
            pathName = (new File(fName)).toURI().toURL().toString();
        } catch (IOException ioe) {
            System.err.println("Problem reading file: <" + fName + ">  Check for typos");
            ioe.printStackTrace();
            System.exit(666);// a bit brutal!
        }
        return true;
    }

    public static Map<Ingredient, Integer> ingredientsList() {
        return ingredientsLoaded;
    }

    public static Map<String, Supplier> supplierList() {
        return suppsLoaded;
    }

    public static Map<String, MenuItem> menuItems() {
        return menuItemsLoaded;
    }

    public static Menu menu() {
        return menuLoaded;
    }

}

package kaitech.io;

import kaitech.api.model.*;
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

    private static String pathName = null;

    /**
     * Toggle option for validating the XML file with the given DTD
     */
    private static boolean validating = true;

    /**
     * Temporary local storage for the data loaded from the XML files
     */
    private static Map<String, Supplier> suppsLoaded;
    private static Map<Ingredient, Integer> ingredientsLoaded;
    private static Map<String, MenuItem> menuItemsLoaded;
    private static Menu menuLoaded;

    /**
     * Loads the suppliers from a given file location, after checking the file is valid
     *
     * @param supplierFile The file String containing the suppliers
     * @throws SAXException when there is an error during loading
     */

    public static void loadSuppliers(String supplierFile) throws SAXException {
        if (checkFileOK(supplierFile)) {
            SupplierLoader supplierLoader = new SupplierLoader(pathName, validating);
            supplierLoader.parseInput();
            suppsLoaded = supplierLoader.getSuppliers();
        }
    }

    /**
     * Loads the Menu from a given file location, after checking the file is valid
     *
     * @param menuFile The file String containing the menus
     * @throws SAXException when there is an error during loading
     */
    public static void loadMenu(String menuFile) throws SAXException {
        if (checkFileOK(menuFile)) {
            MenuLoader menuLoader = new MenuLoader(pathName, validating);
            menuLoader.parseInput();
            menuLoaded = menuLoader.getMenu();
            menuItemsLoaded = menuLoaded.getMenuItems();
        }
    }

    /**
     * Loads the Ingredients from a given file location, after checking the file is valid
     *
     * @param ingredientsFile The file String containing the suppliers
     * @throws SAXException when there is an error during loading
     */
    public static void loadIngredients(String ingredientsFile) throws SAXException {
        if (checkFileOK(ingredientsFile)) {
            IngredientLoader ingredientLoader = new IngredientLoader(pathName, validating);
            ingredientLoader.parseInput();
            ingredientsLoaded = ingredientLoader.getIngredients();
        }
    }

    /**
     * Saves the Ingredients loaded in LoadIngredients() to the database of the given business
     */
    public static void saveIngredients(Business business) {
        for (Ingredient ingredient : ingredientsLoaded.keySet()) {
            business.getIngredientTable().putIngredient(ingredient);
            business.getInventoryTable().putInventory(ingredient, ingredientsLoaded.get(ingredient));
        }
    }

    /**
     * Saves the loaded Menu from LoadMenu to the database of the given business
     */
    public static void saveMenu(Business business) {
        business.getMenuTable().putMenu(menuLoaded);
    }

    /**
     * Saves loaded suppliers into the database of the given business
     */
    public static void saveSuppliers(Business business) {
        for (Supplier supplier : suppsLoaded.values()) {
            business.getSupplierTable().putSupplier(supplier);
        }
    }

    /**
     * Takes a file name and checks if it is valid, returning false if so
     *
     * @param fName The file name as a String
     * @return boolean isFileValid
     */
    public static boolean checkFileOK(String fName) {
        try {
            pathName = (new File(fName)).toURI().toURL().toString();
        } catch (IOException ioe) {
            System.err.println("Problem reading file: <" + fName + ">  Check for typos");
            ioe.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * Get ingredient list
     *
     * @return ingredientsLoaded
     */
    public static Map<Ingredient, Integer> ingredientsList() {
        return ingredientsLoaded;
    }

    /**
     * Get Suppliers List
     *
     * @return suppliersLoaded
     */
    public static Map<String, Supplier> supplierList() {
        return suppsLoaded;
    }

    /**
     * Get menuItems loaded
     *
     * @return menuItemsLoaded
     */
    public static Map<String, MenuItem> menuItems() {
        return menuItemsLoaded;
    }

    /**
     * Get menu loaded
     *
     * @return menuloaded
     */
    public static Menu menu() {
        return menuLoaded;
    }

}

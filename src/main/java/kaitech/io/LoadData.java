package kaitech.io;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import kaitech.model.Ingredient;
import kaitech.model.Menu;
import kaitech.model.MenuItem;
import kaitech.model.Supplier;
import kaitech.parsing.*;

/**
 * Class to demonstrate the basics of using SAX 2.0 as implemented in JAXP. For
 * clarity, the various classes have been written separately. It is not uncommon
 * for the main class to be combined with the application-specfic handler
 * classes. In that case, this class would extend DefaultHandler or another
 * handler interface. Similarly, we might have an error handler as a nested
 * (private static) inner class.
 *
 * @author Neville Churcher
 * @see MyErrorHandler
 */

public class LoadData {

    /**
     * Should extend to handle URLs too.
     */
    private static String pathName = null;

    /**
     * We'll allow an option to toggle validation.
     */
    private static boolean validating = true;

    /* Just stash the loaded data locally for now */
    private static Map<String, Supplier> suppsLoaded;
    private static Map<String, Ingredient> ingredientsLoaded;
    private static Map<String, MenuItem> menuItemsLoaded;
    private static Menu menuLoaded;
    /**
     * Entry point method sorts out the arguments, then hands over the SAX work to a
     * custom content handler. The handler configures the SAX parser, arranges for
     * the right object to be notified of parse events and then initiates the parse.
     */

    public static void loadSuppliers(String supplierFile) {
        if (checkFileOK(supplierFile)) {
            SupplierLoader supplierLoader = new SupplierLoader(pathName, validating);
            supplierLoader.parseInput();
            suppsLoaded = supplierLoader.getSuppliers();
        }
    }

    public static void loadMenu(String menuFile) {
        if (checkFileOK(menuFile)) {
            MenuLoader menuLoader = new MenuLoader(pathName, validating);
            menuLoader.parseInput();
            menuItemsLoaded = menuLoader.getMenuItems();
            menuLoaded = menuLoader.getMenu();
        }
    }

    public static void LoadIngredients(String ingredientsFile) {
        if (checkFileOK(ingredientsFile)) {
            IngredientLoader ingredientLoader = new IngredientLoader(pathName, validating);
            ingredientLoader.parseInput();
            ingredientsLoaded = ingredientLoader.getIngredients();
        }
    }

    private static boolean checkFileOK(String fName) {
        try {
            pathName = (new File(fName)).toURI().toURL().toString();
        } catch (IOException ioe) {
            System.err.println("Problem reading file: <" + fName + ">  Check for typos");
            System.err.println(ioe);
            System.exit(666);// a bit brutal!
        }
        return true;
    }

    public static Map<String, Ingredient> ingredientsList(){
        return ingredientsLoaded;
    }

    public static Map<String, Supplier> supplierList(){
        return suppsLoaded;
    }

    public static Map<String, MenuItem> menuItems() {
        return menuItemsLoaded;
    }

    public static Menu menu(){
        return menuLoaded;
    }

}

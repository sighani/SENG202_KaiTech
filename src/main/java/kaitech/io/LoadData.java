package kaitech.io;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import kaitech.model.Ingredient;
import kaitech.model.MenuItem;
import kaitech.model.Supplier;
import kaitech.parsing.IngredientLoader;
import kaitech.parsing.MyErrorHandler;
import kaitech.parsing.MenuLoaderExample;
import kaitech.parsing.SupplierLoader;

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

    /**
     * Entry point method sorts out the arguments, then hands over the SAX work to a
     * custom content handler. The handler configures the SAX parser, arranges for
     * the right object to be notified of parse events and then initiates the parse.
     */

    public static void loadSuppliers(String supplierFile) {
        if (checkFileOK(supplierFile)) {
            SupplierLoader aSupplierHandler = new SupplierLoader(pathName, validating);
            suppsLoaded = aSupplierHandler.getSuppliers();
        }
    }

    public static void loadMenu(String menuFile) {
        if (checkFileOK(menuFile)) {
            MenuLoaderExample aDOMdemo = new MenuLoaderExample(pathName, validating, System.out);
            aDOMdemo.parseInput();
            menuItemsLoaded = aDOMdemo.getMenuItems();
        }
    }

    public static void LoadIngredients(String ingredientsFile) {
        if (checkFileOK(ingredientsFile)) {
            IngredientLoader aDOMdemo = new IngredientLoader(pathName, validating);
            aDOMdemo.parseInput();
            aDOMdemo.getIngredients();
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

    public static Map<String,MenuItem> menuItems() {
        return menuItemsLoaded;
    }
}

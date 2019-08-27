package kaitech.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import kaitech.model.*;
import kaitech.util.MenuItemType;
import kaitech.util.PhoneType;
import kaitech.util.ThreeValueLogic;
import kaitech.util.UnitType;
import org.joda.money.Money;


import java.awt.event.ActionListener;
import java.util.List;
import java.util.Map;

import static kaitech.util.ThreeValueLogic.UNKNOWN;

/**
 * The controller for the manualData screen where the user makes a data entry
 * manually by filling out the fields.
 */
public class ManualDataController {
    /**
     * An instance of the ManualDataScreen, it will take information from here to be sent to the according model
     * class.
     */
    private ManualDataScreen theScreen = new ManualDataScreen();
   // private Supplier theSupplier;
    /**
     * An instance of the Business model class, this will process information sent to it by the controller, and send
     * back the processed information.
     */
    private Business theBusiness = new Business();
    /**
     * An instance of the Menu model class, this will process information sent to it by the controller, and send back
     * the processed information.
     */
    private Menu theMenu = new Menu();

    /**
     * A function for adding a new supplier to the system manually.
     */

   /* public ManualDataController(ManualDataScreen theScreen, Supplier theSupplier, Business theBusiness, Menu theMenu){
        this.theScreen = theScreen;
        this.theSupplier = theSupplier;
        this.theBusiness = theBusiness;
        this.theMenu = theMenu;


    }*/



    /**
     * This method gets called when the add supplier button on the ManualDataScreen is pressed, it gathers the relevant
     * information from the ManualDataScreen, and then creates a new Supplier. It then checks if the user clicked the
     * confirm or deny button, if the confirm button was pressed the Supplier will be added to the Business, otherwise
     * it will not and the data will be discarded.
     *
     * @param event Indicates the event which occurred, which caused the method to be called.
     */
    @FXML
    public void newSupplier(ActionEvent event) {
        String sid, name, address, phone, email, url = "";
        sid = theScreen.getSupplierID();
        name = theScreen.getName();
        address = theScreen.getAddr();
        phone = theScreen.getPhone();
        email = theScreen.getEmail();
        url = theScreen.getURL();
        PhoneType pt = theScreen.getPhoneType();
        Supplier newSupplier = new Supplier(sid, name, address, email, pt, email, url);
        String confirmValue = ((Button)event.getSource()).getText();
        if(confirmValue.equals("Confirm")) {
            theBusiness.addSupplier(newSupplier);
        }
        else {
            // here probably set a label to something like "Cancel clicked, Ingredient not added.
        }





    }

    /**
     * This method gets called when the addNewIngredient button gets pressed, it gets all the relevant information from
     * the GUI and creates a new Ingredient. It then checks whether the user confirmed the addition, if they did,
     * then the Ingredient is added to the business, otherwise it will not and the data will be discarded.
     *
     * @param event Indicates the event which occurred, which caused the method to be called.
     */

    @FXML
    public void newIngredient(ActionEvent event) {
        String code, name;
        ThreeValueLogic isVeg, isVegan, isGF = UNKNOWN;
        UnitType unit = UNKNOWN;
        code = theScreen.getIngredientCode();
        name = theScreen.getIngredientName();
        unit = theScreen.getUnit();
        Money price = theScreen.getPrice();
        isVeg = theScreen.getIsVeg();
        isVegan = theScreen.getIsVegan();
        isGF = theScreen.getIsGF();

        Ingredient newIngredient = new Ingredient(code, name, unit, price, isVeg, isVegan, isGF);

        String confirmValue = ((Button)event.getSource()).getText();

        if(confirmValue == "confirm") {
            this.theBusiness.addIngredient(newIngredient);
        }
        else {
            // here probably set a label to something like "Cancel clicked, Ingredient not added.
        }

    }

    /**
     * This method gets called when the addNewMenuItem button gets pressed, it gets all the relevant information from
     * the GUI and creates a new MenuItem. It then checks whether the user confirmed the addition, if they did,
     * then the MenuItem is added to the Menu, otherwise it will not and the data will be discarded.
     *
     * @param event Indicates the event which occurred, which caused the method to be called.
     */
    @FXML
    public void newMenuItem(ActionEvent event) {
        int prepTime, cookTime, numServings = 0;
        prepTime = theScreen.getPrepTime();
        cookTime = theScreen.getCookTime();
        Map<Ingredient, Integer> ingredients = theScreen.getRecipeIngredients();
        numServings = theScreen.getNumServings();

        String code, name = "";
        List<String> ingredientList = theScreen.getIngredientList();
        code = theScreen.getMenuCode;
        name = theScreen.getMenuItemName();
        Money price = theScreen.getMoney();
        MenuItemType type = theScreen.getType;
        Recipe newRecipe = new Recipe(ingredients, prepTime, cookTime, numServings);
        MenuItem newMenuItem = new MenuItem(code, name, ingredientList, newRecipe, price, type);

        String confirmValue = ((Button)event.getSource()).getText();

        if(confirmValue == "confirm") {
            theMenu.addMenuItem(newMenuItem);
        }
        else {
            // here probably set a label to something like "Cancel clicked, MenuItem not added.
        }


    }

    /**
     * A function for adding a new menu to the system manually.
     */
    public void newMenu() {

    }

    /**
     * A function for adding new stock to the system manually.
     */
    public void newStock() {

    }

    /**
     * Confirms the adding of an item to the system.
     *
     * @param event Indicates the event which occurred, which caused the method to be called.
     */
    public Boolean confirm(ActionEvent event){
        String confirmCheck = ((Button) event.getSource()).getText();
        return "Confirm".equals(confirmCheck);



    }

    /**
     * Changes the currently displayed scene to the main menu.
     *
     * @param event Indicates the event which occurred, which caused the method to be called.
     */
    @FXML
    public void returnToMain(ActionEvent event) {
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stage.setScene(mainMenuScene);

    }

}

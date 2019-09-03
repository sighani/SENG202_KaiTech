package kaitech.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import kaitech.api.model.Business;
import kaitech.api.model.Ingredient;
import kaitech.model.BusinessImpl;
import kaitech.model.IngredientImpl;
import kaitech.model.MenuImpl;
import kaitech.model.SupplierImpl;
import kaitech.util.PhoneType;

import java.io.IOException;

/**
 * The controller for the manualData screen where the user makes a data entry
 * manually by filling out the fields.
 */
public class ManualDataController {
    @FXML
    private TextField ingredCode;
    @FXML
    private TextField ingredName;
    @FXML
    private TextField ingredCost;
    @FXML
    private ComboBox<String> ingredUnit;
    @FXML
    private ComboBox<String> isVege;
    @FXML
    private ComboBox<String> isVegan;
    @FXML
    private ComboBox<String> isGf;
    //@FXML
    //private TextField supID;
    @FXML
    private TextField supName;
    @FXML
    private TextField supAddress;
    @FXML
    private ComboBox<String> supNumType;
    @FXML
    private TextField supNumber;
    @FXML
    private TextField supEmail;
    @FXML
    private TextField supURL;

    @FXML
    private Text manualUploadText;

    @FXML
    private TextField menuName;

    @FXML
    private TextField menuID;

    @FXML
    private TextField menuItemCode;

    @FXML
    private TextField menuItemName;

    @FXML
    private TextField menuItemIngredients;

    @FXML
    private TextField menuItemRecipe;

    @FXML
    private TextField menuItemPrice;

    @FXML
    private ComboBox<String> menuItemType;

    private Business business;

    @FXML
    public void initialize() {
        business = BusinessImpl.getInstance();
    }


    /**
     * @param event ingredient button pressed, ingredient form is opened.
     * @throws IOException error is printed
     */
    public void addIngredient(ActionEvent event) throws IOException{
        try {
            //When ingredient button pressed, get ingredient form scene
            Parent ingredPar = FXMLLoader.load(getClass().getResource("ingredient.fxml"));
            Scene ingredScene = new Scene(ingredPar);

            //Get stage info and set scene to ingredient form
            Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
            window.setScene(ingredScene);
            window.show();
        }catch (IOException e){
            throw new IOException("Error in opening ingredient form.");
        }
    }

    /**
     * This method is run when the confirm button is pressed on the ingredient form, the method then gets the relevant
     * information from the user input, creates a new ingredient from it, and then adds it to an instance of Business.
     * It also gives back informational feedback to the user to inform them that the ingredient has been added
     * successfully.
     */
    public void confirmIngredient(){
        String code = ingredCode.getText();
        String name = ingredName.getText();
        String unit = ingredUnit.getValue();
        String cost = ingredCost.getText();
        String vege = isVege.getValue();
        String vegan = isVegan.getValue();
        String gf = isGf.getValue();

        //IngredientImpl newIngredient = new IngredientImpl(code, name, unit, cost, vege, vegan, gf);
        //business.addIngredient(newIngredient);

        System.out.println("Code: " + code);
        System.out.println("Name: " + name);
        System.out.println("Unit: " + unit);
        System.out.println("Cost: " + cost);
        System.out.println("Is vegetarian: " + vege);
        System.out.println("Is vegan: " + vegan);
        System.out.println("Is gf: " + gf);
        manualUploadText.setText("Ingredient: " + name + ", has been added.  ");
        manualUploadText.setVisible(true);
    }

    /**
     * @param event When supplier button is pressed, supplier form is opened.
     * @throws IOException error is printed
     */
    public void addSupplier(ActionEvent event) throws IOException{
        try {
            //When supplier button pressed, get supplier form scene
            Parent supPar = FXMLLoader.load(getClass().getResource("supplier.fxml"));
            Scene supScene = new Scene(supPar);

            //get stage info and set scene to supplier form
            Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
            window.setScene(supScene);
            window.show();
        } catch (Exception e){
            throw new IOException("Error in opening supplier form.");
        }

    }

    /**
     * This method is run when the confirm button is pressed on the supplier form, the method then gets the relevant
     * information from the user input, creates a new supplier from it, and then adds it to an instance of Business.
     * It also gives back informational feedback to the user to inform them that the supplier has been added
     * successfully.
     */
    public void confirmSupplier(){
        //String id = supID.getText();
        String name = supName.getText();
        String address = supAddress.getText();
        String type = supNumType.getValue();
        String number = supNumber.getText();
        String email = supEmail.getText();
        String url = supURL.getText();


        //SupplierImpl newSupplier = new SupplierImpl(id, name, address, number, type, email, url);
        //business.addSupplier(newSupplier);

        System.out.println("Name: " + name);
        System.out.println("Address: " + address);
        System.out.println("Number Type: " + type);
        System.out.println("Phone Number: " + number);
        System.out.println("Email: " + email);
        System.out.println("URL: " + url);
        manualUploadText.setText("Supplier: " + name + ", has been added.  ");
        manualUploadText.setVisible(true);
    }

    /**
     * Changes the scene from the dataType screen, to the menu form screen.
     *
     * @param event When menu button is pressed, menu form is opened.
     * @throws IOException catches an error and prints an error message.
     */

    public void addMenu(ActionEvent event) throws IOException{
        try {
            //When menu button is pressed get menu information from scene.
            Parent menuPar = FXMLLoader.load(getClass().getResource("menu.fxml"));
            Scene menuScene = new Scene(menuPar);

            //get stage info and set scene to supplier form
            Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
            window.setScene(menuScene);
            window.show();
        } catch (Exception e){
            throw new IOException("Error in opening supplier form.");
        }

    }

    /**
     * This method is called when the confirm button is pressed on the menu form. It gets the relevant information
     * from the menu screen, and then creates a new menu, and adds this to an instance of Business. It also prints
     * informational feedback, so the user can see that the menu was successfully added.
     */

    public void confirmMenu() {
        String ID = menuID.getText();
        String name = menuName.getText();

        MenuImpl newMenu = new MenuImpl(name, ID);
        //business.addMenu(newMenu);

        System.out.println("Name: " + name);
        System.out.println("ID: " + ID);
        manualUploadText.setText("Menu: " + name + ", has been added.  ");
        manualUploadText.setVisible(true);

    }

    public void addMenuItem(ActionEvent event) throws IOException {
        try {
            //When menu button is pressed get menu information from scene.
            Parent menuItemPar = FXMLLoader.load(getClass().getResource("menuItem.fxml"));
            Scene menuItemScene = new Scene(menuItemPar);

            //get stage info and set scene to supplier form
            Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
            window.setScene(menuItemScene);
            window.show();
        } catch (Exception e){
            throw new IOException("Error in opening MenuItem form.");
        }
    }

    public void confirmMenuItem() {
        String code = menuItemCode.getText();
        String name = menuItemName.getText();
        String ingred = menuItemIngredients.getText();
        String recipe = menuItemRecipe.getText();
        String price = menuItemPrice.getText();
        String type = menuItemType.getValue();

        //MenuItemImpl newMenuItem = new MenuItemImpl(code, name, ingred, recipe, price, type);
        //business.addMenu(newMenuItem);

        System.out.println("Name: " + name);
        System.out.println("Code: " + code);
        System.out.println("Type: " + type);
        System.out.println("Ingredients: " + ingred);
        System.out.println("Recipe: " + recipe);
        System.out.println("Price: " + price);
        manualUploadText.setText("MenuItem: " + name + ", has been added.  ");
        manualUploadText.setVisible(true);


    }

    public void addStock() {}

    public void addUser() {}









    /**
     * @param event Exit button is pressed, scene is changed to main menu
     * @throws IOException Print error
     */
    public void exitManualInput(ActionEvent event) throws IOException {
        try {
            //When exit button pressed
            Parent mainMenuParent = FXMLLoader.load(getClass().getResource("MainMenu.fxml"));
            Scene MainMenuScene = new Scene(mainMenuParent);

            //This line gets the Stage information, and returns to main menu
            Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
            window.setScene(MainMenuScene);
            window.show();
        }catch (IOException e){
            throw new IOException("Error in exiting manual input.");
        }
    }
}

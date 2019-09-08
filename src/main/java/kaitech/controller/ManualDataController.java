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
import javafx.stage.Modality;
import javafx.stage.Stage;
import kaitech.api.model.Business;
import kaitech.api.model.Ingredient;
import kaitech.model.BusinessImpl;
import kaitech.model.IngredientImpl;
import kaitech.model.MenuImpl;
import kaitech.model.SupplierImpl;
import kaitech.util.MenuItemType;
import kaitech.util.PhoneType;
import kaitech.util.ThreeValueLogic;

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

    @FXML
    private Text manualUploadText;

    @FXML
    private TextField menuName;

    @FXML
    private TextField menuID;


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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ingredient.fxml"));
            Parent root = loader.load();
            NewIngredientController controller = loader.<NewIngredientController>getController();
            controller.setComboBoxes();
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.setTitle("Adding new supplier:");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {

        }
    }


    /**
     * @param event When supplier button is pressed, supplier form is opened.
     * @throws IOException error is printed
     */
    public void addSupplier(ActionEvent event) throws IOException{
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("supplier.fxml"));
            Parent root = loader.load();
            NewSupplierController controller = loader.<NewSupplierController>getController();
            controller.setComboBoxes();
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.setTitle("Adding new supplier:");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {

        }
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

       // MenuImpl newMenu = new MenuImpl(name, ID);
        //business.addMenu(newMenu);

        System.out.println("Name: " + name);
        System.out.println("ID: " + ID);
        manualUploadText.setText("Menu: " + name + ", has been added.  ");
        manualUploadText.setVisible(true);

    }

    public void addMenuItem(ActionEvent event) throws IOException {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("menuItem.fxml"));
            Parent root = loader.load();
            NewMenuItemController controller = loader.<NewMenuItemController>getController();
            controller.setComboBoxes();
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.setTitle("Modify Ingredient details");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {

        }
    }




    public void addStock() {}










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

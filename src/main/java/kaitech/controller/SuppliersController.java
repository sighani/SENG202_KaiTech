package kaitech.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import kaitech.api.model.Business;
import kaitech.model.BusinessImpl;

import java.io.IOException;

/**
 * The controller for the supplier screen, which is a list of all the suppliers.
 * Can add/delete/modify suppliers provided the user is logged in.
 */
public class SuppliersController {

    private Business business;

    @FXML
    public void initialize() {
        business = BusinessImpl.getInstance();
    }


    /**
     * A function which allows the user to modify the information currently stored about a supplier.
     */
    public void modifySupplier(ActionEvent event) throws IOException{

        try {
            Parent supParent = FXMLLoader.load(getClass().getResource("gui/forms/supplier.fxml"));
            Scene supScene = new Scene(supParent);

            Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
            window.setScene(supScene);
            window.show();
        } catch (Exception e){
            throw new IOException("Error in opening supplier form.");
        }



    }

    /**
     * A function which allows the user to delete the information about a supplier currently stored in the system.
     *
     * @param event
     */

    @FXML
    public void deleteSupplier(ActionEvent event) {
        //String supplierID = supID.getValue();
        //Supplier supplierToRemove = business.getSuppliers(supplierID);
        //business.removeSupplier(supplierToRemove);

    }

    /**
     * A function which confirms the action of modifying changes to a supplier.
     */
    public void confirmModify() {
        /*
        String name = supName.getText();
        String address = supAddress.getText();
        String type = supNumType.getValue();
        String number = supNumber.getText();
        String email = supEmail.getText();
        String url = supURL.getText();


        // supID = supID.getText();
        // Supplier supToEdit = buisness.getSupplier(supID);
        // supToEdit.setPhoneType(type);
        // supToEdit.setEmail(email);
        // supToEdit.setURL(url);

        System.out.println("Updated supplier information:");
        System.out.println("Name: " + name);
        System.out.println("Address: " + address);
        System.out.println("Number Type: " + type);
        System.out.println("Phone Number: " + number);
        System.out.println("Email: " + email);
        System.out.println("URL: " + url);
        manualUploadText.setText("Supplier: " + name + ", has been edited.  ");
        manualUploadText.setVisible(true);*/
    }

    /**
     * A function which confirms the action of deleting a supplier.
     */
    public void confirmDelete() {

    }

    /**
     * Takes the user to the next page.
     *
     * @param event Indicates the event which occurred, which caused the method to be called.
     */
    @FXML
    public void nextPage(ActionEvent event) {
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        //stage.setScene(nextPage);

    }

    /**
     * Takes the user to the previous page.
     *
     * @param event Indicates the event which occurred, which caused the method to be called.
     */
    @FXML
    public void previousPage(ActionEvent event) {
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        //stage.setScene(previousPage);

    }

    /**
     * Changes the currently displayed scene to the main menu.
     *
     * @param event Indicates the event which occurred, which caused the method to be called.
     */
    @FXML
    public void returnToMain(ActionEvent event) throws IOException {
        try {
            Parent mainMenuParent = FXMLLoader.load(getClass().getResource("gui/MainMenu.fxml"));
            Scene MainMenuScene = new Scene(mainMenuParent);

            Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
            window.setScene(MainMenuScene);
            window.show();

        } catch (IOException e) {
            throw new IOException("Error in exiting manual input.");

        }


    }
}

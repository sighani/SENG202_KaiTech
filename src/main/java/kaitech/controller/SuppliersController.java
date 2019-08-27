package kaitech.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.stage.Stage;
import kaitech.model.Business;
import kaitech.model.Supplier;

/**
 * The controller for the supplier screen, which is a list of all the suppliers.
 * Can add/delete/modify suppliers provided the user is logged in.
 */
public class SuppliersController {

    private SuppliersScreen theScreen = new SuppliersScreen();

    private Business theBusiness = new Business();


    /**
     * A function which allows the user to modify the information currently stored about a supplier.
     */
    public void modifySupplier() {

    }

    /**
     * A function which allows the user to delete the information about a supplier currently stored in the system.
     *
     * @param event
     */

    @FXML
    public void deleteSupplier(ActionEvent event) {
        Supplier supplier = theScreen.getDeletedSupplier();
        theBusiness.removeSupplier(supplier);

    }

    /**
     * A function which confirms the action of modifying changes to a supplier.
     */
    public void confirmModify() {

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
        stage.setScene(nextPage);

    }

    /**
     * Takes the user to the previous page.
     *
     * @param event Indicates the event which occurred, which caused the method to be called.
     */
    @FXML
    public void previousPage(ActionEvent event) {
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stage.setScene(previousPage);

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

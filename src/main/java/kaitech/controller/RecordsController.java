package kaitech.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.stage.Stage;
import kaitech.model.Sale;

/**
 * The controller for the records screen, which shows a list of sales records, which
 * may span over several pages. Sales may be edited provided the user has logged in.
 */
public class RecordsController {
    /**
     * Adjusts the details of a sale, used if a sale was initially input incorrectly.
     * @param sale The sale which was input incorrectly and therefore is having its details adjusted.
     */


    public void adjustDetails(Sale sale) {

    }

    /**
     *Confirms the details of the given sale and stores the information in the system.
     * @param sale The sale which is being confirmed.
     */
    public void confirmChanges(Sale sale) {

    }

    /**
     * Deletes the record of the chosen sale.
     * @param sale The sale which is being deleted.
     */
    public void deleteRecord(Sale sale) {

    }

    /**
     * Takes the user to the next page.
     */
    public void nextPage() {

    }

    /**
     * Takes the user to the previous page.
     */
    public void previousPage() {
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

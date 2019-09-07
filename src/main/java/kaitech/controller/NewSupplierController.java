package kaitech.controller;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import kaitech.api.model.Business;
import kaitech.model.BusinessImpl;
import kaitech.model.SupplierImpl;
import kaitech.util.PhoneType;

public class NewSupplierController {

    @FXML
    private TextField supID;
    @FXML
    private TextField supName;
    @FXML
    private TextField supAddress;
    @FXML
    private ComboBox supNumType;
    @FXML
    private TextField supNumber;
    @FXML
    private TextField supEmail;
    @FXML
    private TextField supURL;
    @FXML
    private Text titleText;
    @FXML
    private Text manualUploadText;


    private Business business;

    public void initialize() {
        business = BusinessImpl.getInstance();
    }

    public void setComboBoxes() {
        supNumType.getItems().setAll(PhoneType.values());

    }

    public void exit() {
        Stage stage = (Stage) titleText.getScene().getWindow();
        stage.close();

    }


    /**
     * This method is run when the confirm button is pressed on the supplier form, the method then gets the relevant
     * information from the user input, creates a new supplier from it, and then adds it to an instance of Business.
     * It also gives back informational feedback to the user to inform them that the supplier has been added
     * successfully.
     */
    public void confirm() {
        String id = supID.getText();
        String name = supName.getText();
        String address = supAddress.getText();
        PhoneType type = (PhoneType) supNumType.getValue();
        String number = supNumber.getText();
        String email = supEmail.getText();
        String url = supURL.getText();



        SupplierImpl newSupplier = new SupplierImpl(id, name, address, number, type, email, url);
        business.addSupplier(newSupplier);

        System.out.println("Name: " + name);
        System.out.println("Address: " + address);
        System.out.println("Number Type: " + type);
        System.out.println("Phone Number: " + number);
        System.out.println("Email: " + email);
        System.out.println("URL: " + url);
        manualUploadText.setText("Supplier: " + name + ", has been added.  ");
        manualUploadText.setVisible(true);
    }

}

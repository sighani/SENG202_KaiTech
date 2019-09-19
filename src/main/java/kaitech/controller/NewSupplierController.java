package kaitech.controller;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import kaitech.api.database.SupplierTable;
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
    @FXML
    private Text responseText;

    private SupplierTable suppliers;

    private Business business;

    public void initialize() {

        business = BusinessImpl.getInstance();
        suppliers = business.getSupplierTable();
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
        if (fieldsAreValid()) {
            try {
                String id = supID.getText();
                String name = supName.getText();
                String address = supAddress.getText();
                PhoneType type = (PhoneType) supNumType.getValue();
                String number = supNumber.getText();
                String email = supEmail.getText();
                String url = supURL.getText();


                SupplierImpl newSupplier = new SupplierImpl(id, name, address, number, type, email, url);
                suppliers.putSupplier(newSupplier);

                System.out.println("Name: " + name);
                System.out.println("Address: " + address);
                System.out.println("Number Type: " + type);
                System.out.println("Phone Number: " + number);
                System.out.println("Email: " + email);
                System.out.println("URL: " + url);
                manualUploadText.setText("Supplier: " + name + ", has been added.  ");
                manualUploadText.setVisible(true);
            } catch (RuntimeException e) {
                manualUploadText.setText("That ID already exists, please enter a unique ID.");
                manualUploadText.setVisible(true);
            }
        }
        else {
            responseText.setVisible(true);
        }
    }

    public boolean fieldsAreValid() {
        boolean isValid = true;
        if (!supNumber.getText().replaceAll("\\s+","").matches("[0-9]+")) {
            responseText.setText("Phone number can contain digits and spaces only.");
            isValid = false;
        }
        if (supName.getText().trim().length() == 0 ||
                supAddress.getText().trim().length() == 0 || supNumber.getText().trim().length() == 0 ||
                supEmail.getText().trim().length() == 0 || supURL.getText().trim().length() == 0 || supID.getText().trim().length() == 0) {
            responseText.setText("A field is empty.");
            isValid = false;
        }
        if (supNumType.getValue() == null) {
            responseText.setText("Please select a phone number type.");
            isValid = false;
        }
        return isValid;
    }

}

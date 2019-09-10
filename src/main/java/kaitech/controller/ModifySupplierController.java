package kaitech.controller;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import kaitech.api.database.SupplierTable;
import kaitech.api.model.Business;
import kaitech.api.model.Supplier;
import kaitech.model.BusinessImpl;
import kaitech.util.PhoneType;

public class ModifySupplierController {
    @FXML
    private Text titleText;

    @FXML
    private TextField nameField;

    @FXML
    private TextField addressField;

    @FXML
    private TextField phField;

    @FXML
    private ComboBox phTypeCB;

    @FXML
    private TextField emailField;

    @FXML
    private TextField websiteField;

    @FXML
    private Text responseText;

    private Business business;

    /**
     * The Supplier that is being modified.
     */
    private Supplier supplier;

    /**
     * Sets the supplier and calls the start method. This is used as an alternative to an initialize method as the
     * supplier must be obtained as a parameter.
     * @param supplier The supplier to modify
     */
    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
        start();
    }

    public void start() {
        titleText.setText("Now modifying Supplier " + supplier.getName() + "(" + supplier.getId() + ")");
        business = BusinessImpl.getInstance();
        nameField.setText(supplier.getName());
        addressField.setText(supplier.getAddress());
        phField.setText(supplier.getPhone());
        phTypeCB.getItems().setAll(PhoneType.values());
        phTypeCB.getSelectionModel().select(supplier.getPhoneType());
        emailField.setText(supplier.getEmail());
        websiteField.setText(supplier.getUrl());
    }

    public void confirm() {
        if (fieldsAreValid()) {
            supplier.setName(nameField.getText());
            supplier.setAddress(addressField.getText());
            supplier.setPhone(phField.getText());
            supplier.setPhoneType((PhoneType) phTypeCB.getValue());
            supplier.setEmail(emailField.getText());
            supplier.setUrl(websiteField.getText());
            Stage stage = (Stage) titleText.getScene().getWindow();
            stage.close();
        }
        else {
            responseText.setVisible(true);
        }
    }

    /**
     * Checks the validity of the TextFields, including if any fields have been left empty, and that the phone number
     * consists of digits and spaces only.
     * @return A boolean, true if all fields are valid, false otherwise.
     */
    public boolean fieldsAreValid() {
        boolean isValid = true;
        if (!phField.getText().replaceAll("\\s+","").matches("[0-9]+")) {
            responseText.setText("Phone number can contain digits and spaces only.");
            isValid = false;
        }
        if (nameField.getText().trim().length() == 0 ||
                addressField.getText().trim().length() == 0 || phField.getText().trim().length() == 0 ||
                emailField.getText().trim().length() == 0 || websiteField.getText().trim().length() == 0) {
            responseText.setText("A field is empty.");
            isValid = false;
        }
        return isValid;
    }
}

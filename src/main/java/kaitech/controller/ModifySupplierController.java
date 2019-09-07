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
    private TextField idField;

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

    private Supplier supplier;

    private String initialId;

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
        start();
    }

    public void start() {
        titleText.setText("Now modifying Supplier " + supplier.getName() + "(" + supplier.getID() + ")");
        business = BusinessImpl.getInstance();
        idField.setText(supplier.getID());
        initialId = supplier.getID();
        nameField.setText(supplier.getName());
        addressField.setText(supplier.getAddress());
        phField.setText(supplier.getPhone());
        phTypeCB.getItems().setAll(PhoneType.values());
        phTypeCB.getSelectionModel().select(supplier.getPhoneType());
        emailField.setText(supplier.getEmail());
        websiteField.setText(supplier.getURL());
    }

    public void confirm() {
        if (fieldsAreValid()) {
            //TODO: Setting ID's is NOT supported!
            //supplier.setSid(idField.getText());
            supplier.setName(nameField.getText());
            supplier.setAddress(addressField.getText());
            supplier.setPhone(phField.getText());
            supplier.setPhoneType((PhoneType) phTypeCB.getValue());
            supplier.setEmail(emailField.getText());
            supplier.setURL(websiteField.getText());
            Stage stage = (Stage) titleText.getScene().getWindow();
            stage.close();
        }
        else {
            responseText.setVisible(true);
        }
    }

    public boolean fieldsAreValid() {
        boolean isValid = true;
        SupplierTable supplierTable = business.getSupplierTable();
        if (idField.getText().trim().length() == 0 || nameField.getText().trim().length() == 0 ||
                addressField.getText().trim().length() == 0 || phField.getText().trim().length() == 0 ||
                emailField.getText().trim().length() == 0 || websiteField.getText().trim().length() == 0) {
            responseText.setText("A field is empty.");
            isValid = false;
        }
        // TODO: Is this necessary? Supplier IDs can't be changed (database integrity reasons)
        if (supplierTable.getAllSupplierIDs().contains(idField.getText()) && !idField.getText().equals(initialId)) {
            responseText.setText("There is a supplier with that ID already.");
            isValid = false;
        }
        if (!phField.getText().replaceAll("\\s+","").matches("[0-9]+")) {
            responseText.setText("Phone number can contain digits and spaces only.");
            isValid = false;
        }
        return isValid;
    }
}

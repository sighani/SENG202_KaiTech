package kaitech.controller;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
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
        titleText.setText("Now modifying Supplier " + supplier.getName() + "(" + supplier.getId() + ")");
        business = BusinessImpl.getInstance();
        idField.setText(supplier.getId());
        initialId = supplier.getId();
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
            supplier.setSid(idField.getText());
            supplier.setName(nameField.getText());
            supplier.setAddress(addressField.getText());
            supplier.setPhone(phField.getText());
            supplier.setPhType((PhoneType) phTypeCB.getValue());
            supplier.setEmail(emailField.getText());
            supplier.setUrl(websiteField.getText());
            Stage stage = (Stage) titleText.getScene().getWindow();
            stage.close();
        }
        else {
            responseText.setVisible(true);
        }
    }

    public boolean fieldsAreValid() {
        boolean isValid = true;
        if (idField.getText().trim().length() == 0 || nameField.getText().trim().length() == 0 ||
                addressField.getText().trim().length() == 0 || phField.getText().trim().length() == 0 ||
                emailField.getText().trim().length() == 0 || websiteField.getText().trim().length() == 0) {
            responseText.setText("A field is empty.");
            isValid = false;
        }
        for (Supplier supplier : business.getSuppliers()) {
            if (idField.getText().equals(supplier.getId()) && !idField.getText().equals(initialId)) {
                responseText.setText("There is a supplier with that ID already.");
                isValid = false;
            }
        }
        if (!phField.getText().replaceAll("\\s+","").matches("[0-9]+")) {
            responseText.setText("Phone number can contain digits and spaces only.");
            isValid = false;
        }
        return isValid;
    }
}

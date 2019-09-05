package kaitech.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import kaitech.api.model.Business;
import kaitech.api.model.Supplier;

public class ModifySupplierController {
    @FXML
    private TextField idField;

    @FXML
    private TextField nameField;

    @FXML
    private TextField addressField;

    @FXML
    private TextField phField;

    @FXML
    private TextField phTypeField;

    @FXML
    private TextField emailField;

    @FXML
    private TextField websiteField;

    private Business business;

    private Supplier supplier;

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }
}

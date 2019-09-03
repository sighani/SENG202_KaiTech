package kaitech.controller;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import kaitech.api.model.Sale;
import kaitech.util.PaymentType;

public class ModifyRecordController {
    @FXML
    private Text titleText;

    @FXML
    private TextField notesUsed;

    @FXML
    private TextField priceTotal;

    @FXML
    private TextField date;

    @FXML
    private TextField time;

    @FXML
    private ComboBox paymentType;


    private Sale sale;

    public void setRecord(Sale sale) {
        this.sale = sale;
        start();
    }

    /**
     * This method initialises the modifyRecord.fxml screen.
     */
    public void start() {
        date.setText(sale.getDate());
        notesUsed.setText(sale.getNotes());
        priceTotal.setText(sale.getPrice().toString());
        time.setText(sale.getTime());
        paymentType.getItems().setAll(PaymentType.values());
        paymentType.getSelectionModel().select(sale.getPaymentType());

    }
}

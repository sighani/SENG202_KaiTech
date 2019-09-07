package kaitech.controller;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import kaitech.api.model.Sale;
import kaitech.util.PaymentType;
import org.joda.money.Money;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

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

    @FXML
    private Text responseText;


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

    public void exit() {
        Stage stage = (Stage) titleText.getScene().getWindow();
        stage.close();

    }

    public void confirm() {
        if (fieldsAreValid()) {
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
            LocalTime newTime = LocalTime.parse(time.getText(), timeFormatter);
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd LLLL yyyy");
            LocalDate newDate = LocalDate.parse(date.getText(), dateFormatter);
            sale.setDate(newDate);
            sale.setTime(newTime);
            sale.setPaymentType((PaymentType) paymentType.getValue());
            sale.setTotalPrice(Money.parse(priceTotal.getText()));
            sale.setNotes(notesUsed.getText());
            Stage stage = (Stage) titleText.getScene().getWindow();
            stage.close();

        }
        else {
            responseText.setVisible(true);
        }
    }

    public boolean fieldsAreValid() {
       boolean isValid = true;
        if (date.getText().trim().length() == 0 || time.getText().trim().length() == 0 ||
                notesUsed.getText().trim().length() == 0 || priceTotal.getText().trim().length() == 0) {
            responseText.setText("A field is empty.");
            isValid = false;
        }
        try {
            Money newPrice = Money.parse(priceTotal.getText());
        }
        catch (IllegalArgumentException e) {
            responseText.setText("Invalid Cost value. Prices should be of the form X.XX where X is a digit");
            isValid = false;
        }
        catch (ArithmeticException e) {
            responseText.setText("Restrict the Cost value to two digits after the decimal point.");
            isValid = false;
        }
        try {
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
            LocalTime newTime = LocalTime.parse(time.getText(), timeFormatter);
        }
        catch  (DateTimeException e){
            responseText.setText("Time format should be HH:MM:SS.");
            isValid = false;
        }

        try {
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd LLLL yyyy");
            LocalDate newDate = LocalDate.parse(date.getText(), dateFormatter);

        }
        catch (DateTimeException e) {
            responseText.setText("Date format should be dd LLLL yyyy.");
            isValid = false;
        }
        return isValid;
    }

}

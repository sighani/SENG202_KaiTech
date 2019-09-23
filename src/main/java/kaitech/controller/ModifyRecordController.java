package kaitech.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import kaitech.api.model.MenuItem;
import kaitech.api.model.Sale;
import kaitech.util.PaymentType;
import org.joda.money.Money;

import java.io.IOException;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

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
    private Map<MenuItem, Integer> newItemsOrdered;

    public void setRecord(Sale sale) {
        this.sale = sale;
        start();
    }

    /**
     * This method initialises the modifyRecord.fxml screen.
     */
    public void start() {
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        date.setText(sale.getDate().toString());
        notesUsed.setText(sale.getNotes());
        priceTotal.setText(sale.getTotalPrice().toString());
        time.setText(timeFormatter.format(sale.getTime()));
        paymentType.getItems().setAll(PaymentType.values());
        paymentType.getSelectionModel().select(sale.getPaymentType());
        newItemsOrdered = new HashMap<>();
    }

    /**
     * Closes the current screen.
     */
    public void exit() {
        Stage stage = (Stage) titleText.getScene().getWindow();
        stage.close();
    }

    /**
     * Checks if the return value of fieldsAreValid is true, if it is confirms the modifying of the record, otherwise
     * it gives a relevant response to the user.
     */
    public void confirm() {
        if (fieldsAreValid()) {
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
            LocalTime newTime = LocalTime.parse(time.getText(), timeFormatter);
            LocalDate newDate = LocalDate.parse(date.getText());
            sale.setDate(newDate);
            sale.setTime(newTime);
            sale.setPaymentType((PaymentType) paymentType.getValue());
            sale.setTotalPrice(Money.parse(priceTotal.getText()));
            sale.setNotes(notesUsed.getText());
            if (!newItemsOrdered.isEmpty()) {
                sale.setItemsOrdered(newItemsOrdered);
            }
            Stage stage = (Stage) titleText.getScene().getWindow();
            stage.close();

        } else {
            responseText.setVisible(true);
        }
    }

    /**
     * Launches a screen where the user can select the new itemsOrdered for the selected MenuItem.
     */
    public void selectItemsOrdered() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("modifyItemsOrdered.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.setTitle("Adjust Items Ordered.");
            stage.setScene(new Scene(root));
            stage.show();
            AdjustItemsOrderedController controller = loader.<AdjustItemsOrderedController>getController();
            controller.setItemsOrdered(newItemsOrdered);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    /**
     * Checks if the fields are valid.
     * @return A boolean, true if fields are valid, false otherwise.
     */
    public boolean fieldsAreValid() {
        boolean isValid = true;
        if (date.getText().trim().length() == 0 || time.getText().trim().length() == 0 ||
                priceTotal.getText().trim().length() == 0) {
            responseText.setText("A field is empty.");
            isValid = false;
        }
        try {
            Money.parse(priceTotal.getText());
        } catch (IllegalArgumentException e) {
            responseText.setText("Invalid Cost value. Prices should be of the form X.XX where X is a digit");
            isValid = false;
        } catch (ArithmeticException e) {
            responseText.setText("Restrict the Cost value to two digits after the decimal point.");
            isValid = false;
        }
        try {
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
            LocalTime.parse(time.getText(), timeFormatter);
        } catch (DateTimeException e) {
            responseText.setText("Time format should be HH:MM:SS.");
            isValid = false;
        }

        try {
            LocalDate.parse(date.getText());
        } catch (DateTimeException e) {
            responseText.setText("Date format should be dd LLLL yyyy.");
            isValid = false;
        }
        return isValid;
    }

}

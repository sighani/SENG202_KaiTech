package kaitech.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import kaitech.api.database.LoyaltyCardTable;
import kaitech.api.model.Business;
import kaitech.api.model.LoyaltyCard;
import kaitech.model.BusinessImpl;
import kaitech.model.LoyaltyCardImpl;

import java.time.LocalDate;

public class NewLoyaltyCardController {
    @FXML
    private TextField ownerName;

    @FXML
    private TextField cardID;

    @FXML
    private Text responseText;

    private LoyaltyCardTable cardTable;

    public void initialize() {
        Business business = BusinessImpl.getInstance();
        cardTable = business.getLoyaltyCardTable();
    }

    /**
     * This method is called when the confirm button is pressed in the new loyalty card screen, when it is clicked, it
     * checks if the fields are empty, and ensures that the code is unique, and ID value is integer.
     */

    public void confirmCard() {
        if (ownerName.getText().trim().length() == 0 || cardID.getText().trim().length() == 0) {
            responseText.setText("A field is blank.");
            responseText.setVisible(true);
        } else {
            try {
                String name = ownerName.getText();
                int id = Integer.parseInt(cardID.getText());
                LocalDate now = LocalDate.now();
                LoyaltyCard newCard = new LoyaltyCardImpl(id, now, name);
                cardTable.putLoyaltyCard(newCard);

                responseText.setText("Loyalty card for: " + name + ", has been added.  ");
                responseText.setVisible(true);
            } catch (NumberFormatException e) {
                responseText.setText("Please enter a purely integer value for the ID.");
                responseText.setVisible(true);
            } catch (RuntimeException e) {
                responseText.setText("That code already exists, please enter a unique code.");
                responseText.setVisible(true);


            }
        }
    }

    /**
     * Closes the current GUI screen.
     */

    public void exit() {
        Stage stage = (Stage) responseText.getScene().getWindow();
        stage.close();
    }
}

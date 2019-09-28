package kaitech.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import kaitech.api.database.MenuTable;
import kaitech.api.model.Business;
import kaitech.model.BusinessImpl;
import kaitech.model.MenuImpl;

public class NewMenuController {
    @FXML
    private TextField menuName;

    @FXML
    private TextArea menuDescription;

    @FXML
    private Text responseText;

    private MenuTable menuTable;

    public void initialize() {
        Business business = BusinessImpl.getInstance();
        menuTable = business.getMenuTable();
    }

    /**
     * This method is called when the confirm button is pressed on the menu form. It gets the relevant information
     * from the menu screen, and then creates a new menu, and adds this to an instance of Business. It also prints
     * informational feedback, so the user can see that the menu was successfully added.
     */

    public void confirmMenu() {
        if (menuName.getText().trim().length() == 0 || menuDescription.getText().trim().length() == 0) {
            responseText.setText("A field is blank.");
            responseText.setVisible(true);
        } else {
            String name = menuName.getText();
            MenuImpl newMenu = new MenuImpl(name, menuDescription.getText());
            menuTable.putMenu(newMenu);

            responseText.setText("Menu: " + name + ", has been added.  ");
            responseText.setVisible(true);
            System.out.println(menuTable.getAllMenuIDs());
        }
    }

    public void exit() {
        Stage stage = (Stage) responseText.getScene().getWindow();
        stage.close();
    }
}

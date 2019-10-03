package kaitech.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import kaitech.api.database.MenuItemTable;
import kaitech.api.model.Business;
import kaitech.api.model.Menu;
import kaitech.api.model.MenuItem;
import kaitech.model.BusinessImpl;
import kaitech.util.LambdaValueFactory;

import java.util.HashMap;
import java.util.Map;

public class ModifyMenuController {
    @FXML
    private TableView<MenuItem> table1;

    @FXML
    private TableColumn<MenuItem, String> codeCol1;

    @FXML
    private TableColumn<MenuItem, String> nameCol1;

    @FXML
    private TableView<MenuItem> table2;

    @FXML
    private TableColumn<MenuItem, String> codeCol2;

    @FXML
    private TableColumn<MenuItem, String> nameCol2;

    @FXML
    private Text titleText;

    @FXML
    private TextField nameTextField;

    @FXML
    private TextArea descTextArea;

    @FXML
    private Text responseText;

    /**
     * The Menu we are modifying
     */
    private Menu menu;

    /**
     * A copy of the Map of String codes to MenuItems. A copy is made as the user may change their mind and cancel, thus
     * the changes to the MenuItems in the Menu should not be saved.
     */
    private HashMap<String, MenuItem> menuItemsCopy;

    public void setMenu(Menu menu) {
        this.menu = menu;
        start();
    }

    public void start() {
        Business business = BusinessImpl.getInstance();
        MenuItemTable menuItemTable = business.getMenuItemTable();
        menuItemsCopy = new HashMap<>();
        for (Map.Entry<String, MenuItem> entry : menu.getMenuItems().entrySet()) {
            menuItemsCopy.put(entry.getKey(), entry.getValue());
        }

        nameTextField.setText(menu.getTitle());
        descTextArea.setText(menu.getDescription());

        titleText.setText("Now modifying Menu: " + menu.getTitle() + '(' + menu.getID() + ')');
        titleText.setVisible(true);
        codeCol1.setCellValueFactory(new LambdaValueFactory<>(MenuItem::getCode));
        nameCol1.setCellValueFactory(new LambdaValueFactory<>(MenuItem::getName));
        table1.setItems(FXCollections.observableArrayList(menuItemTable.resolveAllMenuItems().values()));

        codeCol2.setCellValueFactory(new LambdaValueFactory<>(MenuItem::getCode));
        nameCol2.setCellValueFactory(new LambdaValueFactory<>(MenuItem::getName));
        table2.setItems(FXCollections.observableArrayList(menuItemsCopy.values()));
    }

    /**
     * Adds the MenuItem to the HashMap copy
     */
    public void add() {
        if (table1.getSelectionModel().getSelectedItem() != null) {
            if (menuItemsCopy.size() >= 25) {
                responseText.setText("A menu can have a maximum of 25 items only.");
            }
            else {
                menuItemsCopy.put(table1.getSelectionModel().getSelectedItem().getCode(), table1.getSelectionModel().getSelectedItem());
                refreshTable();
            }
        }
    }

    /**
     * Removes the MenuItem from the HashMap copy
     */
    public void remove() {
        if (table2.getSelectionModel().getSelectedItem() != null) {
            menuItemsCopy.remove(table2.getSelectionModel().getSelectedItem().getCode());
            refreshTable();
        }
    }

    public void back() {
        if (fieldsAreValid()) {
            menu.setMenuItems(menuItemsCopy);
            menu.setTitle(nameTextField.getText());
            menu.setDescription(descTextArea.getText());
            Stage stage = (Stage) titleText.getScene().getWindow();
            stage.close();
        } else {
            responseText.setVisible(true);
        }
    }

    public void refreshTable() {
        table2.setItems(FXCollections.observableArrayList(menuItemsCopy.values()));
    }

    /**
     * Checks that the two text fields haven't been left empty.
     *
     * @return A boolean of whether everything is valid.
     */
    public boolean fieldsAreValid() {
        boolean isValid = true;
        if (nameTextField.getText().trim().length() == 0 ||
                descTextArea.getText().trim().length() == 0) {
            responseText.setText("One of the fields is empty");
            isValid = false;
        }
        return isValid;
    }
}

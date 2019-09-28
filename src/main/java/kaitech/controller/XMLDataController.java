package kaitech.controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import kaitech.api.model.Business;
import kaitech.api.model.Ingredient;
import kaitech.api.model.MenuItem;
import kaitech.api.model.Supplier;
import kaitech.io.LoadData;
import kaitech.model.BusinessImpl;
import kaitech.util.LambdaValueFactory;
import org.joda.money.format.MoneyFormatter;
import org.joda.money.format.MoneyFormatterBuilder;

import java.io.File;
import java.io.IOException;
import java.util.Map;

/**
 * The controller for the XML data entry screen, where the user can select and upload
 * an XML file to be parsed.
 */
public class XMLDataController {

    public Label lblInfo;
    public SplitPane SplitPaneMain;

    /**
     * Table view for displaying the data
     */
    @FXML
    private TableView<Supplier> supplierDisplayTable;
    @FXML
    private TableView<MenuItem> menuDisplayTable;
    @FXML
    private TableView<Ingredient> ingredientsDisplayTable;

    /**
     * Radio Buttons
     */
    @FXML
    private RadioButton rBIngredients;
    @FXML
    private RadioButton rBSuppliers;
    @FXML
    private RadioButton rBMenu;
    @FXML
    private ToggleGroup fileTypes;

    /**
     * Action Buttons
     */
    public Button fileChooserButton;
    public Button btnConfirm;
    public Button btnCancel;

    /**
     * Ingredients Table Columns
     */
    @FXML
    private TableColumn<Ingredient, String> codeCol;
    @FXML
    private TableColumn<Ingredient, String> nameIngCol;
    @FXML
    private TableColumn<Ingredient, String> unitTypeCol;
    @FXML
    private TableColumn<Ingredient, String> costCol;
    @FXML
    private TableColumn<Ingredient, String> vegCol;
    @FXML
    private TableColumn<Ingredient, String> veganCol;
    @FXML
    private TableColumn<Ingredient, String> gfCol;

    /**
     * Supplier table columns
     */
    @FXML
    private TableColumn<Supplier, String> idCol;
    @FXML
    private TableColumn<Supplier, String> nameSupCol;
    @FXML
    private TableColumn<Supplier, String> addressCol;
    @FXML
    private TableColumn<Supplier, String> phCol;
    @FXML
    private TableColumn<Supplier, String> phTypeCol;
    @FXML
    private TableColumn<Supplier, String> emailCol;
    @FXML
    private TableColumn<Supplier, String> urlCol;


    /**
     * Menu Items Columns
     */
    @FXML
    private TableColumn<MenuItem, String> codeItemCol;
    @FXML
    private TableColumn<MenuItem, String> nameItemCol;
    @FXML
    private TableColumn<MenuItem, String> priceCol;
    @FXML
    private TableColumn<MenuItem, String> typeCol;
    @FXML
    private TableColumn<MenuItem, String> recipeCol;

    /**
     * Error label
     */
    @FXML
    private Label lblError;

    /**
     * File Path String
     */
    String selectedFilePath;

    /**
     * The instance of the business.
     */
    private Business business;

    /**
     * A formatter for readable displaying of money.
     */
    private static final MoneyFormatter MONEY_FORMATTER = new MoneyFormatterBuilder() //
            .appendCurrencySymbolLocalized() //
            .appendAmountLocalized() //
            .toFormatter();

    @FXML
    public void initialize() {
        business = BusinessImpl.getInstance();
    }

    /**
     * This is the dialog to chose a file
     */
    public void openFile() {
        /*
         * Making all the tables invisible and setting the Error label to null
         */
        menuDisplayTable.setVisible(false);
        ingredientsDisplayTable.setVisible(false);
        supplierDisplayTable.setVisible(false);
        lblError.setVisible(false);

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select a XML file to upload");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("XML Files", "*.xml"));
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile == null) {
            return;
        }
        selectedFilePath = selectedFile.getAbsolutePath();

        //now we check if the right type of file has been uploaded

        if (fileTypes.getSelectedToggle().equals(rBIngredients)) {
            try {
                LoadData.LoadIngredients(selectedFilePath);
                setTableDataIngredients(LoadData.ingredientsList());
            } catch (Exception e) {
                //The wrong type of file or file error
                lblError.setVisible(true);
            }

        } else if (fileTypes.getSelectedToggle().equals(rBMenu)) {
            try {
                LoadData.loadMenu(selectedFilePath);
                setTableDataMenu(LoadData.menuItems());
            } catch (Exception e) {
                //The wrong type of file or file error
                lblError.setVisible(true);
            }
        } else if (fileTypes.getSelectedToggle().equals(rBSuppliers)) {
            try {
                LoadData.loadSuppliers(selectedFilePath);
                setTableDataSuppliers(LoadData.supplierList());
            } catch (Exception e) {
                //The wrong type of file or file error
                lblError.setVisible(true);
            }
        } else {
            //error (Should never happen but might as well have it here)
            lblError.setVisible(true);
            lblError.setText("Unknown Error, Please Contact KaiTech Support");
        }
    }

    /**
     * Setting columns to the corresponding supplier categories
     *
     * @param suppliersMap A map from a String to the Supplier
     */
    private void setTableDataSuppliers(Map<String, Supplier> suppliersMap) {
        idCol.setCellValueFactory(new LambdaValueFactory<>(Supplier::getId));
        nameSupCol.setCellValueFactory(new LambdaValueFactory<>(Supplier::getName));
        addressCol.setCellValueFactory(new LambdaValueFactory<>(Supplier::getAddress));
        phCol.setCellValueFactory(new LambdaValueFactory<>(Supplier::getPhone));
        phTypeCol.setCellValueFactory(new LambdaValueFactory<>(Supplier::getPhoneType));
        emailCol.setCellValueFactory(new LambdaValueFactory<>(Supplier::getEmail));
        urlCol.setCellValueFactory(new LambdaValueFactory<>(Supplier::getUrl));
        supplierDisplayTable.setItems(FXCollections.observableArrayList(suppliersMap.values()));
        supplierDisplayTable.setVisible(true);
    }

    private void setTableDataMenu(Map<String, MenuItem> menuItemMap) {
        //TODO Still needs some work (Displaying menu info....) LoadedData.menuLoaded or something
        codeItemCol.setCellValueFactory(new LambdaValueFactory<>(MenuItem::getCode));
        nameItemCol.setCellValueFactory(new LambdaValueFactory<>(MenuItem::getName));
        priceCol.setCellValueFactory(new LambdaValueFactory<>(e -> MONEY_FORMATTER.print(e.getPrice())));
        typeCol.setCellValueFactory(new LambdaValueFactory<>(MenuItem::getType));
        recipeCol.setCellValueFactory(cellData -> new SimpleStringProperty("Default"));

        menuDisplayTable.setItems(FXCollections.observableArrayList(menuItemMap.values()));
        menuDisplayTable.setVisible(true);
    }

    /**
     * Sets all column values to the correct corresponding values in the ingredient class and then
     * sets values to loaded ingredients
     *
     * @param ingredientsMap A map from Ingredients to Integers
     */
    private void setTableDataIngredients(Map<Ingredient, Integer> ingredientsMap) {
        codeCol.setCellValueFactory(new LambdaValueFactory<>(Ingredient::getCode));
        nameIngCol.setCellValueFactory(new LambdaValueFactory<>(Ingredient::getName));
        unitTypeCol.setCellValueFactory(new LambdaValueFactory<>(Ingredient::getUnit));
        costCol.setCellValueFactory(new LambdaValueFactory<>(e -> MONEY_FORMATTER.print(e.getPrice())));
        vegCol.setCellValueFactory(new LambdaValueFactory<>(Ingredient::getIsVeg));
        veganCol.setCellValueFactory(new LambdaValueFactory<>(Ingredient::getIsVegan));
        gfCol.setCellValueFactory(new LambdaValueFactory<>(Ingredient::getIsGF));

        ingredientsDisplayTable.setItems(FXCollections.observableArrayList(ingredientsMap.keySet()));
        ingredientsDisplayTable.setVisible(true);
    }


    public void addData() {
        if (!business.isLoggedIn()) {
            LogInController l = new LogInController();
            l.showScreen(null);
        } else {
            //Saves loaded data to the database in LoadData
            if (selectedFilePath == null) {
                Alert alert = new Alert(Alert.AlertType.WARNING,
                        "Please select a valid file, or hit cancel to return to the main menu.");
                alert.showAndWait();
                return;
            }
            if (fileTypes.getSelectedToggle().equals(rBIngredients)) {
                LoadData.saveIngredients();
            } else if (fileTypes.getSelectedToggle().equals(rBMenu)) {
                LoadData.saveMenu();
            } else if (fileTypes.getSelectedToggle().equals(rBSuppliers)) {
                LoadData.saveSuppliers();
            }
            //cleanup
            this.selectedFilePath = null;
            ingredientsDisplayTable.setVisible(false);
            menuDisplayTable.setVisible(false);
            supplierDisplayTable.setVisible(false);
        }
    }

    public void returnToMenu(ActionEvent event) throws IOException {
        try {
            // When exit button pressed
            Parent mainMenuParent = FXMLLoader.load(getClass().getResource("MainMenu.fxml"));
            Scene MainMenuScene = new Scene(mainMenuParent);

            // This line gets the Stage information, and returns to main menu
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setTitle("Main Menu");
            window.setScene(MainMenuScene);
            window.show();
        } catch (IOException e) {
            throw new IOException("Error in exiting XML loading.");
        }
    }
}

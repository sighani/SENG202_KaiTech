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
import javafx.stage.Modality;
import javafx.stage.Stage;
import kaitech.api.model.MenuItem;
import kaitech.api.model.*;
import kaitech.io.LoadData;
import kaitech.model.BusinessImpl;
import kaitech.util.LambdaValueFactory;
import org.joda.money.format.MoneyFormatter;
import org.joda.money.format.MoneyFormatterBuilder;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.util.List;
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
    @FXML
    private TableView<LoyaltyCard> cardDisplayTable;

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
    private RadioButton rbLoyaltyCards;
    @FXML
    private ToggleGroup fileTypes;

    /**
     * Action Buttons
     */
    public Button fileChooserButton;
    public Button btnConfirm;
    public Button btnCancel;

    /**
     * Loyalty card table coluumns
     */
    @FXML
    private TableColumn<LoyaltyCard, String> idCardCol;
    @FXML
    private TableColumn<LoyaltyCard, String> nameCardCol;
    @FXML
    private TableColumn<LoyaltyCard, String> dateCardCol;
    @FXML
    private TableColumn<LoyaltyCard, String> balanceCardCol;

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
    private String selectedFilePath;

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
        rBIngredients.setToggleGroup(fileTypes);
        rBMenu.setToggleGroup(fileTypes);
        rbLoyaltyCards.setToggleGroup(fileTypes);
        rBSuppliers.setToggleGroup(fileTypes);
    }

    /**
     * Called on openFile button press, opens a dialog to a file selector, and loads the
     * data (If it is valid) into temporary memory
     */
    public void openFile() {
        /*
         * Making all the tables invisible and setting the Error label to null
         */
        menuDisplayTable.setVisible(false);
        ingredientsDisplayTable.setVisible(false);
        supplierDisplayTable.setVisible(false);
        cardDisplayTable.setVisible(false);
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
                LoadData.loadIngredients(selectedFilePath);
                setTableDataIngredients(LoadData.getIngredientsLoaded());
            } catch (Exception e) {
                //The wrong type of file or file error
                lblError.setVisible(true);
            }

        } else if (fileTypes.getSelectedToggle().equals(rBMenu)) {
            try {
                LoadData.loadMenu(selectedFilePath);
                setTableDataMenu(LoadData.getMenuItemsLoaded());

                //here we need to check the recipe stuff


            } catch (SAXException | IOException e) {
                //The wrong type of file or file error
                lblError.setVisible(true);
            } catch (IllegalArgumentException iAE) {
                //ingredient error
                List<String> missingIng = LoadData.getMissingIngredients();
                Label ingredientErrorLabel = new Label("You have loaded menu items without these ingredients loaded: \n" + missingIng.toString() + "\n Would you like to manually enter them now?");
                ingredientErrorLabel.setWrapText(true);
                Alert alert = new Alert(Alert.AlertType.WARNING, "", ButtonType.YES, ButtonType.NO);
                alert.getDialogPane().setContent(ingredientErrorLabel);
                alert.showAndWait();

                if (alert.getResult() == ButtonType.YES) {
                    //they want to add the ingredients manually
                    try {
                        for (String s : missingIng) {
                            //opens a new "New Ingredient" screen for each missing code
                            FXMLLoader loaderTemp = new FXMLLoader(getClass().getResource("ingredient.fxml"));
                            Parent rootTemp = loaderTemp.load();
                            NewIngredientController controller = loaderTemp.getController();
                            controller.setComboBoxes();
                            controller.getIngredCode().setText(s);
                            Stage stageTemp = new Stage();
                            stageTemp.initModality(Modality.APPLICATION_MODAL);
                            stageTemp.setResizable(false);
                            stageTemp.setTitle("Add an ingredient");
                            stageTemp.setScene(new Scene(rootTemp));
                            stageTemp.show();
                        }
                    } catch (Exception e) {
                        //comment
                    }
                } else {
                    //they chose no
                    menuDisplayTable.setItems(null);
                    menuDisplayTable.setVisible(false);
                }
            }
        } else if (fileTypes.getSelectedToggle().equals(rBSuppliers)) {
            try {
                LoadData.loadSuppliers(selectedFilePath);
                setTableDataSuppliers(LoadData.getSuppliersLoaded());
            } catch (Exception e) {
                //The wrong type of file or file error
                lblError.setVisible(true);
            }
        } else if (fileTypes.getSelectedToggle().equals(rbLoyaltyCards)) {
            //chosen file type of loyalty cards
            try {
                LoadData.loadLoyaltyCards(selectedFilePath);
                setTableLoyaltyCards(LoadData.getLoyaltyCardsLoaded());
            } catch (Exception e) {
                lblError.setVisible(true);
            }
        } else {
            //error (Should never happen but might as well have it here)
            lblError.setVisible(true);
            lblError.setText("Unknown Error, Please Contact KaiTech Support");
        }
    }


    /**
     * When the radioButton for parsing menus is selected, warns the user to ensure
     * they have the correct ingredients loaded already
     */

    public void menuSelected() {
        ///pop up to warn user that they should ensure that all ingredients are uploaded first
        Label warningText = new Label("Make sure all ingredients are Loaded before loading menu items");
        warningText.setWrapText(true);
        Alert warningAlert = new Alert(Alert.AlertType.INFORMATION, "", ButtonType.OK);
        warningAlert.getDialogPane().setContent(warningText);
        warningAlert.showAndWait();
    }

    /**
     * Sets the table showing parsed loyalty card data to the required data, and
     * makes it visible
     * @param loyaltyCards A map from Integers to LoyaltyCards
     */

    private void setTableLoyaltyCards(Map<Integer, LoyaltyCard> loyaltyCards) {
        idCardCol.setCellValueFactory(new LambdaValueFactory<>(LoyaltyCard::getId));
        nameCardCol.setCellValueFactory(new LambdaValueFactory<>(LoyaltyCard::getCustomerName));
        dateCardCol.setCellValueFactory(new LambdaValueFactory<>(LoyaltyCard::getLastPurchase));
        balanceCardCol.setCellValueFactory(new LambdaValueFactory<>(e -> MONEY_FORMATTER.print(e.getBalance())));
        cardDisplayTable.setItems(FXCollections.observableArrayList(loyaltyCards.values()));
        cardDisplayTable.setVisible(true);
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

    /**
     * Sets the MenuItem table visible and populates it accordingly
     * @param menuItemMap The map of MenuItem String codes to MenuItems
     */

    private void setTableDataMenu(Map<String, MenuItem> menuItemMap) {
        //TODO Still needs some work (Displaying menu info....) LoadedData.menuLoaded or something
        codeItemCol.setCellValueFactory(new LambdaValueFactory<>(MenuItem::getCode));
        nameItemCol.setCellValueFactory(new LambdaValueFactory<>(MenuItem::getName));
        priceCol.setCellValueFactory(new LambdaValueFactory<>(e -> MONEY_FORMATTER.print(e.getPrice())));
        typeCol.setCellValueFactory(new LambdaValueFactory<>(MenuItem::getType));
        //same as the recipe viewer
        recipeCol.setCellValueFactory(cellData -> {
            StringBuilder ingredientsString = new StringBuilder();
            for (Map.Entry<Ingredient, Integer> entry : cellData.getValue().getRecipe().getIngredients().entrySet()) {
                ingredientsString.append(entry.getKey().getName()).append(": ").append(entry.getValue()).append(", ");
            }
            if (ingredientsString.length() > 0) {
                ingredientsString.deleteCharAt((ingredientsString.length() - 1));
                ingredientsString.deleteCharAt((ingredientsString.length() - 1));
            }
            return new SimpleStringProperty(ingredientsString.toString());
        });

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

    /**
     * Takes the data loaded into temporary memory and saves to the persistent storage in the database
     */
    public void addData() {
        if (!business.isLoggedIn()) {
            LogInController l = new LogInController();
            l.showScreen();
        } else {
            //Saves loaded data to the database in LoadData
            if (selectedFilePath == null) {
                Label noFileWarning = new Label("Please select a valid file, or hit cancel to return to the main menu.");
                noFileWarning.setWrapText(true);
                Alert alert = new Alert(Alert.AlertType.WARNING,
                        "");
                alert.getDialogPane().setContent(noFileWarning);
                alert.showAndWait();
                return;
            }
            if (fileTypes.getSelectedToggle().equals(rBIngredients)) {
                LoadData.saveIngredients(business);
            } else if (fileTypes.getSelectedToggle().equals(rBMenu)) {
                LoadData.saveMenu(business);
            } else if (fileTypes.getSelectedToggle().equals(rBSuppliers)) {
                LoadData.saveSuppliers(business);
            } else if (fileTypes.getSelectedToggle().equals(rbLoyaltyCards)) {
                LoadData.saveLoyaltyCards(business);
            }
            //cleanup
            this.selectedFilePath = null;
            ingredientsDisplayTable.setVisible(false);
            menuDisplayTable.setVisible(false);
            supplierDisplayTable.setVisible(false);
            cardDisplayTable.setVisible(false);
        }
    }

    /**
     * Takes user back to main menu screen, takes action event upon button press, and
     * throws an IOException if there is an error
     * @param event The event that triggered the screen change
     * @throws IOException if there are any issues changing screens
     */
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

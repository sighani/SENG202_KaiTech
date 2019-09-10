package kaitech.controller;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import kaitech.api.model.*;
import kaitech.api.model.MenuItem;
import kaitech.io.LoadData;
import kaitech.model.BusinessImpl;
import org.joda.money.format.MoneyFormatter;
import org.joda.money.format.MoneyFormatterBuilder;

import java.io.File;
import java.util.Map;

/**
 * The controller for the XML data entry screen, where the user can select and upload
 * an XML file to be parsed.
 */
public class XMLDataController {

    /**
     * Table view for displaying the data
     */
    @FXML
    private TableView supplierDisplayTable, menuDisplayTable, ingredientsDisplayTable;

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
    private TableColumn<MenuItem, String> recipieCol;

    /**
     * Error label
     */
    @FXML
    private Label lblError;

    /**
     * File Path String
     */
    String selectedFilePath;

    private Business business;

    /**
     * A formatter for readable displaying of money.
     */
    private static final MoneyFormatter MONEY_FORMATTER = new MoneyFormatterBuilder().appendCurrencySymbolLocalized().appendAmountLocalized().toFormatter();


    @FXML
    public void initialize(){business = BusinessImpl.getInstance();}

    //TODO UML CLASS DIAGRAM


    public void openFile(){
        /**
         * This is the dialog to chose a file
         */

        /**
         * Making all the tables invisible and setting the Error label to null
         */
        menuDisplayTable.setVisible(false);
        ingredientsDisplayTable.setVisible(false);
        supplierDisplayTable.setVisible(false);
        lblError.setText(null);

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select a XML file to upload");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("XML Files", "*.xml"));

        selectedFilePath = fileChooser.showOpenDialog(null).getAbsolutePath();



        //now we check if the right type of file has been uploaded

        if(fileTypes.getSelectedToggle().equals(rBIngredients)){
            try {
                LoadData.LoadIngredients(selectedFilePath);
                setTableDataIngredients(LoadData.ingredientsList());
            }catch(Error e){
                //The wrong type of file or file error
                lblError.setText("File invalid or wrong filetype selected, please try again");
            }
        }else if(fileTypes.getSelectedToggle().equals(rBMenu)){
            try {
                LoadData.loadMenu(selectedFilePath);
                setTableDataMenu(LoadData.menuItems());
            }catch(Error e){
                //The wrong type of file or file error
                lblError.setText("File invalid or wrong filetype selected, please try again");
            }
        }else if(fileTypes.getSelectedToggle().equals(rBSuppliers)){
            try {
                LoadData.loadSuppliers(selectedFilePath);
                setTableDataSuppliers(LoadData.supplierList());
            }catch(Error e){
                //The wrong type of file or file error
                lblError.setText("File invalid or wrong filetype selected, please try again");
            }
        }else{
            //error (Should never happen but might as well have it here)
            lblError.setText("Unknown Error, Please Contact KaiTech Support");
        }

    }

    private void setTableDataSuppliers(Map<String, Supplier> suppliersMap){
        /**
         * Setting columns to the corresponding supplier categories
         */

        //TODO Id col isnt working FIXIT

        idCol.setCellValueFactory(new PropertyValueFactory<>("sid"));
        nameSupCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        addressCol.setCellValueFactory(new PropertyValueFactory<>("address"));
        phCol.setCellValueFactory(new PropertyValueFactory<>("phone"));
        phTypeCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPhoneType().toString()));
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        urlCol.setCellValueFactory(new PropertyValueFactory<>("url"));
        supplierDisplayTable.setItems(FXCollections.observableArrayList(suppliersMap.values()));
        supplierDisplayTable.setVisible(true);
    }

    private void setTableDataMenu(Map<String, MenuItem> menuItemMap){
        //TODO Still needs some work (Displaying menu info....) LoadedData.menuLoaded or something
        codeItemCol.setCellValueFactory(new PropertyValueFactory<>("code"));
        nameItemCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        priceCol.setCellValueFactory(cellData -> new SimpleStringProperty(MONEY_FORMATTER.print(cellData.getValue().getPrice())));
        typeCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().toString()));
        recipieCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().toString()));

        menuDisplayTable.setItems(FXCollections.observableArrayList(menuItemMap.values()));
        menuDisplayTable.setVisible(true);

    }

    private void setTableDataIngredients(Map<String, Ingredient> ingredientsMap){
        /**
         * Sets all column values to the correct corresponding values in the ingredient class and then
         * sets values to loaded ingredients
         */
        codeCol.setCellValueFactory(new PropertyValueFactory<>("code"));
        nameIngCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        unitTypeCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getUnit().toString()));
        costCol.setCellValueFactory(cellData -> new SimpleStringProperty(MONEY_FORMATTER.print(cellData.getValue().getPrice())));
        vegCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getIsVeg().toString()));
        veganCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getIsVegan().toString()));
        gfCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getIsGF().toString()));

        ingredientsDisplayTable.setItems(FXCollections.observableArrayList(ingredientsMap.values()));
        ingredientsDisplayTable.setVisible(true);
    }


    public void addData(){
        //Saves loaded data to the database in LoadData
        if(fileTypes.getSelectedToggle().equals(rBIngredients)){
            LoadData.saveIngredients();
        }else if(fileTypes.getSelectedToggle().equals(rBMenu)){
            LoadData.saveMenu();
        }else if(fileTypes.getSelectedToggle().equals(rBSuppliers)){
            LoadData.saveSuppliers();
        }
        //cleanup
        this.selectedFilePath = null;
        ingredientsDisplayTable.setVisible(false);
        menuDisplayTable.setVisible(false);
        supplierDisplayTable.setVisible(false);

    }

}

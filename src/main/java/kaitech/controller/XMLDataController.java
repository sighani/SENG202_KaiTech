package kaitech.controller;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import kaitech.api.model.*;
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
     * Buisness instsnce
     */
    private Business business;

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
     * File Path String
     */
    String selectedFilePath;


    /**
     * A formatter for readable displaying of money.
     */
    private static final MoneyFormatter MONEY_FORMATTER = new MoneyFormatterBuilder().appendCurrencySymbolLocalized().appendAmountLocalized().toFormatter();


    @FXML
    public void initialize(){
        business = BusinessImpl.getInstance();
    }

    public void openFile(){
        /**
         * This is the dialog to chose a file
         */

        /**
         * Making all the tables invisible
         */
        menuDisplayTable.setVisible(false);
        ingredientsDisplayTable.setVisible(false);
        supplierDisplayTable.setVisible(false);

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select a XML file to upload");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("XML Files", "*.xml"));

        selectedFilePath = fileChooser.showOpenDialog(null).getAbsolutePath();



        //now we check if the right type of file has been uploaded

        if(fileTypes.getSelectedToggle().equals(rBIngredients)){
            LoadData.LoadIngredients(selectedFilePath);
            setTableDataIngredients(LoadData.ingredientsList());
        }else if(fileTypes.getSelectedToggle().equals(rBMenu)){
            LoadData.loadMenu(selectedFilePath);
            setTableDataMenu(LoadData.menuItems());
        }else if(fileTypes.getSelectedToggle().equals(rBSuppliers)){
            LoadData.loadSuppliers(selectedFilePath);
            setTableDataSuppliers(LoadData.supplierList());
        }else{
            //error
            System.out.println("ERR");
        }

    }

    private void setTableDataSuppliers(Map<String, Supplier> suppliersMap){
        /**
         * Setting columns to the corresponding supplier categories
         */

        //TODO Id col isnt working

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
        //TODO Still needs some work
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
        //TODO link up database (in load data i reckon)
    }

}

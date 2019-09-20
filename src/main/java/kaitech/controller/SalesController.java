package kaitech.controller;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import kaitech.api.database.InventoryTable;
import kaitech.api.model.Business;
import kaitech.api.model.Ingredient;
import kaitech.api.model.MenuItem;
import kaitech.api.model.Recipe;
import kaitech.model.BusinessImpl;
import kaitech.model.IngredientImpl;
import kaitech.model.MenuItemImpl;
import kaitech.model.RecipeImpl;
import kaitech.util.LambdaValueFactory;
import kaitech.util.ThreeValueLogic;
import kaitech.util.UnitType;
import org.joda.money.Money;
import org.joda.money.format.MoneyFormatter;
import org.joda.money.format.MoneyFormatterBuilder;

import java.io.IOException;
import java.util.*;


public class SalesController {

    private Business business;

    private HashMap<MenuItem, Integer> itemsOrdered;

    @FXML
    private GridPane gridPaneItems;

    @FXML
    private TableView<MenuItem> orderTable;

    @FXML
    private TableColumn<MenuItem, String> nameCol;

    @FXML
    private TableColumn<MenuItem, String> costCol;

    @FXML
    private TableColumn<MenuItem, Button> removeCol;

    @FXML
    private TableColumn<MenuItem, Number> quantityCol;

    @FXML
    private Button eftposButton;

    @FXML
    private Button prevOrderButton;

    @FXML
    private Button exitButton;

    @FXML
    private Button cancelButton;

    @FXML
    private Button managerTaskButton;

    @FXML
    private Button confirmButton;

    @FXML
    private Button cashButton;

    private MenuItem testItem;

    /**
     * A formatter for readable displaying of money.
     */
    private static final MoneyFormatter MONEY_FORMATTER = new MoneyFormatterBuilder().appendCurrencySymbolLocalized().appendAmountLocalized().toFormatter();


    @FXML
    public void initialize() {
        this.gridPaneItems = new GridPane();
        business = BusinessImpl.getInstance();
        itemsOrdered = new HashMap<>();
        InventoryTable inventoryTable = business.getInventoryTable();
        Money newIngPrice = Money.parse("NZD 0.30");
        Ingredient newIng1 = new IngredientImpl("Cheese Slice", "Cheese", UnitType.COUNT, newIngPrice, ThreeValueLogic.YES, ThreeValueLogic.NO, ThreeValueLogic.NO);
        Ingredient newIng2 = new IngredientImpl("Bacon Strip", "Bacon", UnitType.COUNT, newIngPrice, ThreeValueLogic.NO, ThreeValueLogic.NO, ThreeValueLogic.UNKNOWN);
        inventoryTable.putInventory(newIng1, 30);
        inventoryTable.putInventory(newIng2, 50);
        Map<Ingredient, Integer> ingredientsMap = new HashMap<>();
        ingredientsMap.put(newIng1, 1);
        Recipe testRecipe = new RecipeImpl(2, 10, 1, ingredientsMap);
        ArrayList<String> ingredientNames = new ArrayList<>();
        ingredientNames.add(newIng1.getName());
        Money price = Money.parse("NZD 6");
        testItem = new MenuItemImpl("B1", "Cheese Burger", testRecipe, price, ingredientNames);
        business.getMenuItemTable().getOrAddItem(testItem);
        nameCol.setCellValueFactory(new LambdaValueFactory<>(MenuItem::getName));
        costCol.setCellValueFactory(new LambdaValueFactory<>(e -> MONEY_FORMATTER.print(e.getPrice())));
        quantityCol.setCellValueFactory(cellData -> new SimpleIntegerProperty((itemsOrdered.get(cellData.getValue()))));
        removeCol.setCellFactory(ActionButtonTableCell_SalesController.forTableColumn("Remove", foodItem -> {
            // You can put whatever logic in here, or even open a new window.
            // For example here we'll just toggle the isGf
            //orderTable.setGlutenFree(!foodItem.isGlutenFree());
            orderTable.getItems().remove(foodItem);
            itemsOrdered.remove(foodItem);
            orderTable.refresh(); // Have to trigger a table refresh to make it show up in the table
        }));
        orderTable.setItems(FXCollections.observableArrayList(itemsOrdered.keySet()));

        int rowIndex = 0;
        int colIndex = 0;

        for(MenuItem menuItem : business.getMenuItemTable().resolveAllMenuItems().values()){
            Button tempButton = (Button) gridPaneItems.getChildren().get(0);
            tempButton.setVisible(true);
            tempButton.setText(menuItem.getName());
            tempButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    addToOrder(menuItem);
                }
            });
            System.out.println(tempButton.toString());
            /*
            gridPaneItems.add(tempButton, rowIndex, colIndex);
            if(rowIndex == 3){
                rowIndex = 0;
                colIndex++;
            }else{
                rowIndex++;
            }
            */
            //https://teamtreehouse.com/community/javafx-dynamically-adding-buttons-and-calling-setonaction-on-it
        }
        gridPaneItems.setVisible(true);
        System.out.println(gridPaneItems.getChildren());
    }

    public void addToOrder(MenuItem menuItem){
        System.out.println("BURGER");
    }



    /**
     * @param event cancel button pressed
     * @throws IOException print error
     */
    public void exitSalesScreen(ActionEvent event) throws IOException {
        try {
            //When logout button pressed, from home screen
            Parent mainMenuParent = FXMLLoader.load(getClass().getResource("MainMenu.fxml"));
            Scene MainMenuScene = new Scene(mainMenuParent);

            //This line gets the Stage information
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(MainMenuScene);
            window.show();
        } catch (IOException e) {
            throw new IOException("Error in exiting sales screen.");
        }

    }

    /**
     * When the records button is pressed, open the records screen.
     *
     * @param event when the records button on the main menu gets pressed.
     * @throws IOException prints an error message
     */
    public void prevOrder(ActionEvent event) throws IOException {
        try {
            //When sales button pressed, from home screen, get sales scene
            Parent recordsParent = FXMLLoader.load(getClass().getResource("records.fxml"));
            Scene recordsScene = new Scene(recordsParent);

            //Get stage info and switch scenes.
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(recordsScene);
            window.show();
        } catch (IOException e) {
            throw new IOException("Error in opening records screen.");
        }
    }

    public void burger() {

        if (itemsOrdered.containsKey(testItem)) {
            itemsOrdered.put(testItem, itemsOrdered.get(testItem) + 1);
            orderTable.refresh();
        } else {
            itemsOrdered.put(testItem, 1);
        }
        orderTable.setItems(FXCollections.observableArrayList(itemsOrdered.keySet()));
        System.out.println(itemsOrdered.get(testItem));
    }


    /**
     * Method stub to get it running.
     * TODO: IMPLEMENT!!!
     */
    public void eftposPayment() {

    }

    /**
     * Method stub to get it running.
     * TODO: IMPLEMENT!!!
     */
    public void cashPayment() {

    }

    /**
     * Method stub to get it running.
     * TODO: IMPLEMENT!!!
     */
    public void openManagerTasks() {

    }

    /**
     * Method stub to get it running.
     * TODO: IMPLEMENT!!!
     */
    public void confirmOrder() {

    }
}
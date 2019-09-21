package kaitech.controller;


import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import kaitech.api.database.MenuItemTable;
import kaitech.api.database.MenuTable;
import kaitech.api.model.Business;
import kaitech.api.model.Ingredient;
import kaitech.api.model.MenuItem;
import kaitech.model.BusinessImpl;
import kaitech.model.IngredientImpl;
import kaitech.model.MenuItemImpl;
import kaitech.model.RecipeImpl;
import kaitech.util.LambdaValueFactory;
import kaitech.util.MenuItemType;
import kaitech.util.ThreeValueLogic;
import kaitech.util.UnitType;
import org.joda.money.Money;
import org.joda.money.format.MoneyFormatter;
import org.joda.money.format.MoneyFormatterBuilder;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The controller for menu where the user can view and click on each MenuItem in a
 * menu.
 */
public class MenuItemController {
    @FXML
    private TableView<MenuItem> table;

    @FXML
    private TableColumn<MenuItem, String> codeCol;

    @FXML
    private TableColumn<MenuItem, String> nameCol;

    @FXML
    private TableColumn<MenuItem, String> typeCol;

    @FXML
    private TableColumn<MenuItem, String> priceCol;

    @FXML
    private TableColumn<MenuItem, String> stockCol;

    @FXML
    private TableColumn<MenuItem, String> veganCol;

    @FXML
    private TableColumn<MenuItem, String> vegeCol;

    @FXML
    private TableColumn<MenuItem, String> gfCol;

    private Business business;
    private MenuItemTable menuItemTable;

    /**
     * A formatter for readable displaying of money.
     */
    private static final MoneyFormatter MONEY_FORMATTER = new MoneyFormatterBuilder().appendCurrencySymbolLocalized().appendAmountLocalized().toFormatter();

    @FXML
    public void initialize() {
        business = BusinessImpl.getInstance();
        //BusinessImpl.reset();
       // business = BusinessImpl.getInstance();
        menuItemTable = business.getMenuItemTable();

        //Sample price created
        Money samplePrice = Money.parse("NZD 5.00");
        //Sample ingredient list created
        Ingredient sampleIngredient = new IngredientImpl("Cheese Slice", "Cheese", UnitType.COUNT, samplePrice, ThreeValueLogic.YES, ThreeValueLogic.NO, ThreeValueLogic.NO);
        List<String> sampleIngredientList = new ArrayList<String>();
        sampleIngredientList.add(sampleIngredient.getName());
        //Sample Empty recipe created
        Map<Ingredient, Integer> RecipeIngredients = new HashMap<>();
        RecipeImpl sampleRecipe = new RecipeImpl(RecipeIngredients);
        //Sample adding of menu items for manually testing
        MenuItem BB1 = createBB1();
        MenuItem CF = new MenuItemImpl("CFR", "Chicken Fried Rice",  samplePrice, sampleRecipe, MenuItemType.ASIAN, sampleIngredientList);
        MenuItem CAV = new MenuItemImpl("CAV", "Coq au van", samplePrice, sampleRecipe, MenuItemType.MAIN, sampleIngredientList);
        MenuItem Lem = new MenuItemImpl("Lem", "LemCan", samplePrice, sampleRecipe, MenuItemType.BEVERAGE, sampleIngredientList);
        MenuItem BF = new MenuItemImpl("BF", "Baby face", samplePrice, sampleRecipe, MenuItemType.COCKTAIL, sampleIngredientList);
        MenuItem HotChoc = new MenuItemImpl("HotChoc", "Hot Chocolate", samplePrice, sampleRecipe, MenuItemType.BEVERAGE, sampleIngredientList);
        menuItemTable.putMenuItem(BB1);
        menuItemTable.putMenuItem(CF);
        menuItemTable.putMenuItem(CAV);
        menuItemTable.putMenuItem(Lem);
        menuItemTable.putMenuItem(BF);
        menuItemTable.putMenuItem(HotChoc);


        codeCol.setCellValueFactory(new LambdaValueFactory<>(MenuItem::getCode));
        nameCol.setCellValueFactory(new LambdaValueFactory<>(MenuItem::getName));
        typeCol.setCellValueFactory(new LambdaValueFactory<>(MenuItem::getType));
        priceCol.setCellValueFactory(new LambdaValueFactory<>(MenuItem::getPrice));
        stockCol.setCellValueFactory(cellData -> new SimpleStringProperty(Integer.toString(cellData.getValue().calculateNumServings(business))));
        veganCol.setCellValueFactory(new LambdaValueFactory<>(MenuItem::getIsVegan));
        vegeCol.setCellValueFactory(new LambdaValueFactory<>(MenuItem::getIsVeg));
        gfCol.setCellValueFactory(new LambdaValueFactory<>(MenuItem::getIsGF));


        table.setItems(FXCollections.observableArrayList(menuItemTable.resolveAllMenuItems().values()));
    }

    /**
     * Removes the selected menu item from the table and refreshes the table.
     */
    public void delete() {
        menuItemTable.removeMenuItem(table.getSelectionModel().getSelectedItem().getCode());
        table.setItems(FXCollections.observableArrayList(menuItemTable.resolveAllMenuItems().values()));
    }

    /**
     * Launches the modify menu item screen. Upon closing the popup, the table is refreshed. The clicked ingredient
     * is passed to the popup via a setter.
     */
    public void modify() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("modifyMenuItem.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.setTitle("Modify Menu Item");
            stage.setScene(new Scene(root));
            stage.show();
            ModifyMenuItemController controller = loader.getController();
            controller.setMenuItem(table.getSelectionModel().getSelectedItem());
            stage.setOnHiding(paramT -> {
                table.getColumns().get(0).setVisible(false);
                table.getColumns().get(0).setVisible(true);
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void exit(ActionEvent event){
        try {
            Parent mainMenuParent = FXMLLoader.load(getClass().getResource("MainMenu.fxml"));
            Scene MainMenuScene = new Scene(mainMenuParent);

            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setTitle("Main Menu");
            window.setScene(MainMenuScene);
            window.show();

        } catch (IOException e) {
            System.err.println("Error exiting MenuItem Controller: " + e);
        }
    }

    public MenuItem createBB1(){
        Money bb1Price = Money.parse("NZD 5.00");
        Money samplePrice = Money.parse("NZD 0.00");

        //Ingredient list created
        Ingredient BBun = new IngredientImpl("BBun", "Hamburger bun", UnitType.COUNT, samplePrice, ThreeValueLogic.UNKNOWN, ThreeValueLogic.UNKNOWN, ThreeValueLogic.NO);
        Ingredient BPat = new IngredientImpl("BPat", "Beef patty", UnitType.COUNT, samplePrice, ThreeValueLogic.NO, ThreeValueLogic.NO, ThreeValueLogic.UNKNOWN);
        Ingredient Onion = new IngredientImpl("Onion", "Diced Onion", UnitType.GRAM, samplePrice, ThreeValueLogic.YES, ThreeValueLogic.UNKNOWN, ThreeValueLogic.UNKNOWN);
        Ingredient tSauce = new IngredientImpl("TSauce", "Tomato Sauce", UnitType.ML, samplePrice, ThreeValueLogic.UNKNOWN, ThreeValueLogic.UNKNOWN, ThreeValueLogic.UNKNOWN);
        Ingredient Beetroot = new IngredientImpl("Beetroot", "Beetroot Slice", UnitType.COUNT, samplePrice, ThreeValueLogic.UNKNOWN, ThreeValueLogic.UNKNOWN, ThreeValueLogic.YES);
        Ingredient Lettuce = new IngredientImpl("Lettuce", "Sliced Iceberg lettuce", UnitType.GRAM, samplePrice, ThreeValueLogic.UNKNOWN, ThreeValueLogic.UNKNOWN, ThreeValueLogic.UNKNOWN);
        Ingredient Mayo = new IngredientImpl("Mayo", "Eater plain Mayonnaise", UnitType.ML, samplePrice, ThreeValueLogic.UNKNOWN, ThreeValueLogic.UNKNOWN, ThreeValueLogic.UNKNOWN);

        List<String> bb1IngredientList = new ArrayList<String>();
        bb1IngredientList.add(BBun.getName());
        bb1IngredientList.add(BPat.getName());
        bb1IngredientList.add(Onion.getName());
        bb1IngredientList.add(tSauce.getName());
        bb1IngredientList.add(Beetroot.getName());
        bb1IngredientList.add(Lettuce.getName());
        bb1IngredientList.add(Mayo.getName());
        //Recipe created
        Map<Ingredient, Integer> RecipeIngredients = new HashMap<>();
        RecipeIngredients.put(BBun, 1);
        RecipeIngredients.put(BPat, 1);
        RecipeIngredients.put(Onion, 50);
        RecipeIngredients.put(tSauce, 5);
        RecipeIngredients.put(Beetroot, 1);
        RecipeIngredients.put(Lettuce, 15);
        RecipeIngredients.put(Mayo, 5);
        //Menu item created
        RecipeImpl bb1Recipe = new RecipeImpl(2, 5, 1, RecipeIngredients);
        MenuItem BB1 = new MenuItemImpl("BB1", "Beef Burger", bb1Price, bb1Recipe, MenuItemType.GRILL, bb1IngredientList);
        return BB1;
    }
}

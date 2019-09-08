package kaitech.controller;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import kaitech.api.database.InventoryTable;
import kaitech.api.model.Business;
import kaitech.api.model.Ingredient;
import kaitech.model.BusinessImpl;
import kaitech.model.IngredientImpl;
import kaitech.util.ThreeValueLogic;
import kaitech.util.UnitType;
import org.joda.money.Money;
import org.joda.money.format.MoneyFormatter;
import org.joda.money.format.MoneyFormatterBuilder;

import java.io.IOException;

/**
 * The controller for the inventory screen for displaying the current inventory
 * and stock of the business.
 */
public class InventoryController {

    @FXML
    private TableView<Ingredient> table;

    @FXML
    private TableColumn<Ingredient, String> codeCol;

    @FXML
    private TableColumn<Ingredient, String> nameCol;

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

    @FXML
    private TableColumn<Ingredient, Number> quantityCol;

    private Business business;
    private InventoryTable inventoryTable;

    /**
     * A formatter for readable displaying of money.
     */
    private static final MoneyFormatter MONEY_FORMATTER = new MoneyFormatterBuilder().appendCurrencySymbolLocalized().appendAmountLocalized().toFormatter();

    @FXML
    public void initialize() {
        business = BusinessImpl.getInstance();
        inventoryTable = business.getInventoryTable();
//      Quick test:

        Money newIngPrice = Money.parse("NZD 0.30");
        Ingredient newIng1 = new IngredientImpl("Cheese Slice", "Cheese", UnitType.COUNT, newIngPrice, ThreeValueLogic.YES, ThreeValueLogic.NO, ThreeValueLogic.NO);
        Ingredient newIng2 = new IngredientImpl("Bacon Strip", "Bacon", UnitType.COUNT, newIngPrice, ThreeValueLogic.NO, ThreeValueLogic.NO, ThreeValueLogic.UNKNOWN);
        inventoryTable.updateQuantity(newIng1, 30);
        inventoryTable.updateQuantity(newIng2, 50);

        codeCol.setCellValueFactory(new PropertyValueFactory<>("code"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        unitTypeCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getUnit().toString()));
        costCol.setCellValueFactory(cellData -> new SimpleStringProperty(MONEY_FORMATTER.print(cellData.getValue().getPrice())));
        vegCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getIsVeg().toString()));
        veganCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getIsVegan().toString()));
        gfCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getIsGF().toString()));
        quantityCol.setCellValueFactory(cellData -> new SimpleIntegerProperty(inventoryTable.getIngredientQuantity(cellData.getValue())));

        table.setItems(FXCollections.observableArrayList(business.getIngredientTable().resolveAllIngredients().values()));
    }

    /**
     * Removes the selected ingredient from the table and refreshes the table.
     */
    public void delete() {
        inventoryTable.removeInventory(table.getSelectionModel().getSelectedItem());
        table.setItems(FXCollections.observableArrayList(business.getIngredientTable().resolveAllIngredients().values()));
    }

    /**
     * Launches the modify ingredient screen. Upon closing the popup, the table is refreshed. The clicked ingredient
     * is passed to the popup via a setter.
     */
    public void modify() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("modifyIngredient.fxml"));
            Parent root = loader.load();
            ModifyIngredientController controller = loader.<ModifyIngredientController>getController();
            controller.setIngredient(table.getSelectionModel().getSelectedItem());
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.setTitle("Modify Ingredient details");
            stage.setScene(new Scene(root));
            stage.show();
            stage.setOnHiding(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent paramT) {
                    table.getColumns().get(0).setVisible(false);
                    table.getColumns().get(0).setVisible(true);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {

        }
    }
}

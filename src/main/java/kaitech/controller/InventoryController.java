package kaitech.controller;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import kaitech.api.database.IngredientTable;
import kaitech.api.database.InventoryTable;
import kaitech.api.model.Business;
import kaitech.api.model.Ingredient;
import kaitech.model.BusinessImpl;
import kaitech.util.LambdaValueFactory;
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
    private IngredientTable ingredientTable;

    /**
     * The ingredient that the user is trying to delete.
     */
    private static Ingredient selectedIngredient;

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
        inventoryTable = business.getInventoryTable();
        ingredientTable = business.getIngredientTable();

        codeCol.setCellValueFactory(new LambdaValueFactory<>(Ingredient::getCode));
        nameCol.setCellValueFactory(new LambdaValueFactory<>(Ingredient::getName));
        unitTypeCol.setCellValueFactory(new LambdaValueFactory<>(Ingredient::getUnit));
        costCol.setCellValueFactory(new LambdaValueFactory<>(e -> MONEY_FORMATTER.print(e.getPrice())));
        vegCol.setCellValueFactory(new LambdaValueFactory<>(Ingredient::getIsVeg));
        veganCol.setCellValueFactory(new LambdaValueFactory<>(Ingredient::getIsVegan));
        gfCol.setCellValueFactory(new LambdaValueFactory<>(Ingredient::getIsGF));
        quantityCol.setCellValueFactory(cellData -> new SimpleIntegerProperty(inventoryTable.getOrAddQuantity(cellData.getValue())));
        table.setItems(FXCollections.observableArrayList(business.getIngredientTable().resolveAllIngredients().values()));
    }

    /**
     * Removes the selected ingredient from the table and refreshes the table.
     */
    public void delete() {
        if (table.getSelectionModel().getSelectedItem() != null) {
            if (business.getAffectedMenuItems(table.getSelectionModel().getSelectedItem()).size() > 0) {
                try {
                    if (!business.isLoggedIn()) {
                        LogInController l = new LogInController();
                        l.showScreen("modifyIngredient.fxml");
                    } else {
                        selectedIngredient = ingredientTable.getOrAddIngredient(table.getSelectionModel().getSelectedItem());
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("deleteIngredientWarning.fxml"));
                        Parent root = loader.load();
                        Stage stage = new Stage();
                        stage.initModality(Modality.APPLICATION_MODAL);
                        stage.setResizable(false);
                        stage.setTitle("Warning");
                        stage.setScene(new Scene(root));
                        stage.show();
                        stage.setOnHiding(paramT -> table.setItems(FXCollections.observableArrayList(business
                                .getIngredientTable().resolveAllIngredients().values())));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                inventoryTable.removeInventory(table.getSelectionModel().getSelectedItem());
                business.getIngredientTable().removeIngredient(table.getSelectionModel().getSelectedItem().getCode());
                table.setItems(FXCollections.observableArrayList(business.getIngredientTable().resolveAllIngredients().values()));
            }
        }
    }

    /**
     * Launches the modify ingredient screen. Upon closing the popup, the table is refreshed. The clicked ingredient
     * is passed to the popup via a setter.
     */
    public void modify() {
        if (table.getSelectionModel().getSelectedItem() != null) {
            try {
                if (!business.isLoggedIn()) {
                    LogInController l = new LogInController();
                    l.showScreen("modifyIngredient.fxml");
                } else {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("modifyIngredient.fxml"));
                    Parent root = loader.load();
                    Stage stage = new Stage();
                    stage.initModality(Modality.APPLICATION_MODAL);
                    stage.setResizable(false);
                    stage.setTitle("Modify Ingredient details");
                    stage.setScene(new Scene(root));
                    stage.show();
                    ModifyIngredientController controller = loader.getController();
                    controller.setIngredient(ingredientTable.getOrAddIngredient(table.getSelectionModel().getSelectedItem()));
                    stage.setOnHiding(paramT -> {
                        table.getColumns().get(0).setVisible(false);
                        table.getColumns().get(0).setVisible(true);
                    });
                }
            } catch (IOException | NullPointerException e) {
                e.printStackTrace();
            }
        }
    }

    public void back(ActionEvent event) throws IOException {
        try {
            Parent mainMenuParent = FXMLLoader.load(getClass().getResource("MainMenu.fxml"));
            Scene MainMenuScene = new Scene(mainMenuParent);

            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setTitle("Main Menu");
            window.setScene(MainMenuScene);
            window.show();

        } catch (IOException e) {
            throw new IOException("Error in exiting manual input.");
        }
    }

    /**
     * Allows the IngredientWarningController to obtain the ingredient that we want to delete.
     *
     * @return The Ingredient object to delete.
     */
    public static Ingredient getSelectedIngredient() {
        return selectedIngredient;
    }
}

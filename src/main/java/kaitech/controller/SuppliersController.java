package kaitech.controller;

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
import kaitech.api.database.SupplierTable;
import kaitech.api.model.Business;
import kaitech.api.model.Supplier;
import kaitech.model.BusinessImpl;
import kaitech.util.LambdaValueFactory;

import java.io.IOException;

/**
 * The controller for the supplier screen, which is a list of all the suppliers.
 * Can add/delete/modify suppliers provided the user is logged in.
 */
public class SuppliersController {
    @FXML
    private TableView<Supplier> table;

    @FXML
    private TableColumn<Supplier, String> idCol;

    @FXML
    private TableColumn<Supplier, String> nameCol;

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

    private Business business;
    private SupplierTable supplierTable;

    @FXML
    public void initialize() {
        business = BusinessImpl.getInstance();
        supplierTable = business.getSupplierTable();

        idCol.setCellValueFactory(new LambdaValueFactory<>(Supplier::getId));
        nameCol.setCellValueFactory(new LambdaValueFactory<>(Supplier::getName));
        addressCol.setCellValueFactory(new LambdaValueFactory<>(Supplier::getAddress));
        phCol.setCellValueFactory(new LambdaValueFactory<>(Supplier::getPhone));
        phTypeCol.setCellValueFactory(new LambdaValueFactory<>(Supplier::getPhoneType));
        emailCol.setCellValueFactory(new LambdaValueFactory<>(Supplier::getEmail));
        urlCol.setCellValueFactory(new LambdaValueFactory<>(Supplier::getUrl));

        // Resolve all suppliers returns a copied list, this will need to be modified to support lazy loading
        table.setItems(FXCollections.observableArrayList(supplierTable.resolveAllSuppliers().values()));
    }

    /**
     * Launches the modify supplier screen. Upon closing the popup, the table is refreshed. The clicked supplier
     * is passed to the popup via a setter.
     */
    public void modify() {
        if (table.getSelectionModel().getSelectedItem() != null) {
            try {
                if (!business.isLoggedIn()) {
                    LogInController l = new LogInController();
                    l.showScreen();
                } else {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("modifySupplier.fxml"));
                    Parent root = loader.load();
                    Stage stage = new Stage();
                    stage.initModality(Modality.APPLICATION_MODAL);
                    stage.setResizable(false);
                    stage.setTitle("Modify Supplier details");
                    stage.setScene(new Scene(root));
                    stage.show();
                    ModifySupplierController controller = loader.getController();
                    controller.setSupplier(supplierTable.getOrAddSupplier(table.getSelectionModel().getSelectedItem()));
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

    /**
     * Removes the selected supplier from the table and refreshes the table.
     */
    public void delete() {
        if (table.getSelectionModel().getSelectedItem() != null) {
            if (!business.isLoggedIn()) {
                LogInController l = new LogInController();
                l.showScreen();
            } else {
                supplierTable.removeSupplier(table.getSelectionModel().getSelectedItem().getId());
                table.setItems(FXCollections.observableArrayList(supplierTable.resolveAllSuppliers().values()));
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
}

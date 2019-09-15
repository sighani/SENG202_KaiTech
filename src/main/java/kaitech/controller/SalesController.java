package kaitech.controller;

import javafx.beans.property.ReadOnlyObjectWrapper;
import kaitech.api.model.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import kaitech.model.BusinessImpl;

import org.joda.money.Money;
import java.io.IOException;


public class SalesController {

    private Business business;
    private MenuItem menuItem;

    @FXML
    private TableView<MenuItem> orderTable;

    @FXML
    private TableColumn<MenuItem, String> nameCol;

    @FXML
    private TableColumn<MenuItem, String> costCol;

    @FXML
    private TableColumn editCol;

    @FXML
    private TableColumn removeCol;

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
    private Ingredient testIngredient;

    @FXML
    public void initialize() {
        business = BusinessImpl.getInstance();
        //initCols();
        ObservableList<MenuItem> data =
                FXCollections.observableArrayList(
                );
    }



//    private void initCols() {
//        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
//        costCol.setCellValueFactory(cellData -> {
//            Money cost = cellData.getValue().getPrice();
//            String toShow = "$" + cost.getAmountMajorInt() + "." + cost.getAmountMinorInt();
//            return new SimpleStringProperty(toShow);
//        });
//
//        TableColumn<Sale, Sale> removeCol = new TableColumn<>("remove");
//        removeCol.setMinWidth(40);
//        removeCol.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
//        removeCol.setCellFactory(param -> new TableCell<Sale, Sale>() {
//            private final Button deleteButton = new Button("remove");
//
//            @Override
//            protected void updateItem(Sale sale, boolean empty) {
//                super.updateItem(sale, empty);
//
//                if (sale == null) {
//                    setGraphic(null);
//                    return;
//                }
//
//                setGraphic(deleteButton);
//                deleteButton.setOnAction(event -> data.remove(person));
//            }
//        });
//    }



    /**
     *
     * @param event cancel button pressed
     * @throws IOException print error
     */
    public void exitSalesScreen(ActionEvent event) throws IOException {
        try {
            //When logout button pressed, from home screen
            Parent mainMenuParent = FXMLLoader.load(getClass().getResource("gui/MainMenu.fxml"));
            Scene MainMenuScene = new Scene(mainMenuParent);

            //This line gets the Stage information
            Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
            window.setScene(MainMenuScene);
            window.show();
        }catch (IOException e){
            throw new IOException("Error in exiting sales screen.");
        }

    }

    /**
     * When the records button is pressed, open the records screen.
     *
     * @param event when the records button on the main menu gets pressed.
     * @throws IOException prints an error message
     */
    public void prevOrder(ActionEvent event) throws IOException{
        try {
            //When sales button pressed, from home screen, get sales scene
            Parent recordsParent = FXMLLoader.load(getClass().getResource("records.fxml"));
            Scene recordsScene = new Scene(recordsParent);

            //Get stage info and switch scenes.
            Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
            window.setScene(recordsScene);
            window.show();
        } catch (IOException e){
            throw new IOException("Error in opening records screen.");
        }
    }
}
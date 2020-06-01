package scheduler.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import scheduler.dbAccessors.CustomerAccessor;
import scheduler.models.Customer;
import scheduler.services.localization.UserLocalization;

import java.io.IOException;
import java.util.ResourceBundle;

public class CustomersController {
    @FXML
    private GridPane mainPane;

    @FXML
    private TableView<Customer> tableCustomers;
    @FXML
    private TableColumn<Customer, String> colName;
    @FXML
    private TableColumn<Customer, String> colAddress;
    @FXML
    private TableColumn<Customer, String> colPhone;


    @FXML
    public void initialize() {
        colName.setCellValueFactory(new PropertyValueFactory<Customer, String>("name"));
        colPhone.setCellValueFactory(new PropertyValueFactory<Customer, String>("phone"));
        colAddress.setCellValueFactory(new PropertyValueFactory<Customer, String>("address"));
        populateTable();
    }

    private void populateTable() {
        CustomerAccessor accessor = new CustomerAccessor();
        tableCustomers.setItems(accessor.getAllCustomers());
    }


    private void addTableRefresh(Stage stage) {
        stage.addEventHandler(WindowEvent.WINDOW_HIDDEN, windowEvent -> {
            populateTable();
        });
    }

    @FXML
    public void handleAddCustomer() {
        try {
            Stage primaryStage = new Stage();
            ResourceBundle bundle = UserLocalization.getBundle();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../views/addCustomers.fxml"), bundle);
            loader.setController(new AddCustomerController());
            Parent root = loader.load();
            // refresh table when returning from add customer screen
            addTableRefresh(primaryStage);
            primaryStage.setTitle(bundle.getString("addCustomer"));
            primaryStage.setScene(new Scene(root));
            primaryStage.show();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleModifyCustomer() {
        try {
            Stage primaryStage = new Stage();
            ResourceBundle bundle = UserLocalization.getBundle();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../views/addCustomers.fxml"), bundle);
            loader.setController(new ModifyCustomerController(tableCustomers.getSelectionModel().getSelectedItem()));
            addTableRefresh(primaryStage);
            Parent root = loader.load();
            primaryStage.setTitle(bundle.getString("modifyCustomer"));
            primaryStage.setScene(new Scene(root));
            primaryStage.show();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleDeleteCustomer() {
        CustomerAccessor accessor = new CustomerAccessor();
        accessor.deleteCustomer(tableCustomers.getSelectionModel().getSelectedItem());
        populateTable();
    }

    @FXML
    public void handleCancel() {
        Stage stage = (Stage)mainPane.getScene().getWindow();
        stage.close();
    }
}

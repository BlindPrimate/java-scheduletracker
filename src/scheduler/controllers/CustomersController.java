package scheduler.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import scheduler.dbAccessors.CustomerAccessor;
import scheduler.models.Customer;
import scheduler.services.localization.UserLocalization;

import java.io.IOException;
import java.util.Optional;
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



    @FXML
    public void handleAddCustomer() {
        try {
            Stage primaryStage = new Stage();
            ResourceBundle bundle = UserLocalization.getBundle();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../views/addCustomers.fxml"), bundle);
            loader.setController(new AddCustomerController());
            Parent root = loader.load();

            // refresh table when returning from add customer screen
            primaryStage.setTitle(bundle.getString("addCustomer"));
            primaryStage.setScene(new Scene(root));
            primaryStage.showAndWait();
            populateTable();
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
            if (tableCustomers.getSelectionModel().getSelectedItem() != null) {
                loader.setController(new ModifyCustomerController(tableCustomers.getSelectionModel().getSelectedItem()));
                Parent root = loader.load();
                primaryStage.setTitle(bundle.getString("modifyCustomer"));
                primaryStage.setScene(new Scene(root));
                primaryStage.showAndWait();
                populateTable();
                tableCustomers.refresh();
            }
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleDeleteCustomer() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setContentText("Are you sure you wish to delete this customer?");
        alert.setTitle("Delete Customer");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            CustomerAccessor accessor = new CustomerAccessor();
            accessor.deleteCustomer(tableCustomers.getSelectionModel().getSelectedItem());
            populateTable();
        } else {
            alert.close();
        }
    }

    @FXML
    public void handleCancel() {
        Stage stage = (Stage)mainPane.getScene().getWindow();
        stage.close();
    }
}

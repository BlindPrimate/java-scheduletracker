package scheduler.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import scheduler.dbAccessors.CustomerAccessor;
import scheduler.models.Customer;

public class CustomersController {
    @FXML
    private GridPane mainPane;

    @FXML
    private TableView tableCustomers;
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

        CustomerAccessor accessor = new CustomerAccessor();
        tableCustomers.setItems(accessor.getAllCustomers());


    }

    @FXML
    public void handleAddCustomer() {

    }

    @FXML
    public void handleModifyCustomer() {

    }

    @FXML
    public void handleDeleteCustomer() {

    }

    @FXML
    public void handleCancel() {
        Stage stage = (Stage)mainPane.getScene().getWindow();
        stage.close();
    }
}

package scheduler.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import scheduler.dbAccessors.CustomerAccessor;
import scheduler.models.Customer;

public class ModifyCustomerController {
    @FXML
    private GridPane mainPane;
    @FXML
    private TextField fieldName;
    @FXML
    private TextField fieldAddress;
    @FXML
    private TextField fieldPhone;
    Customer customer;

    public ModifyCustomerController(Customer customer) {
        this.customer = customer;
    }

    @FXML
    public void initialize() {
        fieldName.setText(customer.getName());
        fieldAddress.setText(customer.getAddress());
        fieldPhone.setText(customer.getPhone());
    }

    @FXML
    public void handleSave() {
        CustomerAccessor accessor = new CustomerAccessor();
        customer.setName(fieldName.getText());
        customer.setAddress(fieldAddress.getText());
        customer.setPhone(fieldPhone.getText());
        accessor.updateCustomer(customer);
        handleCancel();
    }



    @FXML
    public void handleCancel() {
        Stage stage = (Stage)mainPane.getScene().getWindow();
        stage.close();
    }
}

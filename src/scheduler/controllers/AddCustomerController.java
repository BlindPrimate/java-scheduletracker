package scheduler.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import scheduler.dbAccessors.CustomerAccessor;
import scheduler.models.Customer;

public class AddCustomerController {
    @FXML
    private GridPane mainPane;
    @FXML
    private TextField fieldName;
    @FXML
    private TextField fieldAddress;
    @FXML
    private TextField fieldPhone;


    @FXML
    public void initialize() {

    }

    @FXML
    public void handleSave() {
        CustomerAccessor accessor = new CustomerAccessor();
        String name = fieldName.getText();
        String address = fieldAddress.getText();
        String phone = fieldPhone.getText();
        accessor.addCustomer(new Customer(name, address, phone));
    }



    @FXML
    public void handleCancel() {
        Stage stage = (Stage)mainPane.getScene().getWindow();
        stage.close();
    }
}

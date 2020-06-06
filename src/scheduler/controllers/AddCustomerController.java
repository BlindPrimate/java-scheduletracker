package scheduler.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import scheduler.dbAccessors.CustomerAccessor;
import scheduler.models.Customer;
import scheduler.services.localization.UserLocalization;

import java.util.ResourceBundle;

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
    private Label labelAlert;


    @FXML
    public void initialize() {

    }

    @FXML
    public void handleSave() {
        labelAlert.setText("");
        String name = fieldName.getText();
        String address = fieldAddress.getText();
        String phone = fieldPhone.getText();
        ResourceBundle bundle = UserLocalization.getBundle();
        if (name.isBlank()) {
            labelAlert.setText(bundle.getString("alertCustomerNameBlank"));
        } else if (address.isBlank()) {
            labelAlert.setText(bundle.getString("alertAddressBlank"));
        } else if (phone.isBlank()) {
            labelAlert.setText(bundle.getString("alertPhoneBlank"));
        } else {
            CustomerAccessor accessor = new CustomerAccessor();
            accessor.addCustomer(new Customer(name, address, phone));

            // close window
            handleCancel();
        }
    }


    @FXML
    public void handleCancel() {
        Stage stage = (Stage)mainPane.getScene().getWindow();
        stage.close();
    }
}

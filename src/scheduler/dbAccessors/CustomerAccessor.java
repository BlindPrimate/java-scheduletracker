package scheduler.dbAccessors;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import scheduler.models.Customer;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class CustomerAccessor {

    private ObservableList<Customer> customers = FXCollections.observableArrayList();
    private ObservableList<String> customerNames = FXCollections.observableArrayList();
    private Connection conn = DBConnection.getConnection();

    public CustomerAccessor() {
    }

    public ObservableList<Customer> getAllCustomers() {
        try {
            Statement stm = conn.createStatement();
            ResultSet res = stm.executeQuery("select customer.*, address.* FROM customer INNER JOIN address ON customer.customerId = address.addressId");
            while(res.next()) {
                int id = res.getInt("customerId");
                String name = res.getString("customerName");
                String address = res.getString("address");
                String phone = res.getString("phone");
                customers.add(new Customer(id, name, address, phone));
            }
            return customers;
        } catch (SQLException e) {
            System.out.println("Error retrieving customer all customer data");
            e.printStackTrace();
        }
        return null;
    }

    public ObservableList<String> getAllCustomerNames() {

        try {
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery("select customerName from customer");
            while(rs.next()) {
                customerNames.add(rs.getString("customerName"));
            }
            return customerNames;
        } catch (SQLException e) {
            System.out.println("Error retrieving customer names");
            e.printStackTrace();
        }
        return null;
    }
}

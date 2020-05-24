package scheduler.dbAccessors;

import javafx.collections.ObservableList;
import scheduler.models.Customer;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class CustomerAccessor extends DataAccessor {

    private ObservableList<Customer> customers;

    public CustomerAccessor() {
    }

    public ObservableList<Customer> getAllCustomers() {
        try {
            Statement stm = conn.createStatement();
            ResultSet res = stm.executeQuery("select * from customers");
            while(res.next()) {
                int id = res.getInt("appointmentId");
                String name = res.getString("name");
                String address = res.getString("address");
                customers.add(new Customer(id, name, address));
            }
            return customers;
        } catch (SQLException e) {
            System.out.println("Error retrieving customer all customer data");
            e.printStackTrace();
        }
        return null;
    }
}

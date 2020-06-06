package scheduler.dbAccessors;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import scheduler.models.Customer;
import scheduler.services.Authenticator;

import java.sql.*;

public class CustomerAccessor {


    public CustomerAccessor() {
    }

    public ObservableList<Customer> getAllCustomers() {
        ObservableList<Customer> customers = FXCollections.observableArrayList();
        Connection conn = DBConnection.getConnection();
        try {
            Statement stm = conn.createStatement();
            ResultSet res = stm.executeQuery("select customer.*, address.* FROM customer INNER JOIN address ON customer.addressId = address.addressId");
            while(res.next()) {
                int id = res.getInt("customerId");
                String name = res.getString("customerName");
                String address = res.getString("address");
                String phone = res.getString("phone");
                Customer customer = new Customer(name, address, phone);
                customer.setId(id);
                customer.setAddressId(res.getInt("addressId"));
                customers.add(customer);
            }
            return customers;
        } catch (SQLException e) {
            System.out.println("Error retrieving customer all customer data");
            e.printStackTrace();
        }
        return null;
    }

    public int addCustomer(Customer customer) {
        Authenticator auth = Authenticator.getInstance();
        Connection conn = DBConnection.getConnection();
        try {
            PreparedStatement stm1 = conn.prepareStatement(
                    "INSERT INTO address"+
                            "(addressId, address, address2, cityId, postalCode, phone, createDate, createdBy, lastUpdate, lastUpdateBy) " +
                            "VALUES(DEFAULT, ?,?,?,?,?, CURRENT_TIMESTAMP, ?, CURRENT_TIMESTAMP, ?)", PreparedStatement.RETURN_GENERATED_KEYS);

            // set to allow multiple insert statements to be sent
            conn.setAutoCommit(false);
            stm1.setString(1,customer.getAddress()); // address 1
            stm1.setString(2, "");                // address 2
            stm1.setInt(3, 3);                    // city id
            stm1.setInt(4, 0);                    // post code
            stm1.setString(5, customer.getPhone());  // phone
            stm1.setString(6, auth.getUsername());   // created by
            stm1.setString(7, auth.getUsername());   // last updated by
            stm1.execute();
            // get results form first insert (to get ID for address to add to customer as FK)
            ResultSet keys = stm1.getGeneratedKeys();
            if (keys.next()) {
                PreparedStatement stm2 = conn.prepareStatement("INSERT INTO customer" +
                        "(customerId, customerName, addressId, active, createDate, createdBy, lastUpdate, lastUpdateBy) " +
                        "VALUES" +
                        "(DEFAULT, ?, ?, ?, CURRENT_TIMESTAMP, ?, CURRENT_TIMESTAMP, ? )");
                stm2.setString(1, customer.getName()); // name
                stm2.setLong(2, keys.getLong(1) );  // address id
                stm2.setInt(3, 1);   // active
                stm2.setString(4, auth.getUsername());  // created by
                stm2.setString(5, auth.getUsername());  // last updated by

                stm2.execute();
                conn.commit();
            }
        } catch (SQLException e) {
            System.out.println("Error adding customer");
            e.printStackTrace();
        }
        return 0;
    }

    public int updateCustomer(Customer customer) {

        Connection conn = DBConnection.getConnection();
        Authenticator auth = Authenticator.getInstance();
        try {
            conn.setAutoCommit(false);
            String sqlCustomer = "UPDATE customer " +
                            "SET customerName=? " +
                            "WHERE customerId=?";
            PreparedStatement stm1 = conn.prepareStatement(sqlCustomer);
            stm1.setString(1, customer.getName());
            stm1.setInt(2, customer.getId());
            stm1.executeUpdate();

            String sqlAddress = "UPDATE address " +
                            "SET address=?, phone=?, lastUpdateBy=?, lastUpdate=CURRENT_DATE " +
                            "WHERE addressId=?";
            PreparedStatement stm2 = conn.prepareStatement(sqlAddress);
            stm2.setString(1,customer.getAddress());
            stm2.setString(2,customer.getPhone());
            stm2.setString(3, auth.getUsername());
            stm2.setInt(4, customer.getAddressId());
            stm2.executeUpdate();
            conn.commit();
            return 1;
        } catch (SQLException e) {
            System.out.println("Error updating customer data");
            e.printStackTrace();
        }
        return 0;
    }

    public void deleteCustomer(Customer customer) {
        Connection conn = DBConnection.getConnection();
        try {
            conn.setAutoCommit(false);
            String sql1 = "DELETE FROM appointment WHERE customerId = ?";
            PreparedStatement stm1 = conn.prepareStatement(sql1);
            stm1.setInt(1, customer.getId());
            String sql2 = "DELETE FROM customer WHERE customerId = ?";
            PreparedStatement stm2 = conn.prepareStatement(sql2);
            stm2.setInt(1, customer.getId());

            stm1.executeUpdate();
            stm2.executeUpdate();
            conn.commit();
        } catch (SQLException e) {
            System.out.println("Error deleting customer");
            e.printStackTrace();
        }
    }





}

package scheduler.dbAccessors;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import scheduler.models.Appointment;
import scheduler.models.Customer;
import scheduler.services.Authenticator;

import java.sql.*;
import java.time.LocalDateTime;

public class AppointmentAccessor {

    private Connection conn = null;

    public AppointmentAccessor() {
        this.conn = DBConnection.getConnection();
    }

    private ObservableList<Appointment> createAppointments(ResultSet rs) {

        ObservableList<Appointment> appointments = FXCollections.observableArrayList();
        // loop results and create observable array of appointments
        try {

            while(rs.next()) {
                int userId = rs.getInt("userId");
                String customerName = rs.getString("customerName");
                int customerId = rs.getInt("customerId");
                Timestamp startTime = rs.getTimestamp("start");
                String type = rs.getString("type");
                Timestamp endTime = rs.getTimestamp("end");
                String title = rs.getString("title");
                String description = rs.getString("description");
                int id = rs.getInt("appointmentId");
                Customer customer = new Customer(customerName, "", "");

                // format appointments
                Appointment appointment = new Appointment(startTime, endTime, customerId, userId, type, title, customer);
                appointment.setCustomerName(customerName);
                appointment.setId(id);
                appointment.setDescription(description);
                appointments.add(appointment);
            }
        } catch (SQLException e) {
            System.out.println("Error creating appointment from retrieved data");
            e.printStackTrace();
        }
        return appointments;
    }

    public ObservableList<Appointment> getAppointmentsTimeSpan(LocalDateTime fromDate, LocalDateTime toDate) {

        try {
            PreparedStatement stm = conn.prepareStatement(
                    "SELECT customer.customerId, customer.customerName, "
                        + "user.userName, user.userId, "
                        + "appointment.appointmentId, appointment.title, appointment.type,"
                        + "appointment.start, appointment.end, appointment.description "
                        + "FROM appointment "
                        + "INNER JOIN customer "
                        + "ON appointment.customerId = customer.customerId "
                        + "inner join user "
                        + "ON appointment.userId = user.userId "
                        + "WHERE appointment.start BETWEEN ? AND ? "
            );

            stm.setTimestamp(1, Timestamp.valueOf(fromDate));
            stm.setTimestamp(2, Timestamp.valueOf(toDate));

            ResultSet res = stm.executeQuery();
            return createAppointments(res);
        } catch (SQLException e) {
            System.out.println("Error retrieving appointment data");
            e.printStackTrace();
        }
        return null;
    }

    public ObservableList<Appointment> getWeeklyAppointments() {
        return getAppointmentsTimeSpan(LocalDateTime.now(), LocalDateTime.now().plusDays(7));
    }

    public ObservableList<Appointment> getMonthlyAppointments() {
        return getAppointmentsTimeSpan(LocalDateTime.now(), LocalDateTime.now().plusMonths(1));
    }

    public ObservableList<Appointment> getAllAppointments() {
        try {
            Statement stm = conn.createStatement();

            // retrieve all appointments INNER JOIN with appointment, customer, and user tables
            ResultSet res = stm.executeQuery("select customer.customerId, customer.customerName, "
                    + "user.userName, user.userId, "
                    + "appointment.appointmentId, appointment.title, appointment.type,"
                    + "appointment.start, appointment.end, appointment.description "
                    + "from appointment "
                    + "inner join customer "
                    + "on appointment.customerId = customer.customerId "
                    + "inner join user "
                    + "on appointment.userId = user.userId");

            return createAppointments(res);
        } catch (SQLException e) {
            System.out.println("Error retrieving customer appointment data");
            e.printStackTrace();
        }

        return null;
    }

    public boolean addAppointment(Appointment appointment) {
        Authenticator auth = Authenticator.getInstance();
        try {
            PreparedStatement stm = conn.prepareStatement(
                    "INSERT INTO `appointment` VALUES "
                            + "(default,?,?,?,?,?,?,?,?,?,?,CURRENT_TIMESTAMP,?,CURRENT_TIMESTAMP,?)"
            );
            stm.setInt(1, appointment.getCustomerId() ); // customer id
            stm.setInt(2, auth.getUserId() ); // user id
            stm.setString(3, appointment.getTitle() ); // title
            stm.setString(4, ""); // description
            stm.setString(5, ""); // location
            stm.setString(6, ""); // contact
            stm.setString(7, appointment.getAppointmentType()); // type
            stm.setString(8, ""); // url
            stm.setTimestamp(9, appointment.getStartTimeStamp() ); // start time
            stm.setTimestamp(10, appointment.getEndTimeStamp() ); // end time
            stm.setString(11, auth.getUsername()); //  user
            stm.setString(12, auth.getUsername()); //  last updated by - user
            stm.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error inserting new appointment into database");
            e.printStackTrace();
        } finally {
            return true;
        }
    }

    public void deleteAppointment(int customerId) {
        try {
            PreparedStatement st = conn.prepareStatement( "DELETE FROM appointment WHERE appointmentId=?");
            st.setInt(1, customerId);
            st.execute();
        } catch (Exception e) {
            System.out.println("Error deleting appointment");
            e.printStackTrace();
        }
    }

    public void modifyAppointment(Appointment appointment) {
        Authenticator auth = Authenticator.getInstance();
        String sql = "UPDATE appointment " +
                "SET customerId=?, type=?, start=?, end=? lastUpdate=CURRENT_DATE, lastUpdateBy=? " +
                "WHERE appointmentId=?";
        try {
            Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, appointment.getCustomerId());  // customer id
            stmt.setString(2, appointment.getAppointmentType());  // appointment type
            stmt.setTimestamp(3, appointment.getStartTimeStamp());  // start time
            stmt.setTimestamp(4, appointment.getEndTimeStamp());  // end time
            stmt.setString(5, auth.getUsername());  // last update by
            stmt.setInt(6, appointment.getId());

        } catch(SQLException e) {
            e.printStackTrace();
        }

    }
}

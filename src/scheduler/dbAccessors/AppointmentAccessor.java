package scheduler.dbAccessors;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import scheduler.models.Appointment;

import java.sql.*;

public class AppointmentAccessor {

    private ObservableList<Appointment> appointments;
    private Connection conn = null;

    public AppointmentAccessor() {
        this.conn = DBConnection.getConnection();
        this.appointments = FXCollections.observableArrayList();
    }

    public ObservableList<Appointment> getAllAppointments() {
        try {
            Statement stm = conn.createStatement();

            // retrieve all appointments INNER JOIN with appointment, customer, and user tables
            ResultSet res = stm.executeQuery("select customer.customerId, customer.customerName, " +
                    "user.userName, user.userId, " +
                    "appointment.appointmentId, appointment.title, appointment.type, appointment.start, appointment.end " +
                    "from appointment " +
                    "inner join customer " +
                    "on appointment.customerId = customer.customerId " +
                    "inner join user " +
                    "on appointment.userId = user.userId");

            // loop results and create observable array of appointments
            while(res.next()) {
                int userId = res.getInt("userId");
                String customer = res.getString("customerName");
                int customerId = res.getInt("customerId");
                Timestamp startTime = res.getTimestamp("start");
                String type = res.getString("type");
                Timestamp endTime = res.getTimestamp("end");
                String title = res.getString("title");
                int id = res.getInt("appointmentId");

                // format appointments
                Appointment appointment = new Appointment(startTime, endTime, customerId, userId, type, title);
                appointment.setCustomerName(customer);
                appointment.setId(id);
                appointments.add(appointment);
            }
            return appointments;
        } catch (SQLException e) {
            System.out.println("Error retrieving customer appointment data");
            e.printStackTrace();
        }

        return null;
    }

    public boolean addAppointment(Appointment appt) {
        try {
            PreparedStatement stm = conn.prepareStatement(
                    "INSERT INTO `appointment` VALUES "
                            + "(default,?,?,?,?,?,?,?,?,?,?,CURRENT_TIMESTAMP,?,CURRENT_TIMESTAMP,?)"
            );
            stm.setInt(1, appt.getCustomerId() ); // customer id
            stm.setInt(2, appt.getCreatedById() ); // user id
            stm.setString(3, "" ); // title
            stm.setString(4, ""); // description
            stm.setString(5, ""); // location
            stm.setString(6, ""); // contact
            stm.setString(7, appt.getAppointmentType()); // type
            stm.setString(8, ""); // url
            stm.setTimestamp(9, appt.getStartTimeStamp() ); // start time
            stm.setTimestamp(10, appt.getEndTimeStamp() ); // end time
            stm.setString(11, appt.getCreatedBy() ); //  user
            stm.setString(12, appt.getCreatedBy()); //  last updated by - user
            stm.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error inserting new appointment into database");
            e.printStackTrace();
        } finally {
            return true;
        }
    }
    public void deleteAppointment(int customerId) {
        System.out.println(customerId);
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
        System.out.println("modified");
    }
}

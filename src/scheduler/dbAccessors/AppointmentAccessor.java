package scheduler.dbAccessors;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import scheduler.models.Appointment;

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
                String customer = rs.getString("customerName");
                int customerId = rs.getInt("customerId");
                Timestamp startTime = rs.getTimestamp("start");
                String type = rs.getString("type");
                Timestamp endTime = rs.getTimestamp("end");
                String title = rs.getString("title");
                String description = rs.getString("description");
                int id = rs.getInt("appointmentId");

                // format appointments
                Appointment appointment = new Appointment(startTime, endTime, customerId, userId, type, title);
                appointment.setCustomerName(customer);
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

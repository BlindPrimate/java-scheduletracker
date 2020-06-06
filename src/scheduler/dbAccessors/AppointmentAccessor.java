package scheduler.dbAccessors;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import scheduler.models.Appointment;
import scheduler.models.Customer;
import scheduler.services.Authenticator;
import scheduler.services.localization.TimeUtil;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

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
                LocalDateTime startTime = TimeUtil.toUserTime(rs.getTimestamp("start").toLocalDateTime());
                String type = rs.getString("type");
                LocalDateTime endTime = TimeUtil.toUserTime(rs.getTimestamp("end").toLocalDateTime());
                String title = rs.getString("title");
                String description = rs.getString("description");
                int id = rs.getInt("appointmentId");
                Customer customer = new Customer(customerName, "", "");
                customer.setId(customerId);

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

    public ObservableList<Appointment> getAppointmentsTimeSpan(int userId, LocalDateTime fromDate, LocalDateTime toDate) {

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
                        + "WHERE (appointment.start BETWEEN ? AND ?) "
                        + "AND appointment.userId = ?"
            );

            stm.setTimestamp(1, Timestamp.valueOf(fromDate));
            stm.setTimestamp(2, Timestamp.valueOf(toDate));
            stm.setInt(3, userId);

            ResultSet res = stm.executeQuery();
            return createAppointments(res);
        } catch (SQLException e) {
            System.out.println("Error retrieving appointment data");
            e.printStackTrace();
        }
        return null;
    }

    public ObservableList<Appointment> getWeeklyAppointments(int userId) {
        return getAppointmentsTimeSpan(userId, LocalDateTime.now(), LocalDateTime.now().plusDays(7));
    }

    public ObservableList<Appointment> getMonthlyAppointments(int userId) {
        return getAppointmentsTimeSpan(userId, LocalDateTime.now(), LocalDateTime.now().plusMonths(1));
    }

    public ObservableList<Appointment> getAllAppointments(int userId) {
        try {

            String sql = "select customer.customerId, customer.customerName, "
                    + "user.userName, user.userId, "
                    + "appointment.appointmentId, appointment.title, appointment.type,"
                    + "appointment.start, appointment.end, appointment.description "
                    + "from appointment "
                    + "inner join customer "
                    + "on appointment.customerId = customer.customerId "
                    + "inner join user "
                    + "on appointment.userId = user.userId "
                    + "WHERE appointment.userId = ? "
                    + "ORDER BY appointment.start";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            return createAppointments(rs);
        } catch (SQLException e) {
            System.out.println("Error retrieving customer appointment data");
            e.printStackTrace();
        }

        return null;
    }

    public boolean addAppointment(Appointment appointment) {
        Authenticator auth = Authenticator.getInstance();

        LocalDateTime start = TimeUtil.toUTC(appointment.getStartTime());
        LocalDateTime end = TimeUtil.toUTC(appointment.getEndTime());
        try {
            String sql = "INSERT INTO `appointment` VALUES "
                            + "(default,?,?,?,?,?,?,?,?,?,?,CURRENT_TIMESTAMP,?,CURRENT_TIMESTAMP,?)";
            PreparedStatement stm = conn.prepareStatement(sql);
            stm.setInt(1, appointment.getCustomerId() ); // customer id
            stm.setInt(2, auth.getUserId() ); // user id
            stm.setString(3, appointment.getTitle() ); // title
            stm.setString(4, ""); // description
            stm.setString(5, ""); // location
            stm.setString(6, ""); // contact
            stm.setString(7, appointment.getAppointmentType()); // type
            stm.setString(8, ""); // url
            stm.setTimestamp(9, Timestamp.valueOf(start));
            stm.setTimestamp(10, Timestamp.valueOf(end));
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

    public boolean modifyAppointment(Appointment appointment) {
        Authenticator auth = Authenticator.getInstance();
        LocalDateTime start = TimeUtil.toUTC(appointment.getStartTime());
        LocalDateTime end = TimeUtil.toUTC(appointment.getEndTime());
        String sql = "UPDATE appointment " +
                "SET customerId=?, type=?, start=?, end=?, lastUpdate=CURRENT_DATE, lastUpdateBy=?, title=? " +
                "WHERE appointmentId=?";
        try {
            Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, appointment.getCustomerId());  // customer id
            stmt.setString(2, appointment.getAppointmentType());  // appointment type
            stmt.setTimestamp(3, Timestamp.valueOf(start));  // start time
            stmt.setTimestamp(4, Timestamp.valueOf(end));  // start time
            stmt.setString(5, auth.getUsername());  // last update by
            stmt.setString(6, appointment.getTitle());
            stmt.setInt(7, appointment.getId());
            stmt.execute();
        } catch(SQLException e) {
            e.printStackTrace();
        } finally {
            return true;
        }

    }

    public Appointment checkUpcomingAppointments() {
        try {
            String sql = "select * from appointment INNER JOIN customer ON appointment.customerId = customer.customerId " +
                    "WHERE userId=? AND start BETWEEN ? and ADDDATE(?, interval 15 minute)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, Authenticator.getInstance().getUserId());
            stmt.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now(ZoneOffset.UTC)));
            stmt.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now(ZoneOffset.UTC)));
            ResultSet rs =  stmt.executeQuery();
            Appointment upcoming = new Appointment();
            if (rs.next()) {
                upcoming.setCustomerName(rs.getString("customerName"));
                upcoming.setStartTime(rs.getTimestamp("start").toLocalDateTime());
                return upcoming;
            } else {
                return null;
            }
        } catch(SQLException e ) {
            e.printStackTrace();
        }
        return null;
    }
    public boolean hasAppointmentOverlap(Appointment appointment) {
        Timestamp start = Timestamp.valueOf(TimeUtil.toUTC(appointment.getStartTime()));
        Timestamp end = Timestamp.valueOf(TimeUtil.toUTC(appointment.getEndTime()));

        // check if queried appointment's start or end times are between any existing appointment's start and end
        String sql = "SELECT * FROM appointment " +
                "WHERE NOT appointmentId=? " +
                "AND (((? BETWEEN start AND end) " +
                "OR (? BETWEEN start AND end)) " +
                "   OR ((start BETWEEN ? AND ?) " +
                "OR (end BETWEEN ? AND ?)))";
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, appointment.getId());
            stmt.setTimestamp(2, start);
            stmt.setTimestamp(3, end);
            stmt.setTimestamp(4, start);
            stmt.setTimestamp(5, end);
            stmt.setTimestamp(6, start);
            stmt.setTimestamp(7, end);
            ResultSet rs = stmt.executeQuery();

            // if any results found return true
            if (rs.next()) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;

    }
}

package scheduler.services;

import scheduler.dbAccessors.DBConnection;
import scheduler.services.localization.TimeUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class ReportUtil {

    public String appointmentReport() {
        Connection conn = DBConnection.getConnection();
        String reportText = "";
        String sql = "SELECT " +
        "type, COUNT(*) " +
        "FROM appointment " +
        "GROUP BY type";
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                reportText = reportText + rs.getString("type") + ": " + rs.getInt("count(*)") + "\n";
            }
            return reportText;
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String scheduleReport() {

        Connection conn = DBConnection.getConnection();
        String reportText = "";
        String sql =
                "SELECT * " +
                "FROM appointment " +
                "INNER JOIN user " +
                "ON appointment.userId = user.userId " +
                "INNER JOIN customer " +
                "ON appointment.customerId = customer.customerId " +
                "order by userName, start";
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            int currentId = 0;

            while (rs.next()) {
                if (currentId != rs.getInt("userId")) {
                    currentId = rs.getInt("userId");
                    reportText = reportText + "Consultant: " + rs.getString("userName") + "\n";
                } else {
                    LocalDateTime startTime = TimeUtil.toUserTime(rs.getTimestamp("start").toLocalDateTime());
                    LocalDateTime endTime = TimeUtil.toUserTime(rs.getTimestamp("end").toLocalDateTime());
                    String date = startTime.toLocalDate().format(TimeUtil.STANDARD_CALENDAR);

                    reportText = reportText +
                            rs.getString("customerName") +
                            "   " + date + "    " + startTime.format(TimeUtil.STANDARD12HOUR) +
                            "   " + endTime.format(TimeUtil.STANDARD12HOUR) + "\n";
                }
            }
            return reportText;
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    public String billableHoursReport() {
        Connection conn = DBConnection.getConnection();
        String reportText = "";
        String sql =
                "SELECT sum(time_to_sec(timediff(appointment.end, appointment.start)) / 60 / 60) AS 'billables', customer.customerName " +
                "FROM appointment " +
                "INNER JOIN customer " +
                "ON appointment.customerId = customer.customerId " +
                "group by customerName " +
                "order by customerName";

        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                reportText = reportText + "Customer: " + rs.getString("customerName") + "\n" +
                            "Total Billable Hours: " + rs.getFloat("billables") + "\n\n";
            }
            return reportText;
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}

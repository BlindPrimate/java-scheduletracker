package scheduler.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import scheduler.services.ReportUtil;

public class ReportsController {

    @FXML
    private GridPane mainPane;
    @FXML
    private TextArea scheduleReport;
    @FXML
    private TextArea appointmentReport;
    @FXML
    private TextArea billableReport;

    @FXML
    public void initialize() {
        ReportUtil reports = new ReportUtil();
        scheduleReport.setText(reports.scheduleReport());
        appointmentReport.setText(reports.appointmentReport());
        billableReport.setText(reports.billableHoursReport());
    }
}

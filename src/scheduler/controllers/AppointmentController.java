package scheduler.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import scheduler.models.Appointment;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

public abstract class AppointmentController {

    @FXML
    GridPane mainPane;
    @FXML
    TextField fieldTitle;
    @FXML
    TextField fieldDescription;
    @FXML
    TextField fieldLocation;
    @FXML
    TextField fieldType;
    @FXML
    TextField fieldUrl;
    @FXML
    ChoiceBox<String> choiceCustomer;
    @FXML
    DatePicker choiceStartDate;
    @FXML
    ChoiceBox<String> choiceStartTime;
    @FXML
    DatePicker choiceEndDate;
    @FXML
    ChoiceBox<String> choiceEndTime;

    protected Appointment appointment;
    protected ObservableList<String> appointmentStartTimes = FXCollections.observableArrayList();
    protected ObservableList<String> appointmentEndTimes = FXCollections.observableArrayList();
    protected FilteredList<String> filteredEndTimes = new FilteredList<String>(appointmentEndTimes);
    protected static final DateTimeFormatter parseTime = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT);

    protected Appointment currentAppointment = new Appointment();

    public AppointmentController() {
    }



    public static ObservableList<String> buildAppointmentTime() {
        ObservableList<String> appointmentTimes = FXCollections.observableArrayList();

        // set opening hours to 7am
        LocalTime time = LocalTime.of(8, 0);

        // create dropdown items for appointment hours
        while (!time.equals(LocalTime.of(17, 0))) {
            appointmentTimes.add(time.format(parseTime));
            time = time.plusMinutes(15);
        }
        return appointmentTimes;
    }


    public void handleExit() {
        Stage stage = (Stage)mainPane.getScene().getWindow();
        stage.close();
    }

}

package scheduler.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import scheduler.dbAccessors.AppointmentAccessor;
import scheduler.models.Appointment;
import scheduler.models.Customer;
import scheduler.services.localization.UserLocalization;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ResourceBundle;

public abstract class AppointmentController {

    @FXML
    GridPane mainPane;
    @FXML
    TextField fieldTitle;
    @FXML
    ComboBox<String> comboType;
    @FXML
    ComboBox<Customer> choiceCustomer;
    @FXML
    DatePicker choiceStartDate;
    @FXML
    ComboBox<LocalTime> choiceStartTime;
    @FXML
    ComboBox<LocalTime> choiceEndTime;
    @FXML
    Label alertText;



    protected Appointment appointment;
    protected ObservableList<LocalTime> appointmentStartTimes = FXCollections.observableArrayList();
    protected ObservableList<LocalTime> appointmentEndTimes = FXCollections.observableArrayList();
    protected FilteredList<LocalTime> filteredEndTimes = new FilteredList<>(appointmentEndTimes, s -> false);
    protected static final DateTimeFormatter parseTime = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT);


    protected ListCell<LocalTime> timeFactory() {
        return new ListCell<LocalTime>() {
            @Override
            protected void updateItem(LocalTime localTime, boolean empty) {
                super.updateItem(localTime, empty);
                if (localTime == null || empty) {
                    setText(null);
                } else {
                    setText(localTime.format(parseTime));
                }
            }
        };
    };


    public static ObservableList<LocalTime> buildAppointmentTime() {
        ObservableList<LocalTime> appointmentTimes = FXCollections.observableArrayList();

        // set opening hours to 7am
        LocalTime time = LocalTime.of(8, 0);

        // create dropdown items for appointment hours
        while (!time.equals(LocalTime.of(17, 0))) {
            appointmentTimes.add(time);
            time = time.plusMinutes(15);
        }
        return appointmentTimes;
    }

    public boolean isValidAppointment() {
        AppointmentAccessor accessor = new AppointmentAccessor();
        ResourceBundle bundle = UserLocalization.getBundle();
        if (fieldTitle.getText().isBlank()) {
            alertText.setText(bundle.getString("alertTitle"));
            return false;
        } else if (comboType.getValue() == null) {
            alertText.setText(bundle.getString("alertChooseType"));
            return false;
        } else if (choiceCustomer.getValue() == null) {
            alertText.setText(bundle.getString("alertChooseCustomer"));
            return false;
        }
        return true;
    }

    public void handleExit() {
        Stage stage = (Stage)mainPane.getScene().getWindow();
        stage.close();
    }

}

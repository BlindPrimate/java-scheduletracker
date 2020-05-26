package scheduler.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import scheduler.dbAccessors.AppointmentAccessor;
import scheduler.dbAccessors.CustomerAccessor;
import scheduler.models.Appointment;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

public class AddAppointmentController implements Controller {
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

    private ObservableList<String> appointmentTimes = FXCollections.observableArrayList();
    private DateTimeFormatter parseTime = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT);


    Appointment currentAppointment = new Appointment();

    @FXML
    public void initialize() {


        // load customer names in dropdown
        CustomerAccessor access = new CustomerAccessor();
        choiceCustomer.setItems(access.getAllCustomerNames());

        choiceStartDate.setValue(LocalDate.now());
        choiceEndDate.setValue(LocalDate.now());

        // set opening hours to 8am
        LocalTime time = LocalTime.of(8, 0);

        // create dropdown items for appointment hours
        while (!time.equals(LocalTime.of(18, 0))) {
            appointmentTimes.add(time.format(parseTime));
            time = time.plusMinutes(15);
        }
        choiceStartTime.setItems(appointmentTimes);
        choiceStartTime.setValue(appointmentTimes.get(0));
        choiceEndTime.setItems(appointmentTimes);
        choiceEndTime.setValue(appointmentTimes.get(1));

    }

    public void handleAddAppointment() {

        AppointmentAccessor accessor = new AppointmentAccessor();

        // set appointment attributes
        currentAppointment.setTitle(fieldTitle.getText().trim());
        currentAppointment.setDescription(fieldDescription.getText().trim());

        LocalTime startTime = LocalTime.parse(choiceStartTime.getValue(), parseTime);
        LocalDateTime appointmentStart = LocalDateTime.of(choiceStartDate.getValue(), startTime);

        Timestamp sqlStartTime = Timestamp.valueOf(appointmentStart);

    }


    public void handleExit() {
        Stage stage = (Stage)mainPane.getScene().getWindow();
        stage.close();
    }
}

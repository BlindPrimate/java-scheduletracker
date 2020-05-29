package scheduler.controllers;

import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import scheduler.dbAccessors.AppointmentAccessor;
import scheduler.models.Appointment;

import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.List;


public class RootController {

    @FXML private TableView<Appointment> appointmentTable;
    @FXML private TableColumn<Appointment, String> colStartTime;
    @FXML private TableColumn<Appointment, String> colEndTime;
    @FXML private TableColumn<Appointment, String> colCustomer;
    @FXML private TableColumn<Appointment, String> colType;

    @FXML private ToggleButton toggleAll;
    @FXML private ToggleButton toggleWeek;
    @FXML private ToggleButton toggleMonth;


    private ObservableList<Appointment>  appointments;
    private AppointmentAccessor accessor;
    private static RootController instance = null;
    private static AppointmentAccessor apptAccessor = new AppointmentAccessor();

    // singleton
    public RootController() {
    }

    @FXML
    public void initialize() {


        // set time span toggle buttons to allow one clicked at a time
        List<ToggleButton> timeToggles = List.of(toggleAll, toggleMonth, toggleWeek);
        timeToggles.forEach(toggle -> {
            toggle.setOnAction(e -> {
                timeToggles.forEach(otherToggle -> {
                    otherToggle.setSelected(false);
                });
                toggle.setSelected(true);
                if (toggleAll.isSelected()) {
                    populateAll();
                } else if (toggleWeek.isSelected()) {
                    populateWeekly();
                } else if (toggleMonth.isSelected()) {
                    populateMonthly();
                }

            });
        });


        DateTimeFormatter readableTime = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT);

        colType.setCellValueFactory(new PropertyValueFactory<Appointment, String>("appointmentType"));
        colStartTime.setCellValueFactory(columnData -> {
            String t = columnData.getValue().getStartTimeStamp().toLocalDateTime().format(readableTime);
            return new SimpleObjectProperty<>(t);
        });
        colEndTime.setCellValueFactory(columnData -> {
            String t = columnData.getValue().getEndTimeStamp().toLocalDateTime().format(readableTime);
            return new SimpleObjectProperty<>(t);
        });
        colCustomer.setCellValueFactory(new PropertyValueFactory<Appointment, String>("customerName"));

        // set appointment display to all on open
        toggleAll.fire();
        // select first item
        appointmentTable.getSelectionModel().select(0);
    }

    public void populateAll() {
        appointmentTable.setItems(apptAccessor.getAllAppointments());
    }

    public void populateWeekly() {
        appointmentTable.setItems(apptAccessor.getWeeklyAppointments());
    }

    public void populateMonthly() {
        appointmentTable.setItems(apptAccessor.getMonthlyAppointments());
    }

    // event handlers
    public void handleAddButton() {
        try {
            Stage newStage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../views/makeAppointment.fxml"));
            loader.setController(new AddAppointmentController());
            Parent root = loader.load();

            // refresh table of appointments on return to main screen
            newStage.addEventHandler(WindowEvent.WINDOW_HIDDEN, e -> {
            });
            newStage.initOwner(appointmentTable.getScene().getWindow());
            newStage.setTitle("Add Appointment");
            newStage.setScene(new Scene(root));
            newStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void handleModifyButton() {
        try {
            Stage newStage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../views/makeAppointment.fxml"));

            // load controller with selected appointment
            loader.setController(new ModifyAppointmentController(appointmentTable.getSelectionModel().getSelectedItem()));
            Parent root = loader.load();

            // refresh table of appointments on return to main screen
            newStage.addEventHandler(WindowEvent.WINDOW_HIDDEN, e -> {
            });
            newStage.setTitle("Modify Appointment");
            newStage.initOwner(appointmentTable.getScene().getWindow());
            newStage.setScene(new Scene(root));
            newStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void handleDeleteButton() {
        AppointmentAccessor accessor = new AppointmentAccessor();
        Appointment appt = appointmentTable.getSelectionModel().getSelectedItem();
        accessor.deleteAppointment(appt.getId());
        appointments.remove(appointmentTable.getSelectionModel().getSelectedIndex());
    }

    public void handleExit() {
        Stage stage = (Stage)appointmentTable.getScene().getWindow();
        stage.close();
    }
}

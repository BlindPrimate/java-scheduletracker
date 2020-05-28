package scheduler.controllers;

import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import scheduler.dbAccessors.AppointmentAccessor;
import scheduler.models.Appointment;
import scheduler.services.SceneBuilder;

import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;


public class RootController {

    @FXML private TableView<Appointment> appointmentTable;
    @FXML private TableColumn<Appointment, String> colStartTime;
    @FXML private TableColumn<Appointment, String> colEndTime;
    @FXML private TableColumn<Appointment, String> colCustomer;
    @FXML private TableColumn<Appointment, String> colType;
    private ObservableList<Appointment>  appointments;
    private AppointmentAccessor accessor;

    private static RootController instance = null;

    public static RootController getInstance() {
        if (instance == null) {
            instance = new RootController();
        }
        return instance;
    }

    // singleton
    private RootController() {
    }

    @FXML
    public void initialize() {

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
        populateAppointments();
        appointmentTable.setItems(appointments);
        appointmentTable.getSelectionModel().select(0);


    }

    public void populateAppointments() {
        appointments = new AppointmentAccessor().getAllAppointments();
        appointmentTable.setItems(appointments);
//        System.out.println(appointments.toString());
    }

    // event handlers
    public void handleAddButton() {
        AddAppointmentController controller = new AddAppointmentController();
        SceneBuilder scene = new SceneBuilder(appointmentTable, controller, "../views/makeAppointment.fxml");
        scene.show();
    }
    
    public void handleModifyButton() {
        Appointment targetAppt = (Appointment)appointmentTable.getSelectionModel().getSelectedItem();
        ModifyAppointmentController controller = new ModifyAppointmentController(targetAppt);
        SceneBuilder scene = new SceneBuilder(appointmentTable, controller, "../views/makeAppointment.fxml");
        scene.show();
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

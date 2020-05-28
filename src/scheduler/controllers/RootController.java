package scheduler.controllers;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import scheduler.dbAccessors.AppointmentAccessor;
import scheduler.models.Appointment;
import scheduler.services.SceneBuilder;

import java.util.Date;


public class RootController {

    @FXML private TableView<Appointment> appointmentTable;
    @FXML private TableColumn<Appointment, Date> colStartTime;
    @FXML private TableColumn<Appointment, Date> colEndTime;
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

    private RootController() {

    }

    @FXML
    public void initialize() {
        colType.setCellValueFactory(new PropertyValueFactory<Appointment, String>("appointmentType"));
        colStartTime.setCellValueFactory(new PropertyValueFactory<Appointment, Date>("startTime"));
        colEndTime.setCellValueFactory(new PropertyValueFactory<Appointment, Date>("endTime"));
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

package scheduler.controllers;

import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class AddAppointmentController implements Controller {
    @FXML GridPane mainPane;


    public void handleExit() {
        Stage stage = (Stage)mainPane.getScene().getWindow();
        stage.close();
    }
}

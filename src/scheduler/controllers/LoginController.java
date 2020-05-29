package scheduler.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import scheduler.services.Authenticator;


public class LoginController {

    @FXML
    private TextField fieldUser;
    @FXML
    private PasswordField fieldPassword;
    @FXML
    private Label alertText;

    private void initialize() {
    }

    private void invalid(String text) {
        alertText.setText(text);
    }

    @FXML
    private void handleLogin() {
        Authenticator auth = Authenticator.getInstance();
        boolean isUserValid = auth.isUserValid(fieldUser.getText());

        if (isUserValid) {
            boolean isAuthorized = auth.login(fieldUser.getText(), fieldPassword.getText());
            if (isAuthorized) {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("../views/root.fxml"));
                    Parent root = loader.load();

                    // refresh table of appointments on return to main screen
                    Stage stage = (Stage)fieldUser.getScene().getWindow();
                    stage.setTitle("Schedule Maker");
                    stage.setScene(new Scene(root));
                    stage.show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                invalid("Incorrect Password");
            }
        } else {
            invalid("Username not found");
        }
    }
}

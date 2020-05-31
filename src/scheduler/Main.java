package scheduler;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import scheduler.dbAccessors.DBConnection;
import scheduler.services.localization.UserLocalization;

import java.util.ResourceBundle;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        ResourceBundle bundle = UserLocalization.getBundle();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("views/login.fxml"), bundle);
        Parent root = loader.load();
        primaryStage.setTitle(bundle.getString("login"));
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        DBConnection.getConnection().close();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

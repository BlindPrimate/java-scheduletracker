package scheduler;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import scheduler.dbAccessors.DBConnection;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("views/login.fxml"));
        Parent root = loader.load();
        primaryStage.setTitle("Login");
        primaryStage.setScene(new Scene(root, 500, 475));
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

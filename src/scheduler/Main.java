package scheduler;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import scheduler.controllers.RootController;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("views/root.fxml"));
        loader.setController(RootController.getInstance());
        Parent root = loader.load();
        primaryStage.setTitle("Schedule Maker");
        primaryStage.setScene(new Scene(root, 500, 475));

        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}

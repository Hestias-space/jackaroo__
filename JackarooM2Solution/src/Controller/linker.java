package Controller;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import view.frontend;

public class linker extends Application {
    private frontend start;

    public static void main(String[] args) {
        launch(args);
    }



    public void start(Stage primaryStage) throws Exception {
        start = new frontend();
        primaryStage.setTitle("Jackaroo");
        primaryStage.getIcons().add(new Image(getClass().getResource("/Images/r.png").toExternalForm()));
        primaryStage.setResizable(false);
        primaryStage.setMaximized(false);

        Scene mainScene = start.createMainScene(primaryStage);
        primaryStage.setScene(mainScene);
        primaryStage.show();
    }


}
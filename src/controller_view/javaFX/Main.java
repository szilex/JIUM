package controller_view.javaFX;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Class that contains entry point of the application
 * @author Michal Szeler
 * @version 2.0
 */
public class Main extends Application {

    /**
     * Method that initializes Main Window
     * @param primaryStage Stage that is the root for new Window
     * @throws Exception Any exception that occurred in application
     */
    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("MainWindow.fxml"));
        primaryStage.setTitle("RSA Encryption and Decryption");
        primaryStage.setScene(new Scene(root, 620, 400));
        primaryStage.show();
    }

    /**
     * Method that is an entry point for the application
     * @param args Arguments passed in command line
     */
    public static void main(String[] args) {
        launch(args);
    }
}

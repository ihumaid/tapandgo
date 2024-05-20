package mv.port.harbour_tapngo;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import java.io.IOException;

public class HelloApplication extends Application {

    private static final Logger logger = LogManager.getLogger(HelloApplication.class);
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 600);
        stage.setTitle("Driver Tap To Pay!");
        stage.getIcons().add(new Image(HelloApplication.class.getResourceAsStream("images/barrier.png")));
        stage.setScene(scene);
        stage.show();

        logger.info("Application started!");

        try
        {
            // Import dynamic library
            System.load("C:\\ECR\\dll\\VX520JavaLibrary.dll");

        }catch (UnsatisfiedLinkError e)
        {
            logger.error("Native code library failed to load.", e);
            // TODO show in UI
        }
    }

    public static void main(String[] args) {
        launch();
    }
}
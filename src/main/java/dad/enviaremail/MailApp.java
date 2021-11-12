package dad.enviaremail;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class MailApp extends Application {

    private static Stage primaryStage;

    private MailController controller;

    @Override
    public void start(Stage primaryStage) throws Exception {
        MailApp.primaryStage = primaryStage;
        controller = new MailController();

        Scene scene = new Scene(controller.getView());

        primaryStage.setTitle("Enviar email");
        primaryStage.setScene(scene);
        primaryStage.getIcons().add(new Image("/image/email-send-icon-32x32.png"));
        primaryStage.show();
    }

    public static Stage getPrimaryStage() {
        return primaryStage;
    }

    public static void main(String[] args) {
        launch(args);
    }
}

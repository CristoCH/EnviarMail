package dad.enviaremail;

import javafx.beans.property.*;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MailController implements Initializable {

    //Model
    private StringProperty smtp = new SimpleStringProperty();
    private StringProperty port = new SimpleStringProperty();
    private BooleanProperty ssl = new SimpleBooleanProperty();
    private StringProperty from = new SimpleStringProperty();
    private StringProperty pass = new SimpleStringProperty();
    private StringProperty to = new SimpleStringProperty();
    private StringProperty subject = new SimpleStringProperty();
    private StringProperty message = new SimpleStringProperty();

    private Task<Void> tarea;

    //View

    @FXML
    private Button closeButton;

    @FXML
    private Button emptyButton;

    @FXML
    private TextField fromText;

    @FXML
    private TextField ipText;

    @FXML
    private TextArea messageText;

    @FXML
    private TextField passText;

    @FXML
    private TextField portText;

    @FXML
    private Button sendButton;

    @FXML
    private CheckBox sslCheckbox;

    @FXML
    private TextField subjectText;

    @FXML
    private TextField toText;

    @FXML
    private GridPane view;

    public GridPane getView() {
        return view;
    }

    public MailController() throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/MailView.fxml"));
        loader.setController(this);
        loader.load();

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        //Bindings
        smtp.bind(ipText.textProperty());
        port.bind(portText.textProperty());
        ssl.bind(sslCheckbox.selectedProperty());
        from.bind(fromText.textProperty());
        pass.bind(passText.textProperty());
        to.bind(toText.textProperty());
        subject.bind(subjectText.textProperty());
        message.bind(messageText.textProperty());

    }

    @FXML
    void onSendButtonAction(ActionEvent event) {
        int portInt = Integer.parseInt(port.get());

        tarea = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                Email email = new SimpleEmail();
                email.setHostName(smtp.get());
                email.setSmtpPort(portInt);
                email.setAuthenticator(new DefaultAuthenticator(from.get(), pass.get()));
                email.setSSLOnConnect(ssl.get());
                email.setFrom(from.get());
                email.setSubject(subject.get());
                email.setMsg(message.get());
                email.addTo(to.get());
                email.send();
                return null;
            }
        };

        tarea.setOnSucceeded(e->{
            Alert alertSend = new Alert(Alert.AlertType.INFORMATION);
            alertSend.setTitle("Mensaje enviado");
            alertSend.setHeaderText("Mensaje enviado con éxito a '"+ to.get()+"'");
            Stage stage = (Stage) alertSend.getDialogPane().getScene().getWindow();
            stage.getIcons().setAll(MailApp.getPrimaryStage().getIcons());
            alertSend.showAndWait();
        });
        tarea.setOnFailed(e->{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("No se pudo enviar el email.");
            alert.setContentText(e.getSource().getException().getMessage());
            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            stage.getIcons().setAll(MailApp.getPrimaryStage().getIcons());
            alert.showAndWait();
        });

        sendButton.disableProperty().bind(tarea.runningProperty());
        new Thread(tarea).start();
    }

    @FXML
    void onEmptyButtonAction(ActionEvent event) {
        ipText.clear();
        portText.clear();
        sslCheckbox.setSelected(false);
        fromText.clear();
        passText.clear();
        toText.clear();
        subjectText.clear();
        messageText.clear();
    }

    @FXML
    void onCloseButtonAction(ActionEvent event) {
        MailApp.getPrimaryStage().close();
    }
}

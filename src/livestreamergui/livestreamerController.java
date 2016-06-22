package livestreamergui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

/**
 * FXML Controller class. Kontrolliert die GUI.
 *
 * @author Simon Mönkebüscher
 * @since 2016-06-11 Erstellung
 * @since 2016-06-14 Doku vervollständigt
 */
public class livestreamerController implements Initializable {

    //Generierter Code Anfang
    @FXML
    private Button btnStart;
    @FXML
    private ComboBox<model.Stream> cbStreams;
    @FXML
    private ComboBox<String> cbQuality;
    @FXML
    private RadioButton radioList;
    @FXML
    private ToggleGroup tglgrp;
    @FXML
    private RadioButton radioText;
    @FXML
    private Button btnEnd;
    @FXML
    private TextField textfieldInput;
    @FXML
    private ImageView imageView;
    //Generierter Code Ende

    /**
     * Initializes the controller class.
     *
     * @param url URL
     * @param rb ResourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //Da standardmäßig die CB aktiviert ist, wird Textfield deaktiviert
        textfieldInput.setDisable(true);

        // Hier werden die Comboboxen gefüllt.
        this.setQuality();
        this.setStreams();

    }

    /**
     * Setzt die Qualitätsstufen in die Combobox. Es wird nicht überprüft, ob
     * die Qualitätsstufen verfügbar sind! Best funktioniert immer, daher wird
     * es als Standard ausgewählt.
     */
    private void setQuality() {
        //Erzeugen der Collection der Qualitätsabstufungen
        ObservableList<String> quality = FXCollections.observableArrayList();

        //Hinzufügen der Begriffe
        //Best und Worst sollten immer funktionieren.
        quality.add("best"); //Das wäre dann wohl Source
        quality.add("high");
        quality.add("medium");
        quality.add("low");
        quality.add("worst"); //Wäre dann wohl mobile
        quality.add("audio");

        //Qualität in die Combobox setzen
        cbQuality.setItems(quality);
        //Erstes Item auswählen (best - wird am häufigsten gebraucht)
        cbQuality.getSelectionModel().selectFirst();
    }

    /**
     * Holt die Streams und setzt sie in die Combobox.
     */
    private void setStreams() {
        //Streams in die Combobox setzen
        cbStreams.setItems(LivestreamerGUI.getStreams());
        //Erstes Item auswählen
        cbStreams.getSelectionModel().selectFirst();
        //Prüft, ob der erstausgewählte Stream online ist.
        cbStreamsAction(new ActionEvent());

    }

    /**
     * Startet den ausgewählten Stream.
     *
     * @param event Auslösendes Event.
     */
    @FXML
    private void btnStartAction(ActionEvent event) {
        //Bereitstellen des Kommandos:
        String aufruf = "livestreamer ";

        //Wenn aus der Liste ausgewählt wurde, hol das ausgewählte Item und 
        //setze den Aufrufstring zusammen
        if (radioList.isSelected()) {
            aufruf += cbStreams.getSelectionModel().getSelectedItem().getUrl();
            aufruf += " ";
            aufruf += cbQuality.getSelectionModel().getSelectedItem();
            //Sonst hole die URL und setze den Aufrufstring zusammen.
        } else {
            aufruf += textfieldInput.getText();
            aufruf += " ";
            aufruf += cbQuality.getSelectionModel().getSelectedItem();
        }
        //Kontrollausgabe Aufruf
//        System.out.println(aufruf);
        //Neue Runtime holen
        Runtime rt = Runtime.getRuntime();
        //Ausführen des Aufrufs
        try {
            rt.exec("cmd.exe /c start " + aufruf);
            //Beenden der LivestreamerGUI
            beenden();

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

    }

    /**
     * Steuert das Verhalten bei Aktivierung des Radiobuttons.
     *
     * @param event Auslösendes Event.
     */
    @FXML
    private void radioListAction(ActionEvent event) {
        textfieldInput.setDisable(true);
        cbStreams.setDisable(false);
    }

    /**
     * Steuert das Verhalten bei Aktivierung des Radiobuttons.
     *
     * @param event Auslösendes Event.
     */
    @FXML
    private void radioTextAction(ActionEvent event) {
        textfieldInput.setDisable(false);
        cbStreams.setDisable(true);
    }

    /**
     * Beendet die Livestreamer GUI.
     *
     * @param event Auslösendes Event.
     */
    @FXML
    private void btnEndAction(ActionEvent event) {
        beenden();
    }

    /**
     * Beendet das Programm.
     */
    private void beenden() {
        Platform.exit();
        System.exit(0);
    }

    /**
     * Behandelt das drücken der Enter-Taste im URL Textfeld.
     *
     * @param event Auslösendes Event.
     */
    @FXML
    private void textfeldInputKey(KeyEvent event) {
        //Bei Entertaste im Textfeld wird so geöffnet.
        if (event.getCode().equals(KeyCode.ENTER)) {
            btnStartAction(new ActionEvent());
        }
    }

    /**
     * Steuert das Verhalten bei Auswahl eines Streams.
     * Icons von https://www.iconfinder.com/designmodo.
     * @param event Auslösendes Event.
     */
    @FXML
    private void cbStreamsAction(ActionEvent event) {
        //Prüft, ob der der ausgewählte Stream online ist.
        if (cbStreams.getSelectionModel().getSelectedItem().istStreamOnline()) {
            imageView.setImage(new Image("/checkmark.png"));
        } else {
            imageView.setImage(new Image("/cross.png"));
        }
    }
}

package livestreamergui;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import model.Stream;

/**
 * Diese Klasse bietet die Mainmethode der Livestreamer GUI.
 *
 * @author Simon Mönkebüscher
 * @since 2016-06-11 Erstellung
 * @since 2016-06-14 Doku vervollständigt
 */
public class LivestreamerGUI extends Application {

    /**
     * Titel der Applikation.
     */
    public static final String TITEL = "Livestreamer GUI - Version 1.1";

    /**
     * Collection der Streams. Wird beim Softwarestart befüllt.
     */
    private static final ObservableList<model.Stream> STREAMS
            = FXCollections.observableArrayList();

    /**
     * Statische Variable der Stage.
     */
    private static Stage staticStage;

    @Override
    public void start(Stage stage) throws Exception {
        //Einlesen der Streamsdatei
        leseStreamsEin();
        //Einlesen FXML
        Parent root = FXMLLoader.load(getClass()
                .getResource("livestreamergui.fxml"));
        //Übergeben der Instanz
        staticStage = stage;
        //icon setzen
        staticStage.getIcons().add(new Image("/icon.png"));
        Scene scene = new Scene(root);
        //titel setzen
        staticStage.setTitle(TITEL);
        //szene setzen
        staticStage.setScene(scene);
        //stage anzeigen
        staticStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Liest die Streams aus der Streamdatei ein.
     */
    private void leseStreamsEin() {
        //Stringfeld für den Pfad zur JAR-Datei
        String pfad = "";
        try {
            //Holt den Pfad der GUI Klasse
            File jarPfad = new File(LivestreamerGUI.class.getProtectionDomain()
                    .getCodeSource().getLocation().getPath()
                    .replaceAll("%20", " "));
            //Pfad zur JAR-Datei
            pfad = jarPfad.getParentFile().getAbsolutePath();

            //BufferedReader, der den InputStream übergeben kriegt.
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    new FileInputStream(pfad + "/streams.txt")));
            //StringTokenizer
            StringTokenizer st;
            //Trennzeichen die vom StringTokenizer erkannt werden sollen
            String trenner = "+=";
            //Enthält den Kompletten eingelesenen Text
            String speicher = "";
            //Wird genutzt um das aktuelle Token zu Speichern
            String hilfsvariable;
            //Wird beim einlesen der Zeilen verwendet.
            String zeile;
            try {
                //Liest die komplette Datei ein und speichert sie
                while ((zeile = reader.readLine()) != null) {
                    speicher += zeile;
                    speicher += "+";
                }
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
            //Instanzierung des StringTokenizer mit den gespeicherten
            //Zeilen und dem Trenner
            st = new StringTokenizer(speicher, trenner);

            //Iteriere über die Elemente des StringTokenizers
            while (st.hasMoreElements()) {
                //Weist das aktuelle Token der Hilfsvariable zu
                hilfsvariable = st.nextToken();
                model.Stream stream = new Stream();

                //Kommentare werden ignoriert
                if (!hilfsvariable.startsWith("#")) {
                    //String splitten
                    String[] gesplittet = hilfsvariable.split("\\s+");
                    //Teil 1 des Splits: Streamname
                    stream.setName(gesplittet[0]);
                    //Teil 2 des Splits: Streamurl
                    stream.setUrl(gesplittet[1]);
                    //Zur Collection hinzufügen
                    LivestreamerGUI.STREAMS.add(stream);
                }
            }
        } catch (FileNotFoundException ex) {
            //Da keine Streamdatei gefunden wurde, wird eine
            //Standardkonfiguration erstellt.
            erstelleStandardStreamsDatei(pfad);
        }

    }

    /**
     * Gibt die Collection mit Streams zurück.
     *
     * @return ObservableList mit Streams.
     */
    public static ObservableList<Stream> getStreams() {
        return STREAMS;
    }

    /**
     * Erstellt eine Standardstreamdatei. Wird im übergebenem Pfad angelegt.
     *
     * @param pfad Pfad der JAR Datei.
     */
    private void erstelleStandardStreamsDatei(String pfad) {
        //Lineseparator des Betriebssystems holen
        String newline = System.getProperty("line.separator");
        //Erzeugt eine Standarddatei mit Anweisungen und Beispielstream, 
        //der immer verfügbar sein sollte.
        String inhalt = ""
                + "# Streams im Format \"Name URL\" eintragen, z.B. "
                + "\"Rocketbeans twitch.tv/rocketbeanstv\""
                + newline
                + "# Keine Leerzeichen im Streamnamen nutzen!"
                + newline
                + "# Zeilen mit Hashtag werden ignoriert."
                + newline
                + "# Bei Programmstart wird diese Liste eingelesen."
                + newline
                + "Rocketbeans twitch.tv/rocketbeanstv";

        try {
            //Neue Datei, die erzeugt wird
            File datei = new File(pfad
                    + "/streams.txt");
            //Es wird eine neue Datei erzeugt.
            //Methode wird nur aufgerufen, wenn die Datei nicht vorhanden ist
            datei.createNewFile();

            //Nutze den Filewriter unb BufferedWriter, um die Datei zu schreiben
            FileWriter fw = new FileWriter(datei.getAbsoluteFile());
            try (BufferedWriter bw = new BufferedWriter(fw)) {
                bw.write(inhalt);
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        //Streamdatei geschrieben, also lese die Streamdatei ein.
        leseStreamsEin();

    }

}

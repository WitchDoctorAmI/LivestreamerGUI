package model;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.Scanner;

/**
 * Ein Objekt dieser Klasse repräsentiert einen Stream bestehend aus Namen und
 * URL.
 *
 * @author Simon Mönkebüscher
 * @since 2016-06-11 Erstellung
 * @since 2016-06-14 Doku vervollständigt
 */
public class Stream {

    /**
     * Name des Stream.
     */
    private String name;
    /**
     * URL des Stream.
     */
    private String url;

    /**
     * Konstruktor für vollständiges Sreamobjekt.
     *
     * @param name Name des Streams
     * @param url URL des Streams
     */
    public Stream(String name, String url) {
        this.url = url;
        this.name = name;
    }

    /**
     * Konstruktor für Leeres Streamobjekt.
     */
    public Stream() {
    }

    /**
     * Prüft, ob der Stream online ist.
     *
     * @return Liefert True, falls der Stream online ist.
     */
    public boolean istStreamOnline() {
        //Initialisierung Rückgabevariable
        boolean istOnline;
        //Holt den Channelnamen des Channels
        String channelname = this.getUrl()
                .substring(url.lastIndexOf("/") + 1, url.length());

        //API Adresse
        String channelUrl = "https://api.twitch.tv/kraken/streams/"
                + channelname;

        //Variable für den JSON Text
        String jsonText = null;

        try {
            //Holt den aktuellen JSON String über die Twitch API
            jsonText = readFromUrl(channelUrl);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        //Neuen Parser holen
        JsonParser parser = new JsonParser();
        //Parst den JSON Text als JSON Objekt
        JsonObject json = parser.parse(jsonText).getAsJsonObject();

        //Setzen des Onlinestatus
        istOnline = !json.get("stream").isJsonNull();

        //Rückgabe Ergebnis
        return istOnline;
    }

    /**
     * Ruft die angegebene URL auf und liest den Text den die URL liefert.
     *
     * @param url URL die aufgerufen werden soll.
     * @return Kopierter Text.
     * @throws IOException Wird geworfen, falls ein Zugriffsfehler auftritt.
     */
    private static String readFromUrl(String url) throws IOException {
        //URL
        URL seite = new URL(url);
        //Neuer Stringbuilder
        StringBuilder stringbuilder = new StringBuilder();
        //Greift mit dem Scanner auf die URL zu und baut einen String
        //aus dem Text.
        try (Scanner scanner = new Scanner(seite.openStream(),
                StandardCharsets.UTF_8.name())) {
            while (scanner.hasNextLine()) {
                stringbuilder.append(scanner.nextLine());
            }
        }
        //Rückgabe Ergebnisstring
        return stringbuilder.toString();
    }

    /**
     * Getter für Namen.
     *
     * @return Name
     */
    public String getName() {
        return name;
    }

    /**
     * Setter für Namen.
     *
     * @param name Name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Getter für URL.
     *
     * @return URL
     */
    public String getUrl() {
        return url;
    }

    /**
     * Setter für URL.
     *
     * @param url URL.
     */
    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 89 * hash + Objects.hashCode(this.name);
        hash = 89 * hash + Objects.hashCode(this.url);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Stream other = (Stream) obj;
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        if (!Objects.equals(this.url, other.url)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return this.name;
    }

    public void setOnlineStatus() {
        if (this.istStreamOnline()) {
            this.setName(this.getName() + " (online)");
        }
    }

}

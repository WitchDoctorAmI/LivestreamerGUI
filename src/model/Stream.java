package model;

import java.util.Objects;

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

}

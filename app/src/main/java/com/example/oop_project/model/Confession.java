/**
 * @author Renato Burgos Hidalgo
 */
package com.example.oop_project.model;

import com.google.firebase.firestore.FirebaseFirestore;
import java.sql.Timestamp;

/**
 * Clase que guarda los atributos de una confesión.
 */
public class Confession {
    /**
     * Texto de cada confesión.
     */
    private String confession;
    /**
     * Email de quien creó la confesión.
     */
    private String email;
    /**
     * El estado de una confesión. Si es anónima o no.
     */
    private boolean isAnon;
    /**
     * El número de likes de cada confesión.
     */
    private int likes;
    /**
     * El número de dislikes de cada confesión.
     */
    private int dislikes;
    /**
     * Fecha en que cada confesión fue publicada.
     */
    private Timestamp time;
    /**
     * Para tener acceso al nombre del documento en cada objeto.
     */
    private String document;
    /**
     * Instancia para operar sobre la base de datos.
     */
    private FirebaseFirestore db;

    /**
     * Constructor vacío. Al parecer es importante.
     */
    public Confession() {

    }

    /**
     * Constructor para crear las confesiones.
     * @param email
     * @param confession
     * @param isAnon
     * @param time
     */
    public Confession(String email, String confession, boolean isAnon, Timestamp time) {
        this.email = email;
        this.confession = confession;
        this.isAnon = isAnon;
        this.likes = 0; this.dislikes = 0;
        this.time = time;
        db = FirebaseFirestore.getInstance();
    }

    /**
     * Constructor para obtener las confesiones en MainActivity.
     * @param email
     * @param confession
     * @param isAnon
     * @param likes
     * @param dislikes
     * @param time
     * @param document
     */
    public Confession(String email, String confession, boolean isAnon, int likes, int dislikes, Timestamp time, String document) {
        this.email = email;
        this.confession = confession;
        this.isAnon = isAnon;
        this.likes = likes;
        this.dislikes = dislikes;
        this.time = time;
        this.document = document;
        db = FirebaseFirestore.getInstance();
    }

    public String getEmail() {
        return email;
    }
    public String getConfession() {
        return confession;
    }
    public boolean isAnon() {
        return isAnon;
    }
    public int getLikes() {
        return likes;
    }
    public int getDislikes() {
        return dislikes;
    }
    public Timestamp getTime() {
        return time;
    }
    public String getDocument() {
        return document;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public void setConfession(String confession) {
        this.confession = confession;
    }
    public void setAnon(boolean isAnon) {
        this.isAnon = isAnon;
    }

    /**
     * Establece el número de likes en el objeto y en Firebase.
     * @param likes número de likes. Se va sumando de uno en uno.
     */
    public void setLikes(int likes) {
        this.likes = likes;
        db.collection("confessions").document(getDocument()).update(
                "likes", this.likes);
    }

    /**
     * Establece el número de dislikes en el objeto y en Firebase.
     * @param dislikes número de dislikes. Se va sumando de uno en uno.
     */
    public void setDislikes(int dislikes) {
        this.dislikes = dislikes;
        db.collection("confessions").document(getDocument()).update(
                "dislikes", this.dislikes);
    }
    public void setTime(Timestamp time) {
        this.time = time;
    }
    public void setDocument(String document) {
        this.document = document;
    }
}

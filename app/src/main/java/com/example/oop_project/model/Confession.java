package com.example.oop_project.model;

import android.util.Log;

import com.google.firebase.firestore.FirebaseFirestore;

import java.sql.Time;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Confession {
    private String confession;
    private String email;
    private boolean isAnon;
    private int likes;
    private int dislikes;
    private Timestamp time;
    private String document;
    private FirebaseFirestore db;

    public Confession() {

    }

    public Confession(String email, String confession, boolean isAnon, Timestamp time) {
        this.email = email;
        this.confession = confession;
        this.isAnon = isAnon;
        this.likes = 0; this.dislikes = 0;
        this.time = time;
        db = FirebaseFirestore.getInstance();
    }

    public Confession(String email, String confession, boolean isAnon, int likes, int dislikes, Timestamp time, String document) {
        this.email = email;
        this.confession = confession;
        this.isAnon = isAnon;
        this.likes = likes;
        this.dislikes = dislikes;
        this.time = time;
        this.document = document;
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
    public void setLikes(int likes) {
        this.likes = likes;
        db.collection("confessions").document(getDocument())
                .update(
                        "likes", this.likes
                );
    }
    public void setDislikes(int dislikes) {
        this.dislikes = dislikes;
        db.collection("confessions").document(getDocument())
                .update(
                        "dislikes", this.dislikes
                );
    }
    public void setTime(Timestamp time) {
        this.time = time;
    }
    public void setDocument(String document) {
        this.document = document;
    }
}

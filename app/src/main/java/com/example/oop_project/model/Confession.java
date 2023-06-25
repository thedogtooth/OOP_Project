package com.example.oop_project.model;

import android.util.Log;

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

    public Confession() {

    }

    public Confession(String email, String confession, boolean isPrivate, Timestamp time) {
        this.email = email;
        this.confession = confession;
        this.isAnon = isPrivate;
        this.likes = 0; this.dislikes = 0;
        this.time = time;
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
    }
    public void setDislikes(int dislikes) {
        this.dislikes = dislikes;
    }
    public void setTime(Timestamp time) {
        this.time = time;
    }
}

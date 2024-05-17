package com.example.oop_project.model;

import java.sql.Timestamp;

public class Dislike {
    private String email;
    private String post;
    private Timestamp time;
    private boolean disliked;

    public Dislike() {

    }

    public Dislike(String email, String post, Timestamp time) {
        this.email = email;
        this.post = post;
        this.time = time;
        this.disliked = true;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPost(String post) {
        this.post = post;
    }
    public void setTime(Timestamp time) {
        this.time = time;
    }

    public void setDisliked(boolean disliked) {
        this.disliked = disliked;
    }

    public String getEmail() {
        return email;
    }

    public String getPost() {
        return post;
    }

    public Timestamp getTime() {
        return time;
    }

    public boolean getDisliked() {
        return disliked;
    }
}

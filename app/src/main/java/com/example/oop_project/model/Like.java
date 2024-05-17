package com.example.oop_project.model;

import java.sql.Timestamp;
public class Like {
    private String email;
    private String post;
    private boolean liked;
    private Timestamp time;
    public Like() {

    }

    public Like(String email, String post, Timestamp time) {
        this.email = email;
        this.post = post;
        this.time = time;
        this.liked = true;
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

    public void setLiked(boolean liked) {
        this.liked = liked;
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

    public boolean getLiked() {
        return liked;
    }

}

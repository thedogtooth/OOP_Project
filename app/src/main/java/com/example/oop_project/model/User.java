package com.example.oop_project.model;

public class User {
    private String email;
    private int phone;
    private String campus;
    private String password;
    private String username;

    public User(){

    }

    public User(String email, String password){
        this.email = email;
        this.password = password;
    }

    public User(String email, int phone, String campus, String username){
        this.email = email;
        this.phone = phone;
        this.campus = campus;
        this.username = username;
    }

    public String getUsername(){return username;}
    public void setUsername(String username){
        this.username = username;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public int getPhone() {return phone;}
    public void setPhone(int phone) {this.phone = phone;}
    public String getCampus() {
        return campus;
    }
    public void setCampus(String campus) {
        this.campus = campus;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
}

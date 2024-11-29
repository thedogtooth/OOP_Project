/**
 * @author Renato Burgos Hidalgo
 */
package com.example.oop_project.model;

/**
 * Clase para establecer cada usuario de la aplicación.
 */
public class User {
    private String Id;
    /**
     * Email del usuario.
     */
    private String email;
    /**
     * Teléfono del usuario.
     */
    private int phone;
    /**
     * Campus del usuario.
     */
    private String campus;
    /**
     * Contraseña del usuario.
     */
    private String password;
    /**
     * Nombre de usuario.
     */
    private String username;
    /**
     * Variable booleana para saber si el usuario fue eliminado.
     */
    private boolean deleted;

    /**
     * Constructor vacío.
     */
    public User(){

    }

    /**
     * Constructor que solo necesita el email y la contraseña.
     * @param email
     * @param password
     */
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

    /**
     * Constructor para crear cada usuario. Cada objeto se sube a la base de datos.
     * @param email
     * @param phone
     * @param campus
     * @param username
     */
    public User(String Id, String email, int phone, String campus, String username){
        this.Id = Id;
        this.email = email;
        this.phone = phone;
        this.campus = campus;
        this.username = username;
    }

    public String getId(){return Id;}
    public void setId(String Id){
        this.Id = Id;
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
    public boolean getDeleted(){
        return deleted;
    }
    public void setDeleted(boolean deleted){
        this.deleted = deleted;
    }
}

package es.ucm.fdi.tieryourlikes.model;

import android.graphics.drawable.Icon;
import android.widget.ImageView;

public class User {

    private String username;
    private String password;
    private String email;
    private String iconURL;
    private String creation_time;
    private String rol;

    public User(String username, String password, String email, String iconURL, String creation_time, String rol) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.iconURL = iconURL;
        this.creation_time = creation_time;
        this.rol = rol;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getIconURL() { return iconURL; }

    public void setIconURL(String iconURL) { this.iconURL = iconURL; }

    public String getCreationTime() {
        return creation_time;
    }

    public void setCreationTime(String creation_time) {
        this.creation_time = creation_time;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", iconURL='" + iconURL + '\'' +
                ", creation_time='" + creation_time + '\'' +
                ", rol='" + rol + '\'' +
                '}';
    }
}

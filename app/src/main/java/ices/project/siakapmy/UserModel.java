package ices.project.siakapmy;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class UserModel {
    @PrimaryKey
    public int id;

    private String username;
    private String passwordhash;

    public UserModel(int id, String username, String passwordhash) {
        this.id = id;
        this.username = username;
        this.passwordhash = passwordhash;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswordhash() {
        return passwordhash;
    }

    public void setPasswordhash(String passwordhash) {
        this.passwordhash = passwordhash;
    }
}

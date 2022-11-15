package vttp.finalproject.backend.models;

import java.io.StringReader;

import org.springframework.jdbc.support.rowset.SqlRowSet;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;

public class AppUser {

    Integer id;
    String name;
    String username;
    String email;
    String password;
    
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }


    public static AppUser create(String json) {

        AppUser appUser = new AppUser();

        JsonReader reader = Json.createReader(new StringReader(json));
        JsonObject data = reader.readObject();

        appUser.setName(data.getString("name"));
        appUser.setUsername(data.getString("username"));
        appUser.setEmail(data.getString("email"));
        appUser.setPassword(data.getString("password"));
        
        return appUser;
    }

    public static AppUser populate(SqlRowSet rs) {

        AppUser appUser = new AppUser();

        appUser.setId(rs.getInt("user_id"));
        appUser.setName(rs.getString("name"));
        appUser.setEmail(rs.getString("email"));
        appUser.setUsername(rs.getString("username"));

        return appUser;
    }

    public static JsonObject toJson(AppUser appUser) {
        return Json.createObjectBuilder()
            .add("id", appUser.getId())
            .add("username", appUser.getUsername())
            .add("name", appUser.getName())
            .add("email", appUser.getEmail())
            .build();
    }
}

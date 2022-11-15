package vttp.finalproject.backend.models;

import java.io.StringReader;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;

public class Post {
    
    String text;
    Integer user_id;
    
    public String getText() { return text; }
    public void setText(String text) { this.text = text; }

    public Integer getUser_id() { return user_id; }
    public void setUser_id(Integer user_id) { this.user_id = user_id; }
    
    public static Post create(String json) {

        Post tweet = new Post();

        JsonReader reader = Json.createReader(new StringReader(json));
        JsonObject data = reader.readObject();

        tweet.setText(data.getString("text"));
        tweet.setUser_id(data.getInt("user_id"));

        return tweet;
    }
}

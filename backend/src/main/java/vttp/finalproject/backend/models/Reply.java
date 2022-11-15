package vttp.finalproject.backend.models;

import java.io.StringReader;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;

public class Reply {
    
    String text;
    Integer user_id;
    Integer tweet_id;
    
    public String getText() { return text; }
    public void setText(String text) { this.text = text; }

    public Integer getUser_id() { return user_id; }
    public void setUser_id(Integer user_id) { this.user_id = user_id; }

    public Integer getTweet_id() { return tweet_id; }
    public void setTweet_id(Integer tweet_id) { this.tweet_id = tweet_id; }
    
    
    public static Reply create(String json) {

        Reply reply = new Reply();

        JsonReader reader = Json.createReader(new StringReader(json));
        JsonObject data = reader.readObject();

        reply.setText(data.getString("reply"));
        reply.setUser_id(data.getInt("user_id"));
        reply.setTweet_id(data.getInt("tweet_id"));

        return reply;
    }
}

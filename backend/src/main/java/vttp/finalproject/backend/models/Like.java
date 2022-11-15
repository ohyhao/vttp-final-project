package vttp.finalproject.backend.models;

import java.io.StringReader;

import org.springframework.jdbc.support.rowset.SqlRowSet;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;

public class Like {
    
    private Integer id;
    private Integer tweet_id;
    private Integer user_id;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id;}

    public Integer getTweet_id() { return tweet_id; }
    public void setTweet_id(Integer tweet_id) { this.tweet_id = tweet_id; }

    public Integer getUser_id() { return user_id; }
    public void setUser_id(Integer user_id) { this.user_id = user_id; }

    public static Like populate(SqlRowSet rs) {

        Like like = new Like();
        like.setId(rs.getInt("id"));
        like.setTweet_id(rs.getInt("tweet_id"));
        like.setUser_id(rs.getInt("user_id"));
        return like;
    }

    public static JsonObject toJson(Like like) {
        return Json.createObjectBuilder()
            .add("id", like.getId())
            .add("tweet_id", like.getTweet_id())
            .add("user_id", like.getUser_id())
            .build();
    }

    public static Like create(String json) {

        Like like = new Like();

        JsonReader reader = Json.createReader(new StringReader(json));
        JsonObject data = reader.readObject();

        like.setTweet_id(data.getInt("tweet_id"));
        like.setUser_id(data.getInt("user_id"));
        
        return like;
    }
  
    
    
}

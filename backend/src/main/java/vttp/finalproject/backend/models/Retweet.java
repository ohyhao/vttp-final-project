package vttp.finalproject.backend.models;

import java.io.StringReader;

import org.springframework.jdbc.support.rowset.SqlRowSet;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;

public class Retweet {
    
    private Integer id;
    private Integer tweet_id;
    private Integer user_id;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id;}

    public Integer getTweet_id() { return tweet_id; }
    public void setTweet_id(Integer tweet_id) { this.tweet_id = tweet_id; }

    public Integer getUser_id() { return user_id; }
    public void setUser_id(Integer user_id) { this.user_id = user_id; }

    public static Retweet populate(SqlRowSet rs) {

        Retweet retweet = new Retweet();
        retweet.setId(rs.getInt("id"));
        retweet.setTweet_id(rs.getInt("tweet_id"));
        retweet.setUser_id(rs.getInt("user_id"));
        return retweet;
    }

    public static JsonObject toJson(Retweet retweet) {
        return Json.createObjectBuilder()
            .add("id", retweet.getId())
            .add("tweet_id", retweet.getTweet_id())
            .add("user_id", retweet.getUser_id())
            .build();
    }

    public static Retweet create(String json) {

        Retweet retweet = new Retweet();
        
        JsonReader reader = Json.createReader(new StringReader(json));
        JsonObject data = reader.readObject();

        retweet.setTweet_id(data.getInt("tweet_id"));
        retweet.setUser_id(data.getInt("user_id"));

        return retweet;
    }
}

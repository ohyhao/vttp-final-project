package vttp.finalproject.backend.models;

import org.springframework.jdbc.support.rowset.SqlRowSet;

import jakarta.json.Json;
import jakarta.json.JsonObject;

public class Tweet {
    
    Integer id;
    String tweet;
    Integer likes;
    Integer replies;
    Integer retweets;
    String date_created;
    Integer tweet_id;
    Integer user_id;
    String username;
    String name;
    
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getTweet() { return tweet; }
    public void setTweet(String tweet) { this.tweet = tweet; }

    public Integer getLikes() { return likes; }
    public void setLikes(Integer likes) { this.likes = likes; }

    public Integer getReplies() { return replies; }
    public void setReplies(Integer replies) { this.replies = replies; }

    public Integer getRetweets() { return retweets; }
    public void setRetweets(Integer retweets) { this.retweets = retweets; }

    public String getDate_created() { return date_created; }
    public void setDate_created(String date_created) { this.date_created = date_created.substring(0, 10); }
    
    public Integer getTweet_id() { return tweet_id; }
    public void setTweet_id(Integer tweet_id) { this.tweet_id = tweet_id; }
    
    public Integer getUser_id() { return user_id; }
    public void setUser_id(Integer user_id) { this.user_id = user_id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public static Tweet populate(SqlRowSet rs) {

        Tweet tweet = new Tweet();
        tweet.setId(rs.getInt("id"));
        tweet.setTweet(rs.getString("tweet"));
        tweet.setLikes(rs.getInt("like_counter"));
        tweet.setReplies(rs.getInt("reply_counter"));
        tweet.setRetweets(rs.getInt("retweet_counter"));
        tweet.setDate_created(rs.getString("date_created"));
        tweet.setTweet_id(rs.getInt("tweet_id"));
        tweet.setUser_id(rs.getInt("user_id"));
        return tweet;
    }

    public static JsonObject toJson(Tweet tweet) {
        return Json.createObjectBuilder()
            .add("id", tweet.getId())
            .add("tweet", tweet.getTweet())
            .add("likes", tweet.getLikes())
            .add("replies", tweet.getReplies())
            .add("retweets", tweet.getRetweets())
            .add("date_created", tweet.getDate_created())
            .add("tweet_id", tweet.getTweet_id())
            .add("user_id", tweet.getUser_id())
            .add("username", tweet.getUsername())
            .add("name", tweet.getName())
            .build();
    }
    
  
    
    
}

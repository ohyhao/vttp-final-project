package vttp.finalproject.backend.repositories;

public class Queries {

    public static final String SQL_FIND_USER_BY_ID =
        "select * from users where user_id = ?";

    public static final String SQL_FIND_USER_BY_EMAIL =
        "select * from users where email = ?";
    
    public static final String SQL_FIND_USER_BY_USERNAME = 
        "select * from users where username = ?";

    public static final String SQL_INSERT_USER =
        "insert into users (username, name, email, password) values (?,?,?,?)";

    public static final String SQL_INSERT_TWEET_BY_ID =
        "insert into tweets (tweet, date_created, user_id) values (?,?,?)";

    public static final String SQL_INSERT_REPLY_BY_ID =
        "insert into tweets (tweet, date_created, user_id, tweet_id) values (?,?,?,?)";

    public static final String SQL_FIND_TWEET_BY_ID = 
        "select * from tweets where id = ?";

    public static final String SQL_GET_ALL_TWEETS = 
        "select * from tweets where tweet_id is null order by date_created desc";

    public static final String SQL_DELETE_TWEET_BY_ID =
        "delete from tweets where id = ?";

    public static final String SQL_FIND_TWEETS_BY_USER_ID = 
        "select * from tweets where user_id = ? and tweet_id is null order by date_created desc";

    public static final String SQL_FIND_LIKES_BY_USER_ID =
        "select * from likes where user_id = ?";
        
    public static final String SQL_FIND_LIKE_BY_USER_ID = 
        "select * from likes where tweet_id = ? and user_id = ?";
        
    public static final String SQL_INSERT_LIKE = 
        "insert into likes (tweet_id, user_id) values (?, ?)";
    
    public static final String SQL_DELETE_LIKE = 
        "delete from likes where tweet_id = ? and user_id = ?";
        
    public static final String SQL_GET_LIKE_COUNT_BY_TWEET_ID =
        "select count(tweet_id) from likes where tweet_id = ?";

    public static final String SQL_UPDATE_LIKE_COUNT_BY_TWEET_ID =
        "update tweets set like_counter = ? where id = ?";

    public static final String SQL_FIND_REPLIES_BY_USER_ID =
        "select * from tweets where user_id = ? and tweet_id is not null order by date_created desc";
    
    public static final String SQL_FIND_REPLIES_BY_ID =
        "select * from tweets where tweet_id = ? order by date_created desc";

    public static final String SQL_FIND_RETWEETS_BY_USER_ID = 
        "select * from retweets where user_id = ?";
    
    public static final String SQL_FIND_RETWEET_BY_USER_ID = 
        "select * from retweets where tweet_id = ? and user_id = ?";
    
    public static final String SQL_INSERT_RETWEET = 
        "insert into retweets (tweet_id, user_id) values (?, ?)";
    
    public static final String SQL_DELETE_RETWEET = 
        "delete from retweets where tweet_id = ? and user_id = ?";

    public static final String SQL_UPDATE_RETWEET_COUNT_BY_TWEET_ID =
        "update tweets set retweet_counter = ? where id = ?";
    
    public static final String SQL_UPDATE_REPLY_COUNT_BY_TWEET_ID =
        "update tweets set reply_counter = ? where id = ?";
    
    public static final String SQL_GET_TOTAL_TWEET_COUNT_BY_ID =
        "select (select count(*) from tweets where user_id = ?) + (select count(*) from retweets where user_id = ?) as count";

    public static final String SQL_SEARCH_TWEETS =
        "select * from tweets where tweet like ? order by date_created desc";
    
}

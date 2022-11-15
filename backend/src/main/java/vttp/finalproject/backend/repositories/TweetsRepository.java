package vttp.finalproject.backend.repositories;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import vttp.finalproject.backend.models.Count;
import vttp.finalproject.backend.models.Tweet;

import static vttp.finalproject.backend.repositories.Queries.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public class TweetsRepository {

    @Autowired
    private JdbcTemplate template;

    public boolean createTweet(String text, Integer user_id) {
        
        int count = template.update(SQL_INSERT_TWEET_BY_ID, text, new Date(System.currentTimeMillis()), user_id);
        return count == 1;
    }

    public boolean replyTweet(String text, Integer user_id, Integer tweet_id) {

        int count = template.update(SQL_INSERT_REPLY_BY_ID, text, new Date(System.currentTimeMillis()), user_id, tweet_id);
        return count == 1;
    }

    public boolean deleteTweet(Integer id) {
        int count = template.update(SQL_DELETE_TWEET_BY_ID, id);
        return count == 1;
    }

    public Optional<Tweet> findTweetById(Integer id) {

        final SqlRowSet rs = template.queryForRowSet(SQL_FIND_TWEET_BY_ID, id);
        while (!rs.next()) {
            return Optional.empty();
        }
        return Optional.of(Tweet.populate(rs));
    }

    public List<Tweet> findTweetsByUserId (Integer user_id) {

        List<Tweet> tweets = new ArrayList<>();
        final SqlRowSet rs = template.queryForRowSet(SQL_FIND_TWEETS_BY_USER_ID, user_id);
        while (rs.next()) {

            Tweet tweet = Tweet.populate(rs);
            tweets.add(tweet);
        }
        populateUserDetails(tweets);
        return tweets;
    }

    public List<Tweet> findRepliesById (Integer user_id) {

        List<Tweet> replies = new ArrayList<>();
        final SqlRowSet rs = template.queryForRowSet(SQL_FIND_REPLIES_BY_USER_ID, user_id);
        while (rs.next()) {

            Tweet reply = Tweet.populate(rs);
            replies.add(reply);
        }
        populateUserDetails(replies);
        return replies;
    }

    public List<Tweet> findRepliesForTweet (Integer id) {

        List<Tweet> repliesForTweet = new ArrayList<>();
        final SqlRowSet rs = template.queryForRowSet(SQL_FIND_REPLIES_BY_ID, id);
        while (rs.next()) {

            Tweet reply = Tweet.populate(rs);
            repliesForTweet.add(reply);
        }
        populateUserDetails(repliesForTweet);
        return repliesForTweet;
    }

    public boolean updateTweetLikes (Integer likes, Integer id) {

        int count = template.update(SQL_UPDATE_LIKE_COUNT_BY_TWEET_ID, likes, id);
        return count == 1;
    }

    public boolean updateTweetRetweets (Integer retweets, Integer id) {

        int count = template.update(SQL_UPDATE_RETWEET_COUNT_BY_TWEET_ID, retweets, id);
        return count == 1;
    }

    public boolean updateTweetReplies (Integer replies, Integer id) {

        int count = template.update(SQL_UPDATE_REPLY_COUNT_BY_TWEET_ID, replies, id);
        return count == 1;
    }

    public Optional<Count> findTweetCountById (Integer id) {

        final SqlRowSet rs = template.queryForRowSet(SQL_GET_TOTAL_TWEET_COUNT_BY_ID, id, id);
        while (!rs.next()) {
                return Optional.empty();
            }
        return Optional.of(Count.populate(rs));
    }

    public void populateUserDetails(List<Tweet> tweets) {

        for (int i = 0; i < tweets.size(); i++) {
            Tweet tweet = tweets.get(i);
            Integer user_id = tweet.getUser_id();
            SqlRowSet rs = template.queryForRowSet(SQL_FIND_USER_BY_ID, user_id);

            while (rs.next()) {
                tweet.setUsername(rs.getString("username"));
                tweet.setName(rs.getString("name"));
            }
        }
    }

    public void populateUserDetails(Tweet tweet) {

        Integer user_id = tweet.getUser_id();
        SqlRowSet rs = template.queryForRowSet(SQL_FIND_USER_BY_ID, user_id);

        while (rs.next()) {
            tweet.setUsername(rs.getString("username"));
            tweet.setName(rs.getString("name"));
            }
        }
}

package vttp.finalproject.backend.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import vttp.finalproject.backend.models.Count;
import vttp.finalproject.backend.models.Like;
import vttp.finalproject.backend.models.Retweet;
import vttp.finalproject.backend.models.Tweet;
import vttp.finalproject.backend.repositories.LikesRepository;
import vttp.finalproject.backend.repositories.RetweetsRepository;
import vttp.finalproject.backend.repositories.TweetsRepository;
import static vttp.finalproject.backend.repositories.Queries.*;

@Service
public class TweetService {

    @Autowired
    private JdbcTemplate template;

    @Autowired
    private TweetsRepository tweetRepo;

    @Autowired
    private LikesRepository likesRepo;

    @Autowired
    private RetweetsRepository retweetsRepo;

    public void createTweet(String text, Integer user_id) {

        try {
            boolean add = tweetRepo.createTweet(text, user_id);
            if (add != true)
                throw new IllegalArgumentException("Unable to create tweet");
        } catch (Exception ex) {
            ex.getStackTrace();
            throw ex;
        }
    }

    @Transactional
    public void deleteTweet(Integer id) {

        Optional<Tweet> optTweet = tweetRepo.findTweetById(id);
        if (optTweet.isEmpty()) {
            throw new IllegalArgumentException("Tweet does not exist");
        }
        Tweet tweet = optTweet.get();
        Integer tweet_id = tweet.getTweet_id();
        
        // List<Tweet> replies = tweetRepo.findRepliesForTweet(id);
        
        if (tweet_id != 0) {
            Optional<Tweet> optRepliedTweet = tweetRepo.findTweetById(tweet_id);
            Tweet repliedTweet = new Tweet();
            repliedTweet = optRepliedTweet.get();

            boolean add = tweetRepo.deleteTweet(id);
            repliedTweet.setReplies(repliedTweet.getReplies() - 1);
            tweetRepo.updateTweetReplies(repliedTweet.getReplies(), tweet_id);
            if (add != true)
                throw new IllegalArgumentException("Unable to delete tweet");
        } else {
            boolean add = tweetRepo.deleteTweet(id);
            if (add != true)
                throw new IllegalArgumentException("Unable to delete tweet");
        }
    }
    
    public List<Tweet> getAllTweets(Integer limit, Integer offset) {

        List<Tweet> tweets = new ArrayList<>();
        final SqlRowSet rs = template.queryForRowSet(SQL_GET_ALL_TWEETS, limit, offset);
        while (rs.next()) {
            Tweet tweet = Tweet.populate(rs);
            tweets.add(tweet);
        }

        tweetRepo.populateUserDetails(tweets);
        return tweets;
    }
    
    public Tweet getTweetById(Integer id) {
        
        Optional<Tweet> opt = tweetRepo.findTweetById(id);
        Tweet tweet = opt.get();

        tweetRepo.populateUserDetails(tweet);
        return tweet;
    }

    public List<Tweet> getTweetsById (Integer userId) {
        return tweetRepo.findTweetsByUserId(userId);
    }

    public List<Retweet> getRetweetsById (Integer userId) {
        return retweetsRepo.findRetweetsByUserId(userId);
    }

    public List<Like> getLikesById (Integer userId) {
        return likesRepo.findLikesByUserId(userId);
    }
    
    public List<Tweet> getRepliesById (Integer id) {
        return tweetRepo.findRepliesById(id);
    }
    
    public List<Tweet> getRepliesForTweet (Integer id) {
        return tweetRepo.findRepliesForTweet(id);
    }

    public Count getTweetCountById (Integer id) {
        Optional<Count> opt = tweetRepo.findTweetCountById(id);
        Count count = opt.get();
        return count;
    }

    @Transactional
    public Integer replyTweet(String text, Integer user_id, Integer tweet_id) {

        Optional<Tweet> optTweet = tweetRepo.findTweetById(tweet_id);
        if (optTweet.isEmpty()) {
            throw new IllegalArgumentException("Tweet does not exist");
        }

        Tweet tweet = optTweet.get();
        try {
            boolean add = tweetRepo.replyTweet(text, user_id, tweet_id);
            tweet.setReplies(tweet.getReplies() + 1);
            tweetRepo.updateTweetReplies(tweet.getReplies(), tweet_id);
            if (add != true)
                throw new IllegalArgumentException("Unable to reply tweet");
        } catch (Exception ex) {
            ex.getStackTrace();
            throw ex;
        }
        return 1;
        
    }

    @Transactional
    public Integer likeTweet(Integer tweetId, Integer userId) {
        
        Optional<Tweet> optTweet = tweetRepo.findTweetById(tweetId);
        if (optTweet.isEmpty()) {
            throw new IllegalArgumentException("Tweet does not exist");
        }
        Tweet tweet = optTweet.get();
        Optional<Like> optLike = likesRepo.findLikeByUserId(tweetId, userId);
        
        if (optLike.isPresent()) {
            tweet.setLikes(tweet.getLikes() - 1);
            tweetRepo.updateTweetLikes(tweet.getLikes(), tweetId);
            likesRepo.deleteLike(tweetId, userId);
            return -1;
        } else {
            tweet.setLikes(tweet.getLikes() + 1);
            tweetRepo.updateTweetLikes(tweet.getLikes(), tweetId);
            likesRepo.insertLike(tweetId, userId);
            return 1;
        }
    }

    @Transactional
    public Integer retweetTweet(Integer tweetId, Integer userId) {

        Optional<Tweet> optTweet = tweetRepo.findTweetById(tweetId);
        if (optTweet.isEmpty()) {
            throw new IllegalArgumentException("Tweet does not exist");
        }
        Tweet tweet = optTweet.get();
        Optional<Retweet> optRetweet = retweetsRepo.findRetweetByUserId(tweetId, userId);
        if (optRetweet.isPresent()) {
            tweet.setRetweets(tweet.getRetweets() - 1);
            tweetRepo.updateTweetRetweets(tweet.getRetweets(), tweetId);
            retweetsRepo.deleteRetweet(tweetId, userId);
            return -1;
        } else {
            tweet.setRetweets(tweet.getRetweets() + 1);
            tweetRepo.updateTweetRetweets(tweet.getRetweets(), tweetId);
            retweetsRepo.insertRetweet(tweetId, userId);
            return 1;
        }
    }

    
    
}

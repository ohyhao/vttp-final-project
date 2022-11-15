package vttp.finalproject.backend.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import vttp.finalproject.backend.models.Retweet;
import vttp.finalproject.backend.models.Tweet;

import static vttp.finalproject.backend.repositories.Queries.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class RetweetsRepository {
    
    @Autowired
    private JdbcTemplate template;

    public boolean insertRetweet (Integer tweet_id, Integer user_id) {

        int count = template.update(SQL_INSERT_RETWEET, tweet_id, user_id);
        return count == 1;
    }

    public List<Retweet> findRetweetsByUserId (Integer userId) {

        List<Retweet> retweets = new ArrayList<>();
        final SqlRowSet rs = template.queryForRowSet(SQL_FIND_RETWEETS_BY_USER_ID, userId);
        while(rs.next()) {
            Retweet retweet = Retweet.populate(rs);
            retweets.add(retweet);
        }
        return retweets;
    }

    public Optional<Retweet> findRetweetByUserId (Integer tweetId, Integer userId) {

        final SqlRowSet rs = template.queryForRowSet(SQL_FIND_RETWEET_BY_USER_ID, tweetId, userId);
        while(!rs.next()) {
            return Optional.empty();
        }
        return Optional.of(Retweet.populate(rs));
    }

    public boolean deleteRetweet (Integer tweet_id, Integer user_id) {

        int count = template.update(SQL_DELETE_RETWEET, tweet_id, user_id);
        return count == 1;
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

    
    
}

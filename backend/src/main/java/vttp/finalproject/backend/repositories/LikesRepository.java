package vttp.finalproject.backend.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import vttp.finalproject.backend.models.Like;

import static vttp.finalproject.backend.repositories.Queries.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class LikesRepository {

    @Autowired
    private JdbcTemplate template;

    public List<Like> findLikesByUserId (Integer userId) {
        
        List<Like> likes = new ArrayList<>();
        final SqlRowSet rs = template.queryForRowSet(SQL_FIND_LIKES_BY_USER_ID, userId);
        while (rs.next()) {
            Like like = Like.populate(rs);
            likes.add(like);
        }
        return likes;
    }

    public boolean insertLike (Integer tweetId, Integer userId) {

        int count = template.update(SQL_INSERT_LIKE, tweetId, userId);
        return count == 1;
    }

    public Optional<Like> findLikeByUserId (Integer tweetId, Integer userId) {

        final SqlRowSet rs = template.queryForRowSet(SQL_FIND_LIKE_BY_USER_ID, tweetId, userId);
        while(!rs.next()) {
            return Optional.empty();
        }
        return Optional.of(Like.populate(rs));
    }

    public boolean deleteLike (Integer tweetId, Integer userId) {

        int count = template.update(SQL_DELETE_LIKE, tweetId, userId);
        return count == 1;
    }

    
}

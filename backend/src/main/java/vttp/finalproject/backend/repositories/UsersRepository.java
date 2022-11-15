package vttp.finalproject.backend.repositories;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import vttp.finalproject.backend.models.AppUser;
import static vttp.finalproject.backend.repositories.Queries.*;


@Repository
public class UsersRepository {

    @Autowired
    private JdbcTemplate template;

    public Optional<AppUser> findByEmail(String email) {

        SqlRowSet rs = template.queryForRowSet(SQL_FIND_USER_BY_EMAIL, email);
        if (!rs.next()) {
            return Optional.empty();
        } else {
            AppUser appUser = new AppUser();
            appUser.setId(rs.getInt("user_id"));
            appUser.setName(rs.getString("name"));
            appUser.setUsername(rs.getString("username"));
            appUser.setEmail(rs.getString("email"));
            appUser.setPassword(rs.getString("password"));
    
            return Optional.of(appUser);
        }

    }

    public Optional<AppUser> findByUsername(String username) {

        SqlRowSet rs = template.queryForRowSet(SQL_FIND_USER_BY_USERNAME, username);
        if (!rs.next()) {
            return Optional.empty();
        } else {
            AppUser appUser = new AppUser();
            appUser.setId(rs.getInt("user_id"));
            appUser.setName(rs.getString("name"));
            appUser.setUsername(rs.getString("username"));
            appUser.setEmail(rs.getString("email"));
            appUser.setPassword(rs.getString("password"));
    
            return Optional.of(appUser);
        }
    }

    public boolean insertUser(String email, String name, String username, String password) {
        int count = template.update(SQL_INSERT_USER, username, name, email, password);  
        return count == 1;
    }
}

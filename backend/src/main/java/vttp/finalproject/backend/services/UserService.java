package vttp.finalproject.backend.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import vttp.finalproject.backend.models.AppUser;
import vttp.finalproject.backend.repositories.UsersRepository;
import static vttp.finalproject.backend.repositories.Queries.*;


@Service
public class UserService {

    @Autowired
    private JdbcTemplate template;

    @Autowired
    private UsersRepository userRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void addUser(String username, String name, String email, String password) {

        String encoded = passwordEncoder.encode(password);

        System.out.println("encoded: %s".formatted(encoded));
        
        Optional<AppUser> opt = userRepo.findByEmail(email);
        if (opt.isPresent()) {
            throw new IllegalArgumentException("%s is already used, please use other email".formatted(email));
        } else {
            try {
                boolean add = userRepo.insertUser(email, name, username, encoded);
                if (add != true)
                    throw new IllegalArgumentException("Unable to create user! Please try again!");
            } catch (Exception ex) {
                ex.getStackTrace();
                throw ex;
            }
        }
    }

    public AppUser getUserDetailsByUsername(String username) {

        AppUser appUser = new AppUser();
        SqlRowSet rs = template.queryForRowSet(SQL_FIND_USER_BY_USERNAME, username);
        while (rs.next()) {
            appUser = AppUser.populate(rs);
        }

        return appUser;
    }

    public AppUser getUserDetailsByEmail(String email) {

        AppUser appUser = new AppUser();
        SqlRowSet rs = template.queryForRowSet(SQL_FIND_USER_BY_EMAIL, email);
        while (rs.next()) {
            appUser = AppUser.populate(rs);
        }

        return appUser;
    }
    
}

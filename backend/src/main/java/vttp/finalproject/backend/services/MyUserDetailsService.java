package vttp.finalproject.backend.services;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import vttp.finalproject.backend.models.AppUser;
import vttp.finalproject.backend.repositories.UsersRepository;


@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private UsersRepository userRepo;
    
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        
        Optional<AppUser> opt = userRepo.findByEmail(email);

        if (opt.isEmpty()) {
            throw new UsernameNotFoundException("User not found with username: " + email);
        }
        AppUser appUser = opt.get();
               
        return new User(appUser.getEmail(), appUser.getPassword(), new ArrayList<>());
    }
}

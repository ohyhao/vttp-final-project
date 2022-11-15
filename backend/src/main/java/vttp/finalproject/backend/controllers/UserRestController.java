package vttp.finalproject.backend.controllers;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import vttp.finalproject.backend.models.AppUser;
import vttp.finalproject.backend.models.AuthenticationRequest;
import vttp.finalproject.backend.models.Response;
import vttp.finalproject.backend.services.EmailService;
import vttp.finalproject.backend.services.MyUserDetailsService;
import vttp.finalproject.backend.services.UserService;
import vttp.finalproject.backend.utils.JwtUtil;

@RestController
@RequestMapping(path="/api", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserRestController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private MyUserDetailsService userDetailsService;

    @Autowired
    private UserService userSvc;

    @Autowired
    private EmailService emailSvc;

    @Autowired
    private JwtUtil jwtTokenUtil;

    @Autowired
    private AmazonS3 s3;

    @PostMapping(path = "/authenticate", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest, HttpServletResponse response) throws Exception {
      
        try {
            System.out.println(">>>> email: " + authenticationRequest.getEmail());
            authenticate(authenticationRequest.getEmail(), authenticationRequest.getPassword());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(e.getMessage());
        }
	
		final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getEmail());
	
		final String token = jwtTokenUtil.generateToken(userDetails);
        
        Cookie c = new Cookie("token", token);
        c.setPath("/");
        c.setHttpOnly(true);
        c.setSecure(true);
        c.setMaxAge(60*60*5);
        response.addCookie(c);
        
        AppUser user = userSvc.getUserDetailsByEmail(authenticationRequest.getEmail());
        
        
		return ResponseEntity.status(HttpStatus.OK).body(user);
    }


    @PostMapping(path = "/register", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addUser(@RequestBody String payload) {

        Response resp;
        AppUser appUser = new AppUser();
        try {
            appUser = AppUser.create(payload);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw ex;
        }

        try {
            userSvc.addUser(appUser.getUsername(), appUser.getName(), appUser.getEmail(), appUser.getPassword());
            emailSvc.sendSimpleMail(appUser.getEmail(), appUser.getUsername());
            resp = new Response();
            resp.setCode(201);
            resp.setMessage("User created");
            return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(resp.toJson().toString());
        } catch (Exception ex) {
            resp = new Response();
            resp.setCode(400);
            resp.setMessage(ex.getMessage());
            return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(resp.toJson().toString());
        }    
    }

    @GetMapping(path = "/logout")
    public ResponseEntity<?> logout(HttpServletResponse response) {

        Cookie c = new Cookie("token", null);
        c.setPath("/");
        c.setHttpOnly(true);
        c.setSecure(true);
        c.setMaxAge(0);
        response.addCookie(c);

        return ResponseEntity.ok().body("Logout successful");
    }

    @GetMapping(path = "/user/{username}")
    public ResponseEntity<String> getUserDetails(@PathVariable String username) {

        Response resp;

        try {
            AppUser user = userSvc.getUserDetailsByUsername(username);
            resp = new Response();
            resp.setCode(200);
            resp.setMessage("User created");
            return ResponseEntity
                .status(HttpStatus.OK)
                .body(AppUser.toJson(user).toString());
        } catch (Exception ex) {
            resp = new Response();
            resp.setCode(400);
            resp.setMessage(ex.getMessage());
            return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(resp.toJson().toString());
        }    

    }

    @PostMapping(path="user/profile-pic/upload/{username}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> postSpacesUpload(@RequestPart MultipartFile myfile, @PathVariable String username) {

        //My private metadata
        Map<String, String> myData = new HashMap<>();
        myData.put("createdOn", (new Date().toString()));

        //MetaData for the object
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(myfile.getContentType());
        metadata.setContentLength(myfile.getSize());
        metadata.setUserMetadata(myData);

        try {
            PutObjectRequest putReq = new PutObjectRequest("vttp-final-project", "profile-pic/%s".formatted(username), myfile.getInputStream(), metadata);
            putReq = putReq.withCannedAcl(CannedAccessControlList.PublicRead);
            PutObjectResult result = s3.putObject(putReq);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        
        JsonObject data = Json.createObjectBuilder()
            .add("Content-type", myfile.getContentType())
            .add("name", username)
            .add("original_name", myfile.getOriginalFilename())
            .add("size", myfile.getSize())
            .build();

        return ResponseEntity.ok(data.toString());
    }

    private void authenticate(String email, String password) throws Exception {
        try{
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e); 
        }
    }
    
}

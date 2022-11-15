package vttp.finalproject.backend.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    
    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String sender;

    public String sendSimpleMail(String email, String username) {

        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();

            mailMessage.setFrom(sender);
            mailMessage.setTo(email);
            mailMessage.setText("""
                            Hi %s,

                                Thanks for creating an account with Tweeter. You may access your account now.

                                We look forward to seeing your soon.
                        """.formatted(username)); 
            
            mailMessage.setSubject("Your Tweeter account has been created!");

            mailSender.send(mailMessage);
            return "Mail send successfully";
        } catch (Exception ex) {
            ex.printStackTrace();
            return "Error while sending email";
        }
    }
}

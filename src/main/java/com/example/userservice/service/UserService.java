package com.example.userservice.service;

import com.example.userservice.model.ForumUser;
import com.example.userservice.model.ForumUserRequest;
import com.example.userservice.model.LoginRequest;
import com.example.userservice.repository.UserRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;



import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

@Service
public class UserService {

    private final JavaMailSender javaMailSender;

    private final UserRepository userRepository;

    @Autowired
    public UserService(JavaMailSender javaMailSender, UserRepository userRepository) {
        this.javaMailSender = javaMailSender;
        this.userRepository = userRepository;
    }


    public ForumUser registerUser(ForumUserRequest request){

        if (userRepository.findUserByEmail(request.getEmail()).isPresent()){
            throw new IllegalStateException("Email taken");
        }



        ForumUser forumUser = ForumUser.builder()
                .id(UUID.randomUUID().toString())
                .profilePic(request.getProfilePic())
                .email(request.getEmail())
                .username(request.getUsername())
                .password(encodePassword(request.getPassword()))
                .build();

        userRepository.save(forumUser);


        try{
            MimeMessage message = javaMailSender.createMimeMessage();
             message.setFrom("welcometosweden.org@hotmail.com");
             message.setRecipients(MimeMessage.RecipientType.TO, request.getEmail());
             message.setSubject("Your account has been created!");
             message.setContent(accountCreatedEmail(request.getUsername()),"text/html; charset=utf-8");
             javaMailSender.send(message);

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }



        return forumUser;
    }
    public ForumUser loginUser(LoginRequest request){

        if (userRepository.findUserByEmail(request.getEmail()).isPresent() && userRepository.findUserByEmail(request.getEmail()).get().getPassword().equals(encodePassword(request.getPassword()))){
            return userRepository.findUserByEmail(request.getEmail()).get();
        }
        else {
            throw new IllegalStateException("User not found");
        }

    }


    public String accountCreatedEmail(String userName) {
        return "<html><head><style>"
                + "body { font-family: Arial, sans-serif; background-color: #f8f8f8; margin: 0; padding: 0; font-size: 16px; }"
                + ".container { max-width: 600px; margin: 0 auto; background-color: #ffffff; border-radius: 10px; box-shadow: 0 4px 8px rgba(0,0,0,0.1); }"
                + ".header { background-color: #014AAD; color: #FFDA00; padding: 20px; text-align: center; border-top-left-radius: 10px; border-top-right-radius: 10px; font-family: 'Pacifico', cursive; }"
                + ".content { padding: 20px; }"
                + ".message { padding: 20px; text-align: center; }"
                + "</style></head><body>"
                + "<div class='container'>"
                + "<div class='header'>"
                + "<h2>Welcome to Sweden, " + userName + "!</h2>"
                + "</div>"
                + "<div class='content'>"
                + "<div class='message'>"
                + "<p>Your account on welcometosweden.org has been successfully created. Thank you for joining us!</p>"
                + "<p>Login to get your questions answered!</p>"
                + "<a href='https://welcometosweden.onrender.com/login'>Login Here</a>"
                + "</div>"
                + "</div>"
                + "</div>"
                + "</body></html>";
    }



    private String encodePassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashedPassword = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : hashedPassword) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error encoding password", e);
        }
    }


}

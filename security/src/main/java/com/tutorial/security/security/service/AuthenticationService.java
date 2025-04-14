package com.tutorial.security.security.service;

import com.tutorial.security.security.dto.EmailTemplateName;
import com.tutorial.security.security.dto.RegistrationRequest;
import com.tutorial.security.security.model.Token;
import com.tutorial.security.security.model.User;
import com.tutorial.security.security.repo.RoleRepository;
import com.tutorial.security.security.repo.TokenRepository;
import com.tutorial.security.security.repo.UserRepository;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class AuthenticationService {

    @Value("${app.mailing.frontend.activation-url}")
    private String activationUrl;

    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final EmailService emailService;

    public AuthenticationService(
            RoleRepository roleRepository,
            PasswordEncoder passwordEncoder,
            UserRepository userRepository,
            TokenRepository tokenRepository,
            EmailService emailService) {
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
        this.emailService = emailService;
    }

    public void register(RegistrationRequest request) throws MessagingException {
        //apply custom/default(USER) role, validate via email
        //TODO: exception handling to be added
        var userRole = roleRepository.findByRoleName("USER")
                .orElseThrow(
                        () -> new IllegalStateException("ROLE USER was not initialized")
                );

        var user = new User();
                user.setFirstName(request.firstName());
                user.setLastName(request.lastName());
                user.setEmail(request.email());
                user.setPassword(passwordEncoder.encode(request.password())); //inside the app password must always stay encrypted
                user.setAccountLocked(false);
                user.setEnabled(false);
                user.setRoles(List.of(userRole));

        userRepository.save(user);
        sendValidationEmail(user);
    }

    private void sendValidationEmail(User user) throws MessagingException {
        var newToken = generateAndSaveActivationToken(user);
        //sending email
        emailService.sendEmail(user.getEmail(), user.getFullName(), EmailTemplateName.ACTIVATE,activationUrl,newToken,"Account Activation");
    }

    private String generateAndSaveActivationToken(User user){
        //generate a token
        String generatedToken = generateActivationCode(6); //generate a code of a length
        var token = new Token();
          token.setToken(generatedToken);
          token.setCreatedOn(LocalDateTime.now());
          token.setExpiryOn(LocalDateTime.now().plusMinutes(15));
          token.setUser(user);
        tokenRepository.save(token);
        return generatedToken;
    }

    private String generateActivationCode(int length) { //random OTP 6 digit generation
        String chars = "0123456789";
        StringBuilder codeBuilder = new StringBuilder();
        SecureRandom secureRandom = new SecureRandom(); //a crypt/secure random generation
        for (int i =0;  i < length; i++) {
            int randomIndex = secureRandom.nextInt(chars.length()); //random index generated is in 0 - 9 range, so
            codeBuilder.append(chars.charAt(randomIndex));
        }
        return codeBuilder.toString();
    }

}

package com.blessify.locallegends.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.blessify.locallegends.dto.LoginDto;
import com.blessify.locallegends.dto.UserDto;
import com.blessify.locallegends.model.User;
import com.blessify.locallegends.model.User.Role;
import com.blessify.locallegends.repository.UserRepository;
import com.blessify.locallegends.response.AuthenticationResponse;



@Service
public class AuthenticationService {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private UserService userService;
   
    @Autowired
    private EmailSender emailSender;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    public AuthenticationResponse registerUser(UserDto req) {
        if (!req.getPassword().equals(req.getConfirmPassword())) {
            throw new RuntimeException("Passwords do not match");
        }

        if (req.getEmail().isEmpty()) {
            throw new RuntimeException("Email is required");
        }

        if (userRepository.existsByEmail(req.getEmail())) {
            throw new RuntimeException("Oops, email taken. Enter a unique email.");
        }

        User user = new User();
        user.setPassword(passwordEncoder.encode(req.getPassword()));
        String name = req.getFirstName() + " " +req.getLastName();
        user.setName(name);
        user.setEmail(req.getEmail());
        user.setRole(Role.USER);

        userRepository.save(user);

       emailSender.sendRegistrationSuccessEmail(user.getEmail(),user.getName());
       emailSender.sendAdminNotification(user.getEmail());

        String token = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);
        
        AuthenticationResponse authResponse = new AuthenticationResponse();
        authResponse.setToken(token);
        authResponse.setRole(user.getRole().name());
        authResponse.setRefreshToken(refreshToken);
        return authResponse;
    }
    
   

    public AuthenticationResponse authenticate(LoginDto req) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(req.getEmail(), req.getPassword()));

        User user = userRepository.findByEmail(req.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));
       

        String token = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);
        AuthenticationResponse authResponse = new AuthenticationResponse();
        authResponse.setToken(token);
        authResponse.setRole(user.getRole().name());
        authResponse.setRefreshToken(refreshToken);
        authResponse.setUser(user);

        return authResponse;
    }

    public AuthenticationResponse refreshToken(String refreshToken) {
        String username = jwtService.extractUsername(refreshToken);
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!jwtService.isValid(refreshToken, user)) {
            throw new RuntimeException("Invalid refresh token");
        }

        String newAccessToken = jwtService.generateToken(user);
        
        AuthenticationResponse authResponse = new AuthenticationResponse();
        authResponse.setToken(newAccessToken);
        authResponse.setRole(user.getRole().name());
        authResponse.setRefreshToken(refreshToken);
        return authResponse;
    }
}

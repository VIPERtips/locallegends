package com.blessify.locallegends.service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.blessify.locallegends.dto.UserDto;
import com.blessify.locallegends.model.User;
import com.blessify.locallegends.repository.UserRepository;



@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    
  

    @Autowired
    private PasswordEncoder passwordEncoder;
    
    public String generateOTP() {
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000); 
        return String.valueOf(otp);
    }

    public void generateResetToken(User user) {
        String token = UUID.randomUUID().toString(); 
        //user.setResetToken(token);
        //user.setTokenExpiration(LocalDateTime.now().plusMinutes(30)); 
        userRepository.save(user);
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow();
    }

//    public User findByResetToken(String token) {
//        return userRepository.findByResetToken(token);
//    }

//    public void updatePassword(User user, String newPassword) {
//        user.setPassword(passwordEncoder.encode(newPassword));
//        user.setResetToken(null); 
//        user.setTokenExpiration(null);
//        user.setUpdatedAt(LocalDateTime.now());
//        userRepository.save(user);
//    }
    
   

    public UserDto getUserById(int id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + id));
        UserDto userDto = new UserDto();
        userDto.setId(user.getUserId());
       
        userDto.setFirstName(user.getName());
        userDto.setEmail(user.getEmail());;

        return userDto;
    }
    
    public User getUserBy_Id(int id) {
    	return userRepository.findById(id)
    			.orElseThrow(() -> new RuntimeException("User not found with ID: " + id));
    }

    public void deleteUserById(int id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("User not found with ID: " + id);
        }
        userRepository.deleteById(id);
    }
    
   


    public User getUserByUsername(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found for username " + email));
    }
}

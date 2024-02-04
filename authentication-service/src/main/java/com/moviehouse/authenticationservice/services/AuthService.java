package com.moviehouse.authenticationservice.services;

import com.moviehouse.authenticationservice.model.User;
import com.moviehouse.authenticationservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

//    public boolean userExists(String email,String hashedPassword){
//        Optional<User> user = userRepository.findById(email);
//
//        if(user.get().getHashedPassword() == hashedPassword && user.get().getEmail() == email){
//            return true;
//        }
//
//        return false;
//    }

   public boolean createUser(String email,String password){
       // if(userExists(email,password)){
        //    return false;
       // }
        String hashedPassword = passwordEncoder.encode(password);
        User user = new User(email,hashedPassword);
        userRepository.save(user);
        return true;

   }


   public boolean validateCredentials(String email,String password){
        Optional<User> user = userRepository.findById(email);

       if(passwordEncoder.matches(password,user.get().getHashedPassword())){
           return true;
       }

       return false;

   }

   public Optional<User> findUser(String email){
         return userRepository.findById(email);
   }

   





}

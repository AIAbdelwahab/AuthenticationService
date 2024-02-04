package com.moviehouse.authenticationservice.services;

import com.moviehouse.authenticationservice.model.User;
import com.moviehouse.authenticationservice.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
@ExtendWith(MockitoExtension.class)
class AuthServiceTest {
    @Mock
    private UserRepository userRepository;
    @Autowired
    private MockMvc mockMvc;

    @Mock
    private PasswordEncoder passwordEncoder;
    @InjectMocks
    private AuthService authService;

    @Test
    void createUser() {
        User user = new User("email","blahash");

        Mockito.when(passwordEncoder.encode("raw")).thenReturn("blahash");
        Mockito.when(userRepository.save(user)).thenReturn(user);

        Assertions.assertTrue(authService.createUser("email","raw"));

    }

    @Test
    void validateCredentials() {
        String email = "example@mail.com";
        User user = new User("email","blahash");
        User user2 = new User("email","blabla");

        Mockito.when(userRepository.findById(email)).thenReturn(Optional.ofNullable(user2));
        Mockito.when(passwordEncoder.matches(user.getHashedPassword(),user2.getHashedPassword())).thenReturn(true);


        Assertions.assertTrue(authService.validateCredentials(email,user.getHashedPassword()));

    }



    @Test
    void findUser() {
        String email = "example@email.com";
        User user = new User("willywonka@gmail.com","darkChoco");
        Optional<User> opUser = Optional.ofNullable(user);
        Mockito.when(userRepository.findById(email)).thenReturn(Optional.ofNullable(user));

        Assertions.assertTrue(authService.findUser(email).isPresent());
    }
}
package com.moviehouse.authenticationservice.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.moviehouse.authenticationservice.JwtUtil;
import com.moviehouse.authenticationservice.dto.AuthReq;
import com.moviehouse.authenticationservice.dto.AuthResp;
import com.moviehouse.authenticationservice.model.User;
import com.moviehouse.authenticationservice.services.AuthService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
class AuthControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private AuthService authService;

    @MockBean
    private JwtUtil jwtUtil;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void authenticate() throws Exception {
        String user = "random@gmail.com";
        String password = "password";
        AuthReq x = new AuthReq();

        Mockito.when(authService.validateCredentials(user,password)).thenReturn(false);

        mockMvc.perform(MockMvcRequestBuilders.post("/auth/login",x).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(400));
    }

    @Test
    void register() throws Exception {
        String user = "random@gmail.com";
        String password = "password";
        AuthReq x = new AuthReq();

        Mockito.when(authService.createUser(user,password)).thenReturn(false);

        mockMvc.perform(MockMvcRequestBuilders.post("/auth/register",x).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(400));

    }

    @Test
    void authenticate_Success() throws Exception {
        String user = "random@gmail.com";
        String password = "password";
        AuthReq x = new AuthReq();
        x.setEmail(user);
        x.setPassword(password);
        User userx = new User("bla","bla");
        AuthResp authResp = new AuthResp("email","bla");

        Mockito.when(authService.validateCredentials(user,password)).thenReturn(true);
        Mockito.when(authService.findUser(user)).thenReturn(Optional.ofNullable(userx));
        Mockito.when(jwtUtil.createToken(userx)).thenReturn(String.valueOf(authResp));


        mockMvc.perform(MockMvcRequestBuilders.post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(x)))
                        .andExpect(status().is(200));
    }

    @Test
    void register_Success() throws Exception {
        String user = "random@gmail.com";
        String password = "password";
        AuthReq x = new AuthReq();
        x.setPassword(password);
        x.setEmail(user);

        Mockito.when(authService.createUser(user,password)).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders.post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(x)))
                        .andExpect(status().is(200));

    }



}
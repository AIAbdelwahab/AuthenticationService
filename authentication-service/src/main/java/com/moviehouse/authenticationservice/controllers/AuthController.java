package com.moviehouse.authenticationservice.controllers;

import com.moviehouse.authenticationservice.JwtUtil;
import com.moviehouse.authenticationservice.dto.AuthReq;
import com.moviehouse.authenticationservice.dto.AuthResp;
import com.moviehouse.authenticationservice.model.User;
import com.moviehouse.authenticationservice.services.AuthService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Encoders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.crypto.SecretKey;
import java.security.KeyPair;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private PasswordEncoder passwordEncoder;



    @Autowired
    private final AuthService authService;

   private final JwtUtil jwtUtil;


    public AuthController(AuthService authService,JwtUtil jwtUtil) {
        this.authService = authService;
        this.jwtUtil = jwtUtil;
    }
    @CrossOrigin
    @PostMapping("/login")
    public ResponseEntity<AuthResp> authenticate(@RequestBody AuthReq authReq){
       if (authService.validateCredentials(authReq.getEmail(),authReq.getPassword())){
           AuthResp authResp = new AuthResp(authReq.getEmail(),"");
           Optional<User> user = authService.findUser(authReq.getEmail());
           authResp.setToken(jwtUtil.createToken(user.get()));
           return ResponseEntity.ok(authResp);
       }
       return ResponseEntity.status(401).build();

    }
    @CrossOrigin
    @PostMapping("/register")
    public ResponseEntity register(@RequestBody AuthReq authReq){
       boolean f = authService.createUser(authReq.getEmail(),authReq.getPassword());
       if (f){
           return ResponseEntity.ok().build();
       }
       return ResponseEntity.status(401).build();
    }



}

package com.kiranmaipilli.userJournalApp.controller;

import com.kiranmaipilli.userJournalApp.entity.User;
import com.kiranmaipilli.userJournalApp.service.UserDetailsServiceImpl;
import com.kiranmaipilli.userJournalApp.service.UserService;
import com.kiranmaipilli.userJournalApp.utilis.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

@RestController
@RequestMapping("/public")
@Slf4j
public class PublicController {
    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping("/health-check")
    public ResponseEntity<?> healthCheck(){
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/create-user")
    public boolean createNewUser(@RequestBody User user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(Arrays.asList("USER"));
        userService.saveUser(user);
        return true;
    }


    @PostMapping("/signup")
    public boolean signup(@RequestBody User user){
        user.setPassword(passwordEncoder.encode((user.getPassword())));
        user.setRoles(Arrays.asList("USER"));
        userService.saveUser(user);
        return true;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user){
        try{
            authenticationManager.authenticate
                    (new UsernamePasswordAuthenticationToken(user.getUserName(), user.getPassword()));

            UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUserName());
            String jwt = jwtUtil.generateToken(userDetails.getUsername());
            return new ResponseEntity<>(jwt, HttpStatus.OK);

        } catch (Exception e) {
            log.error("Exception occurred while createAuthentication ", e);
            return new ResponseEntity<>("Incorrect userName or password", HttpStatus.BAD_REQUEST);

        }
    }
}












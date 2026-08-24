package com.kiranmaipilli.userJournalApp.controller;

import com.kiranmaipilli.userJournalApp.api.response.WeatherResponse;
import com.kiranmaipilli.userJournalApp.entity.JournalEntry;
import com.kiranmaipilli.userJournalApp.entity.User;
import com.kiranmaipilli.userJournalApp.service.JournalEntryService;
import com.kiranmaipilli.userJournalApp.service.UserService;
import com.kiranmaipilli.userJournalApp.service.WeatherService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private WeatherService weatherService;

    //get all users
//    @GetMapping
//    public List<User> getAllUsers(){
//        return userService.getAll();
//    }

    //get my Id
//    @GetMapping("/userName/{userName}")
//    public User getUserbyuserName(@PathVariable  String userName){
//        return userService.getUserbyUsername(userName);
//    }

//    @PostMapping
//    public boolean createUser(@RequestBody User user){
//        userService.saveUser(user);
//        return true;
//    }

    //to save password in encypted form even inside db
    //moved to public end point
//    @PostMapping
//    public boolean createNewUser(@RequestBody User user){
//        user.setPassword(passwordEncoder.encode(user.getPassword()));
//        user.setRoles(Arrays.asList("USER"));
//        userService.saveUser(user);
//        return true;
//    }

//    @PutMapping("/{userName}")
//    public User updateUser(@RequestBody  User user, @PathVariable String userName){
//        User prevUser = userService.getUserbyUsername(userName);
//        if(prevUser == null){
//            return null;
//        }
//        else{
//            prevUser.setUserName(user.getUserName() != null ? user.getUserName() : prevUser.getUserName());
//            prevUser.setPassword(user.getPassword() != null ? user.getPassword() : prevUser.getPassword());
//            prevUser.setJournalEntries(user.getJournalEntries() != null ? user.getJournalEntries() : prevUser.getJournalEntries());
//
//            userService.saveUser(prevUser);
//
//            return prevUser;
//        }
//
//    }

    @PutMapping
    public User updateUser(@RequestBody  User user){
        //when ever a user is authenticated, their details will come under SecurityContextHolder
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();

        User prevUser = userService.getUserbyUsername(userName);
        if(prevUser == null){
            return null;
        }
        else{
            prevUser.setUserName(user.getUserName() != null ? user.getUserName() : prevUser.getUserName());
            prevUser.setPassword(user.getPassword() != null ? passwordEncoder.encode(user.getPassword()) : passwordEncoder.encode(prevUser.getPassword()));
            prevUser.setJournalEntries(user.getJournalEntries() != null ? user.getJournalEntries() : prevUser.getJournalEntries());

            userService.saveUser(prevUser);

            return prevUser;
        }

    }


    //delete
    @DeleteMapping()
    public boolean deleteUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        userService.deleteUserbyUsername(userName);
        return true;
    }


    @GetMapping
    public ResponseEntity<?> greetings(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        WeatherResponse weatherResponse = weatherService.getWeather("Kochi");
        String greeting = "";
        if(weatherResponse != null){
            greeting = ", Weather feels like " + weatherResponse.getCurrent().getWeatherDescriptions().get(0);
        }
        return new ResponseEntity<>("Hi " + authentication.getName() + greeting, HttpStatus.OK);
    }




}

package com.kiranmaipilli.userJournalApp.service;

import com.kiranmaipilli.userJournalApp.entity.JournalEntry;
import com.kiranmaipilli.userJournalApp.entity.User;
import com.kiranmaipilli.userJournalApp.repository.JournalEntryRepo;
import com.kiranmaipilli.userJournalApp.repository.UserRepo;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

//@Component
@Service
@Slf4j

public class UserService {

    @Autowired
    private UserRepo userRepo;

//    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    //save entry
    public void saveUser(User user){
//        user.setPassword(passwordEncoder.encode(user.getPassword()));
        try{
            userRepo.save(user);
        }
        catch(Exception e){
            log.error("Error occurred for {}", user.getUserName(),e);
            log.info("hahahahahahhahahahahah!");
            log.warn("hahahahahahhahahahahah!");
            log.debug("hahahahahahhahahahahah!");
//            logger.error("hahahahahahhahahahahah!");
//            logger.trace("hahahahahahhahahahahah!");
            System.out.println(e.getMessage());
        }

    }

    //get all
    public List<User> getAll(){
        return userRepo.findAll();
    }

    //find by Id
    public User getUserbyId(ObjectId myId){
        return userRepo.findById(myId).orElse(null);
    }

    //delete by Id
    public void deleteUserbyId(ObjectId myId){
        userRepo.deleteById(myId);
    }

    public User getUserbyUsername(String username){
        return userRepo.findByUserName(username);
    }

    public void deleteUserbyUsername(String username){
        User user = userRepo.findByUserName(username);
        userRepo.deleteById(user.getId());

    }

    public void saveAdmin(User user){
        user.setRoles(Arrays.asList("USER","ADMIN"));
        userRepo.save(user);
    }



}

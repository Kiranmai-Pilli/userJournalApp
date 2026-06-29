package com.kiranmaipilli.userJournalApp.controller;

import com.kiranmaipilli.userJournalApp.entity.JournalEntry;
import com.kiranmaipilli.userJournalApp.entity.User;
import com.kiranmaipilli.userJournalApp.service.JournalEntryService;
import com.kiranmaipilli.userJournalApp.service.UserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/journal")
public class JournalEntryController {

    @Autowired
    private JournalEntryService journalEntryService;
    @Autowired
    private UserService userService;

//    @GetMapping // used to define a base path
//    public List<JournalEntry> getAllJournal() {
//        return journalEntryService.getAllJournalEntries();
//    }

    @GetMapping
    public ResponseEntity<?> getAllJournalEntriesofUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userService.getUserbyUsername(username);
        List<JournalEntry> userEntries= user.getJournalEntries();

        if(userEntries != null && !userEntries.isEmpty()){
            return new ResponseEntity<>(userEntries, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }


//    @PostMapping
//    public ResponseEntity<JournalEntry> createEntry(@RequestBody JournalEntry myEntry){
//        try{
//            myEntry.setDate(LocalDateTime.now());
//            journalEntryService.saveEntry(myEntry);
//            return new ResponseEntity<>(myEntry, HttpStatus.CREATED);
//        }
//        catch(Exception e){
//            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//        }
//    }

    @PostMapping
    public ResponseEntity<?> createUserEntry(@RequestBody JournalEntry entry){
        try{
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
//            User user = userService.getUserbyUsername(userName);
//            List<JournalEntry> userEntries = user.getJournalEntries();
//            entry.setDate(LocalDateTime.now())
            journalEntryService.saveEntry(entry, username); // should save it in journal Entries before user service
//            userEntries.add(entry);
//            userService.saveUser(user);
            return  new ResponseEntity<>(HttpStatus.CREATED);
        }
        catch(Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }


    //to get teh record of a particular id
    @GetMapping("/id/{myId}")
    public ResponseEntity<JournalEntry> getJournalEntryById(@PathVariable ObjectId myId){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userService.getUserbyUsername(username);
        List<JournalEntry> userEntries = user.getJournalEntries().stream().filter(x -> x.getId().equals(myId)).collect(Collectors.toList());
        if(!userEntries.isEmpty()){
            Optional<JournalEntry> entry = Optional.ofNullable(journalEntryService.getJournalEntryById(myId));
            if(entry.isPresent()){
                return new ResponseEntity<>(entry.get(), HttpStatus.OK);
            }
        }

        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);

//        return null;
    }

    //to delete entry of a particular if
    @DeleteMapping("/id/{myId}")
    public ResponseEntity<?> deleteJournalEntryById(@PathVariable ObjectId myId){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        boolean result = journalEntryService.deleteJournlaEntrybyId(myId, userName);

        if(result) return new ResponseEntity<>(true, HttpStatus.NO_CONTENT);
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    //to update journal entry
    @PutMapping("/id/{myId}")
    public ResponseEntity<?> updateJournalEntryById(@PathVariable ObjectId myId, @RequestBody JournalEntry entry){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        JournalEntry oldEntry = journalEntryService.getJournalEntryById(myId);

        if(oldEntry == null){
//            journalEntryService.saveEntry(entry);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        else{
            oldEntry.setDate(LocalDateTime.now());
            oldEntry.setTitle(entry.getTitle() != null ? entry.getTitle() : oldEntry.getTitle());
            oldEntry.setContent(entry.getContent() != null ? entry.getContent() : oldEntry.getContent());

            journalEntryService.putJournalEntry(oldEntry);
            return new ResponseEntity<>(oldEntry, HttpStatus.CREATED);
        }

//        entry.setDate(LocalDateTime.now());
//        journalEntryService.putJournalEntry(entry);

    }







}

package com.kiranmaipilli.userJournalApp.service;

import com.kiranmaipilli.userJournalApp.entity.JournalEntry;
import com.kiranmaipilli.userJournalApp.entity.User;
import com.kiranmaipilli.userJournalApp.repository.JournalEntryRepo;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class JournalEntryService {

    @Autowired
    private JournalEntryRepo journalEntryRepo;

    @Autowired
    private UserService userService;

    private static final Logger logger = LoggerFactory.getLogger(JournalEntryService.class);

    @Transactional
    public void saveEntry(JournalEntry entry, String userName){
        try{
            entry.setDate(LocalDateTime.now());
            JournalEntry saved = journalEntryRepo.save(entry);
            User user = userService.getUserbyUsername(userName);
            List<JournalEntry> userEntries = user.getJournalEntries();
            userEntries.add(saved);
            userService.saveUser(user);
        }
        catch (Exception e){
            System.out.println(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    public List<JournalEntry> getAllJournalEntries(){
        return journalEntryRepo.findAll();
    }

    public JournalEntry getJournalEntryById(ObjectId myId){
        return journalEntryRepo.findById(myId).orElse(null);
    }

    @Transactional
    public boolean deleteJournlaEntrybyId(ObjectId myId, String userName){
        boolean removed = false;
        try{
            User user = userService.getUserbyUsername(userName);
            removed = user.getJournalEntries().removeIf(x -> x.getId().equals(myId));
            if(removed){
                journalEntryRepo.deleteById(myId);
                userService.saveUser(user);
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }

        return removed;

    }

    public void putJournalEntry(JournalEntry entry){
        journalEntryRepo.save(entry);
    }


    public List<JournalEntry> findByUsername(String userName){
        return null;

    }




}

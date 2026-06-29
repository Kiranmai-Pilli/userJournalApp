package com.kiranmaipilli.userJournalApp.service;

import com.kiranmaipilli.userJournalApp.entity.User;
import com.kiranmaipilli.userJournalApp.repository.UserRepo;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UserServiceTests {

    @Autowired
    private UserRepo userRepo;

    @Test
    public void testAdd(){
        assertEquals(3, 2+1);
    }

    @Disabled
    @Test
    public void testGetByUsername(){
        User user = userRepo.findByUserName("Shyam");
        assertTrue(user.getJournalEntries().isEmpty());
//        assertNotNull(userRepo.findByUserName("Shyam"));
    }

    //parameterized test only for usernames
    @ParameterizedTest
    @ValueSource(
            strings = {
                    "Shyam",
                    "Ram",
                    "Geetha"
            }
    )
    public void testGetUsername(String name){
        assertNotNull(userRepo.findByUserName(name));
    }

    //adding the parameterised test cases to change the variable values of test inputs
    @ParameterizedTest
    @CsvSource({
            "4,1,3",
            "6,2,3",
            "10,4,6"

    })
    public void testAddition(int expected, int a, int b){
        assertEquals(expected, a+b);
    }
}

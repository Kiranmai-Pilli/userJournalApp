package com.kiranmaipilli.userJournalApp.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class UserRepoImplTest {

    @Autowired
    private UserRepoImpl userRepoImpl;

    @Test
    public void testfindUsers(){
        userRepoImpl.getUserforSA();
    }
}

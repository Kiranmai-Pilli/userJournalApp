package com.kiranmaipilli.userJournalApp.service;

import com.kiranmaipilli.userJournalApp.repository.UserRepo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.userdetails.User;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import static org.mockito.Mockito.*;
import com.kiranmaipilli.userJournalApp.entity.User;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.ArrayList;

//@SpringBootTest
public class UserDetailsServiceImplTest {

//    @Autowired
    @InjectMocks
    private UserDetailsServiceImpl userDetailsService;

//    @Mock
//    private UserRepo userRepo;

    @Mock
    private UserService userService;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void loadUserByUsernameTest(){
        when(userService.getUserbyUsername(ArgumentMatchers.anyString())).
                thenReturn(User.builder()
                        .userName("Shyam")
                        .password("asdfgh")
                        .roles(new ArrayList<>())
                        .build());
        UserDetails user = userDetailsService.loadUserByUsername("Shyam");
        Assertions.assertNotNull(user);
    }
}

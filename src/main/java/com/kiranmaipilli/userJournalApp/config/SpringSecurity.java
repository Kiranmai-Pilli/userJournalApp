package com.kiranmaipilli.userJournalApp.config;

import com.kiranmaipilli.userJournalApp.filter.JwtFilter;
import com.kiranmaipilli.userJournalApp.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SpringSecurity{

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private JwtFilter jwtFilter;
//
//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception{
//        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
//    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/journal/**","/user/**").authenticated() // allow these paths without authorization
                        .requestMatchers("/admin/**").hasAuthority("ADMIN") // allow these paths without authorization
                        .anyRequest().permitAll()); // all other need to authorize
//                .httpBasic(Customizer.withDefaults()) //enable HTTP basic authentication
//                .formLogin(Customizer.withDefaults())
//                .csrf(csrf -> csrf.disable()); //disable csrf for rest api paths
                                                                        // CSRF protection is for browser-based apps (forms)
                                                                        // we disable it for REST APIs since we use tokens/basic auth instead
                http.sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))  // new way
                .csrf(csrf -> csrf.disable());

        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    // Spring Boot sees UserDetailsService + PasswordEncoder beans
    // and auto-wires authentication — no extra config needed!
//
//    @Bean
//    public UserDetailsService userDetailsService() {
//        return userDetailsService;
//    }
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception{
        return config.getAuthenticationManager();
    }
}

package com.kiranmaipilli.userJournalApp.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kiranmaipilli.userJournalApp.api.response.WeatherResponse;
import lombok.extern.slf4j.Slf4j;
import lombok.extern.slf4j.XSlf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class RedisService {
    @Autowired
    private RedisTemplate redisTemplate;


    //we'll setup one get method and one set method
    public <T> T get(String key, Class<T> entityClass){
        try{
            Object o = redisTemplate.opsForValue().get(key);
            if(o==null) return null;

            ObjectMapper mapper = new ObjectMapper();

            return mapper.readValue(o.toString(), entityClass);
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }

    }

    //we'll setup one get method and one set method
    public void set(String key,Object o, Long ttl){
        try{
            ObjectMapper mapper = new ObjectMapper();
            String jsonValue = mapper.writeValueAsString(o);
             redisTemplate.opsForValue().set(key, jsonValue, ttl,TimeUnit.SECONDS);
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }
}

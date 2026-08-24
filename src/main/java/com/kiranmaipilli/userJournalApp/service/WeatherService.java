package com.kiranmaipilli.userJournalApp.service;

import com.kiranmaipilli.userJournalApp.api.response.WeatherResponse;
import com.kiranmaipilli.userJournalApp.cache.AppCache;
import com.kiranmaipilli.userJournalApp.constants.PlaceHolders;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class WeatherService {
    @Value("${weather.api.key}")
    private String apiKey;
//    private static final String API = "https://api.weatherstack.com/current?access_key=API_KEY&query=CITY";

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private AppCache appCache;

    @Autowired
    private RedisService redisService;

    private String API;

    @PostConstruct
    public void init(){
        API = appCache.app_cache.get(AppCache.keys.WEATHER_API.toString());
    }


    public WeatherResponse getWeather(String city){
        WeatherResponse weatherResponse = redisService.get("weather_of_" + city, WeatherResponse.class);

        if(weatherResponse != null){
            return weatherResponse;
        }

        String finalAPI = API.replace(PlaceHolders.API_KEY,apiKey).replace(PlaceHolders.CITY,city);
        ResponseEntity<WeatherResponse> response = restTemplate.exchange(finalAPI, HttpMethod.GET, null, WeatherResponse.class);
        WeatherResponse body = response.getBody();
        if(body != null){
            redisService.set("weather_of_" + city, body, 300l);
        }
        return body;
    }

    //we can post request also to this
//    String requestBody = "{\n" +
//            "    \"userName\" : \"Shyam\",\n" +
//            "    \"password\" : \"Shyam123\"\n" +
//            "}";
//    HttpEntity<String> httpEntity = new HttpEntity<>(requestBody);
//
//    public WeatherResponse postWeather(String city){
//        String finalAPI = API.replace("API_KEY",apiKey).replace("CITY",city);
//        ResponseEntity<WeatherResponse> response = restTemplate.exchange(finalAPI, HttpMethod.POST,httpEntity, WeatherResponse.class );
//        WeatherResponse body = response.getBody();
//        return body;
//    }


}

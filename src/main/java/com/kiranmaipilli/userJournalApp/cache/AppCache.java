package com.kiranmaipilli.userJournalApp.cache;

import com.kiranmaipilli.userJournalApp.entity.ConfigJournalAppEntity;
import com.kiranmaipilli.userJournalApp.repository.ConfigJournalAppRepo;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class AppCache {
    public Map<String, String> app_cache;

    @Autowired
    private ConfigJournalAppRepo configJournalAppRepo;

    public enum keys {
        WEATHER_API;
    }


    @PostConstruct
    public void init(){
        app_cache = new HashMap<>();
        List<ConfigJournalAppEntity> configEntries = configJournalAppRepo.findAll();
        for(ConfigJournalAppEntity it: configEntries){
            app_cache.put(it.getKey(), it.getValue());
        }
    }


}

package com.kiranmaipilli.userJournalApp.scheduler;

import com.kiranmaipilli.userJournalApp.cache.AppCache;
import com.kiranmaipilli.userJournalApp.entity.JournalEntry;
import com.kiranmaipilli.userJournalApp.entity.User;
import com.kiranmaipilli.userJournalApp.enums.Sentiment;
import com.kiranmaipilli.userJournalApp.model.SentimentData;
import com.kiranmaipilli.userJournalApp.repository.UserRepoImpl;
import com.kiranmaipilli.userJournalApp.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class UserScheduler {
    @Autowired
    private UserRepoImpl userRepoImpl;
    @Autowired
    private EmailService emailService;

    @Autowired
    private KafkaTemplate<String, SentimentData> kafkaTemplate;

//    @Autowired
//    private SentimentAnalysisService sentimentAnalysisService;

    @Autowired
    private AppCache appCache;

//    @Scheduled(cron = "0 0 9 * * SUN")

    public void fetchUsersAndSendMail(){
        List<User> users = userRepoImpl.getUserforSA();
        for(User it: users){
            List<JournalEntry> entries = it.getJournalEntries();
            if(entries.isEmpty()) continue;
            List<Sentiment> filteredEntries = entries.stream()
                    .filter(x -> x!= null)
                    .filter(x -> x.getDate() != null)
                    .filter(x -> x.getDate()
                            .isAfter(LocalDateTime.now().minus(7, ChronoUnit.DAYS)))
                            .map(x -> x.getSentiment())
                            .collect(Collectors.toList());


            //from all the sentiment values we'll send the most frequent sentiment value of user to user
            Map<Sentiment, Integer> mp = new HashMap<>();
            for(Sentiment sentiment: filteredEntries){
                if(sentiment != null){
                    mp.put(sentiment, mp.getOrDefault(sentiment,0)+1);
                }
            }

            int maxi = 0;
            Sentiment freqSentiment = null;

            for(Map.Entry<Sentiment, Integer> entry : mp.entrySet()){
                if(entry.getValue() > maxi){
                    maxi = entry.getValue();
                    freqSentiment = entry.getKey();
                }
            }
//            String entry = String.join(" ", filteredEntries);
//            String sentiment = sentimentAnalysisService.getSentiment(entry);

            if(freqSentiment != null){
                SentimentData sentimentData = SentimentData.builder().email(it.getEmail()).sentiment("Sentiment for last 7 days " + freqSentiment.toString()).build();
                try{
                    kafkaTemplate.send("weekly-sentiments",sentimentData.getEmail(),  sentimentData);
                }
                catch(Exception e){
                    emailService.sendEmail(it.getEmail(),
                            "Sentiment for last 7 days",
                            freqSentiment.toString());
                }

            }
        }

        return;
    }

    @Scheduled(cron = "0 0/10 * ? * *")
    public void clearAppCache(){
        appCache.init();
    }
}

package com.kiranmaipilli.userJournalApp.model;

import lombok.*;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SentimentData {
    private String email;
    private String sentiment;
}

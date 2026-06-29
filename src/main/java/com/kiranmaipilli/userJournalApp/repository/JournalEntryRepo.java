package com.kiranmaipilli.userJournalApp.repository;

import com.kiranmaipilli.userJournalApp.entity.JournalEntry;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface JournalEntryRepo extends MongoRepository<JournalEntry, ObjectId> {

}

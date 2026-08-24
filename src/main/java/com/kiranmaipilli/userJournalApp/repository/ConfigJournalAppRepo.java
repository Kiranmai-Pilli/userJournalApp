package com.kiranmaipilli.userJournalApp.repository;

import com.kiranmaipilli.userJournalApp.entity.ConfigJournalAppEntity;
import com.kiranmaipilli.userJournalApp.entity.User;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ConfigJournalAppRepo extends MongoRepository<ConfigJournalAppEntity, ObjectId> {

}

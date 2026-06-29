package com.kiranmaipilli.userJournalApp.repository;

import com.kiranmaipilli.userJournalApp.entity.User;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepo extends MongoRepository<User, ObjectId> {

    User findByUserName(String userName);
    // by name of the function itself Spring automatically converts this into db.users.find({"userName" : "userName"})


}

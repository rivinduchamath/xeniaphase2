
package com.cloudofgoods.xenia.repository;



import com.cloudofgoods.xenia.entity.AuthUser;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends MongoRepository <AuthUser, String> {
    AuthUser findByUsername(String username);
}

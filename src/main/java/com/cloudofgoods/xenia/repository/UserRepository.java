
package com.cloudofgoods.xenia.repository;



import com.cloudofgoods.xenia.entity.AuthUser;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends MongoRepository <AuthUser, String> {

    AuthUser findByUsername(String username);
//    List<CustomerEntityObject> findAllByEndDateTimeIsGreaterThanEqualAndSlotIdEquals1(Date from, PageRequest of); // If Composite RuleRequestRootModelId
//
//    long countAllByEndDateTimeIsGreaterThanEqual1(Date from);


}

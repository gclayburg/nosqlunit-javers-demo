package com.example;

import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created by IntelliJ IDEA.
 * Date: 10/20/16
 * Time: 1:38 PM
 *
 * @author Gary Clayburg
 */
@JaversSpringDataAuditable
public interface UserRepo extends MongoRepository<User,String> {

	User findByLastname(String lastname);
}

package com.example;

import static com.example.FongoMongoTestConfig.getSpringFongoMongoDbRule;
import static org.junit.Assert.assertEquals;

import java.util.List;

import com.lordofthejars.nosqlunit.annotation.UsingDataSet;
import com.lordofthejars.nosqlunit.core.LoadStrategyEnum;
import com.lordofthejars.nosqlunit.mongodb.MongoDbRule;
import org.javers.core.Javers;
import org.javers.core.metamodel.object.CdoSnapshot;
import org.javers.repository.jql.QueryBuilder;
import org.javers.spring.boot.mongo.JaversMongoAutoConfiguration;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {NosqlunitJaversDemoApplication.class,FongoMongoTestConfig.class})
public class NosqlunitJaversDemoApplicationTests {
	private static final Logger log = LoggerFactory.getLogger(NosqlunitJaversDemoApplicationTests.class);

	@Autowired
	UserRepo userRepo;

	@Autowired
	Javers javers;

	@Autowired
	private ApplicationContext applicationContext; // nosql-unit requirement

	@Rule
	public MongoDbRule mongoDbRule = getSpringFongoMongoDbRule();

	@Test
	public void contextLoads() {
	}

	@UsingDataSet(locations = {"/userList.json"}, loadStrategy = LoadStrategyEnum.CLEAN_INSERT)
	@Test
	public void findByLastname() throws Exception {
		User user = userRepo.findByLastname("Smith");
		assertEquals("Johan",user.getFirstname());
	}

	@UsingDataSet(locations = {"/userList.json"}, loadStrategy = LoadStrategyEnum.CLEAN_INSERT)
	@Test
	public void modifyFirstname() throws Exception {
		//when: modify existing user twice
		User user = userRepo.findByLastname("Smith");
		user.setFirstname("bob");
		userRepo.save(user);
		user.setFirstname("bill");
		userRepo.save(user);

		//then: user audit snapshots are stored
		List<CdoSnapshot> snapshots = javers.findSnapshots(QueryBuilder.byInstanceId(user.getId(), User.class).build());
		assertEquals(2, snapshots.size());
		log.info("snapshot= " + snapshots.get(0).toString());
		assertEquals("bill",snapshots.get(0).getState().getPropertyValue("firstname"));
		assertEquals("bob",snapshots.get(1).getState().getPropertyValue("firstname"));
	}
}

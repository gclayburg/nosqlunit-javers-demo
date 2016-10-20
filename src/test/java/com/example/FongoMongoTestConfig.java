/*
 * VisualSync - a tool to visualize user data synchronization
 * Copyright (c) 2014 Gary Clayburg
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

package com.example;

import com.github.fakemongo.Fongo;
import com.lordofthejars.nosqlunit.mongodb.MongoDbConfiguration;
import com.lordofthejars.nosqlunit.mongodb.SpringMongoDbRule;
import com.mongodb.MockMongoClient;
import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

/**
 * Created by IntelliJ IDEA.
 * Date: 3/28/14
 * Time: 12:50 PM
 *
 * @author Gary Clayburg
 */

@Configuration
@EnableMongoRepositories
@Import(value = FongoMongoClient.class)
public class FongoMongoTestConfig extends AbstractMongoConfiguration {

	@SuppressWarnings("UnusedDeclaration")
	private static final Logger log = LoggerFactory.getLogger(FongoMongoTestConfig.class);
	public static final String DEMO_TEST = "demo-test";

	@Autowired
	private MongoClient mongoClient;

	@Autowired
	private MongoProperties mongoProperties;

	public static SpringMongoDbRule getSpringFongoMongoDbRule() {
		log.info("configuring nosql-unit to use MongoDB database: {}",DEMO_TEST);
		MongoDbConfiguration mongoDbConfiguration = new MongoDbConfiguration();
		mongoDbConfiguration.setDatabaseName(DEMO_TEST);
		MongoClient mongo = MockMongoClient.create(new Fongo("this-fongo-instance-is-ignored"));
		mongoDbConfiguration.setMongo(mongo);
		return new SpringMongoDbRule(mongoDbConfiguration);
	}

	@Override
	protected String getDatabaseName() {
		return DEMO_TEST; // name must match nosqlunit MongoDbRule, cannot be null
	}

	/**
	 * uses Fongo for in-memory tests - nosqlunit looks up Mongo bean and not MongoClient
	 * we return the same Fongo instance created for MongoClient bean so that
	 * nosqlunit based tests insert data into the same underlying database that our service beans use
	 *
	 * @return
	 */
	@Bean
	@Override
	public Mongo mongo() {
		log.info("configuring in-memory mongo from mongoClient");
		mongoProperties.setDatabase(DEMO_TEST);  //needed for JaversMongoAutoConfiguration
		return mongoClient;
	}
}

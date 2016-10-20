package com.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Created by IntelliJ IDEA.
 * Date: 10/20/16
 * Time: 1:36 PM
 *
 * @author Gary Clayburg
 */
@Document
public class User {

	@SuppressWarnings("UnusedDeclaration")
	private static final Logger log = LoggerFactory.getLogger(User.class);

	@Id
	private String id;
	private String username;
	private String firstname;
	private String lastname;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}
}

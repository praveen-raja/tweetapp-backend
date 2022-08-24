package com.tweetapp.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler" })
public class User {

	@Column
	@NotNull(message = "Firstname should not be null")
	private String firstName;
	@Column
	@NotNull(message = "Lastname should not be null")
	private String lastName;
	@Id
	@Email(message = "Invalid email address")
	@Pattern(regexp = "^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$", message = "Invalid email address")
	@Column(name = "email")
	@NotNull(message = "Email should not be null")
	private String email;
	@Column
	private boolean loginId;
	@Column
	@NotNull(message = "Password should not be null")
	private String password;
	@Column
	@NotNull(message = "Contact number should not be null")
	@Pattern(regexp = "(0/91)?[7-9][0-9]{9}", message = "Invalid contact number")
	private String contactNumber;

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
	@JsonIgnore
	private List<Tweet> tweets = new ArrayList<>();

	public void addTweet(Tweet tweet) {
		if (tweet != null) {
			if (tweets == null) {
				tweets = new ArrayList<>();
			}
			tweets.add(tweet);
			tweet.setUser(this);
		}
	}
}

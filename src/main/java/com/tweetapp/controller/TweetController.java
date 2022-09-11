package com.tweetapp.controller;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.tweetapp.entity.Comment;
import com.tweetapp.entity.Tweet;
import com.tweetapp.entity.User;
import com.tweetapp.repository.CommentRepository;
import com.tweetapp.repository.TweetRepository;
import com.tweetapp.repository.UserRepository;
import com.tweetapp.service.impl.TweetServiceImpl;


@RestController
@CrossOrigin("http://tweetapp-frontend-lb-1733541662.us-east-1.elb.amazonaws.com/")
public class TweetController {
	
	private static final Logger log = LogManager.getLogger(TweetController.class);

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private TweetRepository tweetRepository;

	@Autowired
	private TweetServiceImpl tweetServiceImpl;

	@Autowired
	CommentRepository commentRepository;

	@Autowired
	private KafkaTemplate<String, Object> kafkaTemplate;

	private static String loggedInUserEmailID = "";

	@Value("${kafka.topic.value}")
	private String topic;

	static User users = new User();
	static Tweet tweetsData = new Tweet();
	static String message = "";

	@PostMapping("/register")
	public User registerUser(@RequestBody @Valid User user) {

		log.info("start registerUser()");
		log.info("setting values to user");
		users.setEmail(user.getEmail());
		users.setContactNumber(user.getContactNumber());
		users.setFirstName(user.getFirstName());
		users.setLastName(user.getLastName());
		users.setPassword(user.getPassword());
		log.info("end registerUser()");
		//kafkaTemplate.send(topic, "User Details Are: " + user);
		return userRepository.save(users);

	}

	@GetMapping("/allUsers")
	public List<User> findAllUsers() {
		log.info("start getAllUsers()");
		log.info("end getAllUsers()");
		
		List<User> user = userRepository.findAll();
		if (user == null) {
//			log.error("No user available");
			throw new RuntimeException("Something went wrong!! Please try again.");
		} else {
			log.info("Returning all user");
			//kafkaTemplate.send(topic, "User Details Are: " + user);
			return user;
		}
	}

	@GetMapping("/login/{username}/{password}")
	public void login(@PathVariable String username, @PathVariable String password) {
		log.info("start login()");
		log.info("Username from path variable " + username + ", Password from path variable " + password);
		Optional<User> userDetails = userRepository.findById(username);
		if (userDetails.isPresent()) {
			//kafkaTemplate.send(topic, username);
			//System.out.println(userDetails);
		}
		String userName = userDetails.get().getEmail();
		String passWord = userDetails.get().getPassword();
		log.info("Username from repository " + userName + " Password from repository " + passWord);
		log.info("checking path values prior to save");
		if ((userName.equals(username)) && (passWord.equals(password))) {
			userDetails.get().setLoginId(true);
			log.info("saving user details");
			//if (userDetails.isEmpty()) {
			//	throw new RuntimeException("Could not find user using given inputs " + username + " Not Found");
			//} 
			//else {
				userRepository.save(userDetails.get());
				TweetController.loggedInUserEmailID = userName;
				log.info(TweetController.loggedInUserEmailID);
				log.info("end getAllUsers()");
				TweetController.message = "successfully logged in";
				log.info(username+ " Loggedin Successfully!!");
			//}

		} else {
			log.info("Please check your Input");
		}
	}

	@GetMapping("/forgotPassword/{username}/{updatedPassword}")
	public void forgotPassword(@PathVariable String username, @PathVariable String updatedPassword) {
		log.info("start forgotPassword()");
		User userDetails = userRepository.findByEmail(username);
		String email = userDetails.getEmail();
		if (username.equals(email)) {
			userDetails.setPassword(updatedPassword);
			log.info("saving updated user password");
			userRepository.save(userDetails);
			log.info("end forgotPassword()");
			log.info("Password Updated Successfully!!");
		} else {
			log.info("Invalid Username");
		}
	}

	@GetMapping("/searchByUsername/{username}")
	public User searchByUsername(@PathVariable String username) {
		log.info("start searchByUsername()");
		log.info("end searchByUsername()");
		User user = userRepository.findByEmail(username);
		if (user == null) {
			return null;
		} else {
			return user;
		}
	}

	@PostMapping("/addTweet")
	public Tweet postTweet(@RequestBody @Valid Tweet tweet) {
		log.info("start postTweet()");
		log.info("setting loggedIn user email to user Obj");
		users.setEmail(loggedInUserEmailID);
		tweet.setUser(users);
		log.info("User Tweet Foriegn value -> ", users.getEmail());
		log.info("Tweet email address -> ", TweetController.loggedInUserEmailID);
		log.info("end postTweet()");
		// kafkaTemplate.send(topic, "User Tweets Are: " + tweet);
		log.info("Consumer output -->", tweet.getUser());
		return tweetRepository.save(tweet);
	}

	@GetMapping("/allTweet")
	public List<Tweet> findAllTweets() {
		log.info("start findAllTweets()");
		log.info("end findAllTweets()");
		return tweetRepository.findAll();
	}

	@PutMapping("/updateTweet/{id}/{username}")
	public Tweet updatedTweet(@PathVariable Long id, @PathVariable String username, @RequestBody Tweet tweet) {
		log.info("start updatedTweet()");
		log.info("path id  -> ", id);
		log.info("path id  -> ", username);
		Optional<Tweet> tweets = tweetRepository.findById(id);
		String email = tweets.get().getUser().getEmail();
		Long uId = tweets.get().getId();
		log.info("id  -> ", uId);
		log.info("email -> ", email);
		if (email.equals(username) && uId.equals(id)) {
			users.setEmail(username);
			tweet.setUser(users);
			log.info("saving tweet details to tweet repository");
			tweetRepository.save(tweet);
			log.info("end updatedTweet()");
			log.info("Updated Succesfully!!!");
			return tweet;
		} else {
			log.info("Oops Something Went Wrong!!");
			return tweet;
		}
	}

	@DeleteMapping("/deleteTweet/{id}/{username}")
	public void DeleteTweet(@PathVariable Long id, @PathVariable String username) {
		log.info("start DeleteTweet()");
		log.info("path id -> ", id);
		log.info("path id -> ", username);
		log.info("finding tweet by id");
		Optional<Tweet> tweets = tweetRepository.findById(id);
		String email = tweets.get().getUser().getEmail();
		Long uId = tweets.get().getId();
		log.info("id -> ", uId);
		log.info("email -> ", email);
		if (email.equals(username) && uId.equals(id)) {
			tweetRepository.deleteById(uId);
			log.info("end DeleteTweet()");
			log.info("Deleted Successfully");
		} else {
			log.info("Please check Input");
		}
	}

	@GetMapping("/getTweetOfUser/{username}")
	public List<Tweet> getAllTweetsOfUser(@PathVariable String username) {
		log.info("start getAllTweetsOfUser()");
		User fetchedUser = userRepository.findByEmail(username);
		fetchedUser.getEmail();
		log.info("fetching list of tweets");
		List<Tweet> fetchedTweet = fetchedUser.getTweets();
		log.info("list of tweets -> ", fetchedTweet);
		log.info("end getAllTweetsOfUser()");
		return fetchedTweet;
	}

	@PostMapping("reply/{tweetId}")
	public Comment storeComment(@PathVariable Long tweetId, @RequestBody @Valid Comment comment) {
		log.info("start storeComment()");
		boolean isCommentInserted = false;
		String username = TweetController.loggedInUserEmailID;
		log.info("setting loggedIn user email");
		comment.setCommentedBy(username);
		comment.setTweet(tweetsData);
		comment.setInsertTime(new Date());
		isCommentInserted = tweetServiceImpl.addComment(comment, tweetId);
		if (isCommentInserted) {
			log.info("saving comment to comment repository");
			commentRepository.save(comment);
			log.info("end storeComment()");
			log.info("Comment Added Successfully!!");
			return comment;
		} else {
			log.info("No Comments Found To Add!!");
			return null;
		}
	}

	@PutMapping("like/{tweetId}/{option}")
	public void proccessLike(@PathVariable Long tweetId, @PathVariable Integer option) {
		log.info("start proccessLike()");
		boolean isTweetDataUpdated = false;
		String oprationType = option == 1 ? "Like" : "Dis-like";
		String username = TweetController.loggedInUserEmailID;
		if (option == 1) {
			// perform the like
			log.info("perform like");
			isTweetDataUpdated = tweetServiceImpl.performLike(tweetId, username);
		} else if (option == 0) {
			// perform the dislike
			log.info("perform dislike");
			isTweetDataUpdated = tweetServiceImpl.performDislike(tweetId, username);
		} else {
			log.info("Wrong Option");
			throw new RuntimeException("Invalid Input");

		}
		if (isTweetDataUpdated) {
			log.info("end proccessLike()");
			log.info("Thanks for the feedback!!");
		} else {
			log.info("OOPS!!");
		}
	}

	@GetMapping("/logout")
	public String logout() {
		log.info("Start logout()");
		String username = TweetController.loggedInUserEmailID;
		log.info(username);
		Optional<User> userDetails = userRepository.findById(username);
		userDetails.get().setLoginId(false);
		userRepository.save(userDetails.get());
		log.info("end logout()");
		return "Loggedout Successfully";

	}
}

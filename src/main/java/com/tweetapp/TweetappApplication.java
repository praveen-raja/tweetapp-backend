package com.tweetapp;



import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TweetappApplication {
	
	private static final Logger log = LogManager.getLogger(TweetappApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(TweetappApplication.class, args);
		log.info("Application started : TweetappApplication");
	}

}

package com.tweetapp.service.impl;

import java.util.Optional;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tweetapp.entity.Comment;
import com.tweetapp.entity.Tweet;
import com.tweetapp.repository.CommentRepository;
import com.tweetapp.repository.TweetRepository;
import com.tweetapp.service.TweetService;


@Service
public class TweetServiceImpl implements TweetService {
	
	private static final Logger log = LogManager.getLogger(KafkaConsumerService.class);

	@Autowired
	TweetRepository tweetRepository;

	@Autowired
	CommentRepository commentRepository;

	@Override
	public boolean performLike(Long tweetId, String username) {
		log.info("Start: performLike()");
		boolean result = false;
		log.info("Fetching the tweet Object with Id: " + tweetId);
		Optional<Tweet> tweetOptional = tweetRepository.findById(tweetId);
		log.info("Fetched the tweet Object with Id: " + tweetId);
		if (tweetOptional.isPresent()) {
			log.info("Tweet Object present with Id: " + tweetId);
			Tweet tweetObj = tweetOptional.get();
			Set<String> likedBy = tweetObj.getLikedBy();
			likedBy.add(username);
			tweetObj.setLikedBy(likedBy);
			log.info("Storing the updated (liked) tweet object");
			tweetRepository.save(tweetObj);
			log.info("Stored the updated (liked) tweet object");
			log.info("Success performed like on the tweetId: " + tweetId + " by the user: " + username);
			result = true;
		} else {
			log.error("No Tweet object found with the tweetId: " + tweetId);
			log.error("Error in performing like on the tweetId: " + tweetId + " by the user: " + username);
		}

		log.info("End: performLike()");
		return result;
	}

	@Override
	public boolean performDislike(Long tweetId, String username) {
		log.info("Start: performDislike()");
		boolean result = false;
		log.info("Fetching the tweet Object with Id: " + tweetId);
		Optional<Tweet> tweetOptional = tweetRepository.findById(tweetId);
		log.info("Fetched the tweet Object with Id: " + tweetId);
		if (tweetOptional.isPresent()) {
			log.info("Tweet Object present with Id: " + tweetId);
			Tweet tweetObj = tweetOptional.get();
			Set<String> likedBy = tweetObj.getLikedBy();
			likedBy.remove(username);
			tweetObj.setLikedBy(likedBy);
			log.info("Storing the updated (dis-liked) tweet object");
			tweetRepository.save(tweetObj);
			log.info("Storing the updated (dis-liked) tweet object");
			log.info("Success performed dis-like on the tweetId: " + tweetId + " by the user: " + username);
			result = true;
		} else {
			log.error("No Tweet object found with the tweetId: " + tweetId);
			log.error("Error in performing dis-like on the tweetId: " + tweetId + " by the user: " + username);
		}

		log.info("End: performDislike()");
		return result;
	}

	@Override
	public boolean addComment(Comment comment, Long tweetId) {
		log.info("Start: addComment()");
		boolean result = false;
		log.info("Fetching the tweet Object with Id: " + tweetId);
		Optional<Tweet> tweetOptional = tweetRepository.findById(tweetId);
		log.info("Fetched the tweet Object with Id: " + tweetId);
		if (tweetOptional.isPresent()) {
			Tweet tweetObj = tweetOptional.get();
			comment.setTweet(tweetObj);
			log.info("Storing the comment object on tweet with tweetId: " + tweetId);
			Comment savedComment = commentRepository.save(comment);
			if (savedComment.getCommentId() > 0) {
				log.info("Stored the comment object on tweet with tweetId: " + tweetId);
				result = true;
			} else {
				log.error("Error in storing the comment object on tweet with tweetId: " + tweetId);
			}
		} else {
			log.error("No Tweet object found with the tweetId: " + tweetId);
			log.error("Error in commenting the tweet with tweetId: " + tweetId + " by the user: "
					+ comment.getCommentedBy());
		}
		log.info("End: addComment()");
		return result;
	}
}

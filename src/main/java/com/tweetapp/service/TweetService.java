package com.tweetapp.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tweetapp.entity.Comment;
import com.tweetapp.entity.Tweet;
import com.tweetapp.entity.User;
import com.tweetapp.repository.UserRepository;

@Service
public interface TweetService {

	boolean performLike(Long tweetId, String username);

	boolean performDislike(Long tweetId, String username);

	boolean addComment(Comment comment, Long tweetId);;

}

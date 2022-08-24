package com.tweetapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tweetapp.entity.Tweet;

public interface TweetRepository extends JpaRepository<Tweet, Long> {

}

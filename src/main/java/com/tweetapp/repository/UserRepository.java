package com.tweetapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tweetapp.entity.User;

public interface UserRepository extends JpaRepository<User, String> {
	
	User findByEmail(String email);
}

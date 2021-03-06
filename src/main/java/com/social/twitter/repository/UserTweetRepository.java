package com.social.twitter.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.social.twitter.model.UserTweet;


public interface UserTweetRepository  extends JpaRepository<UserTweet, Integer> {
	Page<UserTweet> findByTUID(long tuid, Pageable pageable);
}

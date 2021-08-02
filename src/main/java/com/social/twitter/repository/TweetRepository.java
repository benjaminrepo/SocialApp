package com.social.twitter.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.social.twitter.model.Tweet;


public interface TweetRepository  extends JpaRepository<Tweet, Integer> {
	Page<Tweet> findByUserId(long TUID, Pageable pageable);
}

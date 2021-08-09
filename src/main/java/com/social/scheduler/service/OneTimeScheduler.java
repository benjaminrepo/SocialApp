package com.social.scheduler.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.social.twitter.model.Tweet;
import com.social.twitter.service.TweetService;
import com.social.users.model.SocialUser;
import com.social.users.service.SocialUserService;

@Service
public class OneTimeScheduler {

	@Autowired
	TweetService tweetService;

	@Autowired
	SocialUserService userService;
	public List<Tweet> updateStatusForUser(SocialUser socialUser) {
		
		StatusForUserTask task = new StatusForUserTask(socialUser, userService, tweetService);
		List<Tweet> res = task.getTweetList();
		task.run();
		
		return res;
	}
}

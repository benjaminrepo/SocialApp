package com.social.scheduler.service;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.social.twitter.model.Tweet;
import com.social.twitter.service.TweetService;
import com.social.twitter.service.TwitterAPIService;
import com.social.users.model.SocialUser;
import com.social.users.service.SocialUserService;


public class StatusForUserTask implements Runnable {

	private static final Logger LOGGER = LoggerFactory.getLogger(StatusForUserTask.class);
	private SocialUserService userService;
	private SocialUser socialUser;
	private TweetService tweetService;
	private List<Tweet> tweetList;


	public StatusForUserTask(SocialUser user, SocialUserService userService, TweetService tStatusService) {
		this.socialUser = user;
		this.userService = userService;
		this.tweetService = tStatusService;
	}

	@Override
	public void run() {
		try {
			if(tweetList == null) {
				tweetList = getTweetList();
			}
			tweetService.addUpdateUserTweets(tweetList, socialUser);
			LOGGER.info("status updated {} ", socialUser.getTUID());
			userService.updateUpdatedTiime(socialUser);

		} catch (Exception e) {
			LOGGER.error(" unable to update for ", socialUser.getTUID());
			e.printStackTrace();
		}
	}
	
	public List<Tweet> getTweetList() {
		return tweetList = TwitterAPIService
				.fetchUserTimelineFromTwitter(socialUser)
				.stream()
				.map(tweet -> TwitterAPIService.mapTweetDO(tweet))
				.collect(Collectors.toList());

	}

}

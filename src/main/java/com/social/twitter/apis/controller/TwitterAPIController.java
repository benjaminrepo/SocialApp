package com.social.twitter.apis.controller;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.social.auth.util.SocialUserUtil;
import com.social.scheduler.service.UserScheduledTasks;
import com.social.twitter.dataAccess.service.TweetService;

import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;

@RestController
public class TwitterAPIController {
	private static final Logger LOGGER = LoggerFactory.getLogger(UserScheduledTasks.class);

	@Autowired
	TweetService tStatusService;

	@RequestMapping("/scheduler/getSyncUserTimeline")
	public ResponseList<Status> getTimeLine(HttpServletRequest request) throws TwitterException {
		Twitter twitter = (Twitter) SocialUserUtil.getLoggedInUser().getTwitter();
		ResponseList<Status> timeline = twitter.getUserTimeline();
		try {
			long socialId = SocialUserUtil.getLoggedInUser().getId();
			for (Status status : timeline) {
				tStatusService.addNewTweetsForUser(status, socialId, SocialUserUtil.getLoggedInUser().getTUID());

			}
		} catch (Exception e) {
			LOGGER.error("getSyncUserTimeline twitter: {} \ntimeline: {}", twitter, timeline);
			e.printStackTrace();
		}
		return timeline;
	}

}

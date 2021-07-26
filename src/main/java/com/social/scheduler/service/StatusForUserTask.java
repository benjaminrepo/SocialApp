package com.social.scheduler.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.SerializationUtils;

import com.social.twitter.dataAccess.service.TweetService;
import com.social.users.model.SocialUser;
import com.social.users.service.SocialUserService;

import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.Twitter;

@Component
public class StatusForUserTask implements Runnable {

	private static final Logger LOGGER = LoggerFactory.getLogger(StatusForUserTask.class);
	private SocialUserService userService;
	private SocialUser socialUser;
	private TweetService tStatusService;

	public StatusForUserTask() {

	}

	public StatusForUserTask(SocialUser user, SocialUserService userService, TweetService tStatusService) {
		this.socialUser = user;
		this.userService = userService;
		this.tStatusService = tStatusService;
	}

	@Override
	public void run() {
		long TUID = 0;
		try {
			Twitter twitter = (Twitter) SerializationUtils.deserialize(socialUser.getTwitterObj());
			long SID = socialUser.getId();
			TUID = socialUser.getTUID();

			// ResponseList<Status> timeline = twitter.getHomeTimeline();
			ResponseList<Status> timeline = twitter.getUserTimeline();

			for (Status status : timeline) {

				LOGGER.debug("status to update {} ", status);
				tStatusService.addNewTweetsForUser(status, SID, TUID);

			}
			userService.updateUpdatedTiime(this.socialUser);

		} catch (Exception e) {
			LOGGER.error(" unable to update for ", TUID);
			e.printStackTrace();
		}
	}

}

package com.social.scheduler.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.social.config.SocialConfigurationProperties;
import com.social.twitter.dataAccess.service.TweetService;
import com.social.users.model.SocialUser;
import com.social.users.service.SocialUserService;

@Component
public class UserScheduledTasks {

	@Autowired
	SocialUserService userService;

	@Autowired
	TweetService tStatusService;

	@Autowired
	SocialConfigurationProperties config;

	private static AtomicBoolean isSchedulerStarted = new AtomicBoolean(false);

	private static final Logger LOGGER = LoggerFactory.getLogger(UserScheduledTasks.class);
	private ExecutorService executor;
	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
	private static final int USER_COUNT = 10;

	@Scheduled(fixedRate = 1000 * 60 * 1)
	public void run() {

		if (!isSchedulerStarted.get()) {
			LOGGER.info("Scheduler paused {}", dateFormat.format(new Date()));
			return;
		}
		LOGGER.info("Scheduler started {}", dateFormat.format(new Date()));
		if (executor == null) {
			executor = Executors.newFixedThreadPool(config.getUserThreads());
		}
		int sIndex = 0;
		List<SocialUser> users = userService.getUserListForScheduler(sIndex, USER_COUNT);
		while (users.size() != 0) {
			for (SocialUser user : users) {
				executor.execute(new StatusForUserTask(user, userService, tStatusService));
			}
			sIndex += 10;
			users = userService.getUserListForScheduler(sIndex, USER_COUNT);
		}
		LOGGER.info("Scheduler ended {}", dateFormat.format(new Date()));
	}

	public static void resumeScheduler() {
		isSchedulerStarted = new AtomicBoolean(true);
	}

	public static void pauseScheduler() {
		isSchedulerStarted = new AtomicBoolean(false);
	}

	public static boolean getSchedulerStatus() {
		return isSchedulerStarted.get();
	}

}

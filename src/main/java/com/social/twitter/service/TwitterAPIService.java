package com.social.twitter.service;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.modelmapper.config.Configuration.AccessLevel;
import org.modelmapper.convention.MatchingStrategies;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.social.twitter.model.TUser;
import com.social.twitter.model.Tweet;
import com.social.users.model.SocialUser;

import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;

public class TwitterAPIService {

	private static final Logger LOGGER = LoggerFactory.getLogger(TwitterAPIService.class);

	public static ResponseList<Status> fetchUserTimelineFromTwitter(SocialUser socialUser) {
		try {
			
			Twitter twitter = socialUser.getTwitter();
			// ResponseList<Status> timeline = twitter.getHomeTimeline();
			
			//TODO : based on the last synced time fetch only updated tweets from Twitter.
			Pageable paging = PageRequest.of(0, 20, Sort.by("createdAt").descending());
			ResponseList<Status> timeline = twitter.getUserTimeline();
			return timeline;
		} catch (TwitterException e) {
			LOGGER.error("Unable to fetch user tweets for {}", socialUser.getTUID());
		}
		return null;
	}
	
	public static Tweet mapTweetDO(Status status) {
		ModelMapper modelMapper = new ModelMapper();

		modelMapper.getConfiguration().setFieldMatchingEnabled(true).setMatchingStrategy(MatchingStrategies.STRICT)
				.setFieldAccessLevel(AccessLevel.PRIVATE);

		Tweet tweet = modelMapper.map(status, Tweet.class);

		TUser tuser = modelMapper.map(status.getUser(), TUser.class);
		tweet.setUser(tuser);
		LOGGER.debug("User updated {}", tuser.getId());

		if (status.getRetweetedStatus() != null) {
			Tweet retweetedStatus = modelMapper.map(status.getRetweetedStatus(), Tweet.class);
			tweet.setRetweetedStatus(retweetedStatus);
			LOGGER.debug("getRetweetedStatus updated {}", retweetedStatus.getId());
		}

		if (status.getUserMentionEntities().length != 0) {
			Set<TUser> userMentionEntities = Arrays.asList(status.getUserMentionEntities()).stream()
					.map(u -> modelMapper.map(u, TUser.class)).collect(Collectors.toSet());
			tweet.setUserMentionEntities(userMentionEntities);
			LOGGER.debug("userMentionEntities updated");
		}
		tweet.setUserMentionEntities(null);
		if (status.getQuotedStatus() != null) {
			Tweet quotedStatus = modelMapper.map(status.getQuotedStatus(), Tweet.class);
			quotedStatus.setUser(modelMapper.map(quotedStatus.getUser(), TUser.class));
			tweet.setRetweetedStatus(quotedStatus);
			LOGGER.debug("quotedStatus updated");
		}

		if (status.getContributors().length != 0) {
			Set<TUser> contributorsIDs = Arrays.asList(status.getContributors()).stream()
					.map(u -> modelMapper.map(u, TUser.class)).collect(Collectors.toSet());
			tweet.setUserMentionEntities(contributorsIDs);
			LOGGER.debug("contributorsIDs updated");
		}

		LOGGER.debug("tweet updated {}", tweet.getId());
		return tweet;

	}

}

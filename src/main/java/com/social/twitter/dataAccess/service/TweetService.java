package com.social.twitter.dataAccess.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.modelmapper.config.Configuration.AccessLevel;
import org.modelmapper.convention.MatchingStrategies;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.social.twitter.dataAccess.model.TUser;
import com.social.twitter.dataAccess.model.Tweet;
import com.social.twitter.dataAccess.repository.TweetRepository;

import twitter4j.Status;

@Service
public class TweetService {
	private static final Logger LOGGER = LoggerFactory.getLogger(TweetService.class);

	@Autowired
	TweetRepository tweetRepository;

	public void addNewTweetsForUser(Status status, long SID, long TUID) {
		try {
			Tweet tweet = mapTweetDO(status);
			tweetRepository.save(tweet);
			LOGGER.info("userStatus updated {}", tweet.getId());

		} catch (Exception e) {
			LOGGER.error("Tweet not updated for {} {} {} ", SID, TUID, e.getMessage());
		}

	}

	public List<Tweet> getRecentStatus(long TUID) {
		LOGGER.debug("get recent Status TUID {}", TUID);
		try {
			Pageable paging = PageRequest.of(0, 20, Sort.by("createdAt").descending());
			Page<Tweet> pagedResult = tweetRepository.findByUserId(TUID, paging);
			if (pagedResult.hasContent()) {
				return pagedResult.getContent();
			}
		} catch (Exception e) {
			LOGGER.error("tuid {}", TUID);
		}
		return new ArrayList<Tweet>();
	}

	private Tweet mapTweetDO(Status status) {
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

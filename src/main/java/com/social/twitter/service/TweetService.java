package com.social.twitter.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.social.twitter.model.SearchCriteria;
import com.social.twitter.model.Tweet;
import com.social.twitter.repository.TweetDAO;
import com.social.twitter.repository.TweetRepository;
import com.social.users.model.SocialUser;

@Service
public class TweetService {
	private static final Logger LOGGER = LoggerFactory.getLogger(TweetService.class);

	@Autowired
	TweetRepository tweetRepository;

	@Autowired
	TweetDAO tweetDAO;

	public void addNewTweetsForUser(List<Tweet> tweets, SocialUser socialUser) {
		try {
			tweetRepository.saveAll(tweets);
			LOGGER.info("tweets updated for {}", socialUser.getTUID());

		} catch (Exception e) {
			LOGGER.error("Tweet not updated for {} {} {} ", socialUser.getId(), socialUser.getTUID(), e.getMessage());
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

	public List<Tweet> searchStatus(long TUID, List<SearchCriteria> params, Pageable pageable) {

		List<Tweet> results = tweetDAO.searchTweets(params, pageable);

		return results;

	}

}

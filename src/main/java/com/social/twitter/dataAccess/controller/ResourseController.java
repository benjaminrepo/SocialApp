package com.social.twitter.dataAccess.controller;

import java.lang.reflect.Type;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.social.auth.util.SocialUserUtil;
import com.social.twitter.dataAccess.model.Tweet;
import com.social.twitter.dataAccess.service.TweetService;

import twitter4j.Twitter;
import twitter4j.TwitterException;

@RestController
public class ResourseController {

	@Autowired
	TweetService tweetService;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ResourseController.class);
	

    @RequestMapping("twitter/dataAccess/getRecent")
    public List<Tweet> getRecentHome(
    		@RequestParam(defaultValue = "0") Integer pageNo, 
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(defaultValue = "id") String sortBy) {
    	
    	return tweetService.getRecentStatus(SocialUserUtil.getLoggedInUser().getTUID());
    }

    @RequestMapping("twitter/dataAccess/search")
    public String twiterLogin(
    		@RequestParam(defaultValue = "0") Integer pageNo, 
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(defaultValue = "id") String sortBy) {
    	
    	
		 Type typeOfSrc = new TypeToken<List<Tweet>>(){}.getType();
		 List<Tweet> pagedResult = tweetService.getRecentStatus(SocialUserUtil.getLoggedInUser().getTUID());
		
    	return new Gson().toJson(pagedResult, typeOfSrc);
    }
        
    @RequestMapping("twitter/dataAccess/getUserName")
    public String home(HttpServletRequest request) {
    	String userName = "";
    	try {
        	Twitter twitter = (Twitter) SocialUserUtil.getLoggedInUser().getTwitter();
        	userName = twitter.getScreenName();
		} catch (IllegalStateException | TwitterException e) {
			LOGGER.error("get username {}", e.getMessage());
		}
    	return userName;
    }
}

package com.social.twitter.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.social.auth.util.SocialUserUtil;
import com.social.twitter.model.SearchCriteria;
import com.social.twitter.model.Tweet;
import com.social.twitter.service.TweetService;

import twitter4j.Twitter;
import twitter4j.TwitterException;

@RestController
public class ResourseController {

	@Autowired
	TweetService tweetService;
	

	
	private static final Logger LOGGER = LoggerFactory.getLogger(ResourseController.class);
	

    @RequestMapping("twitter/getRecent")
    public List<Tweet> getRecentHome(
    		@RequestParam(defaultValue = "0") Integer pageNo, 
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(defaultValue = "id") String sortBy) {
    	
    	return tweetService.getRecentStatus(SocialUserUtil.getLoggedInUser().getTUID());
    }

    @RequestMapping("twitter/search")
    public List<Tweet> search(
    		@RequestParam(value = "searchTweet", required = false) String searchTweet,
			@RequestParam(value = "searchUser", required = false) String searchUsers,
			@RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
			@RequestParam(value = "size", required = false, defaultValue = "10") Integer size,
			@RequestParam(value = "orderBy", required = false, defaultValue = "createdAt") String orderBy,
			@RequestParam(value = "direction", required = false, defaultValue = "desc") String direction) {
    	
    	List<SearchCriteria> paramTweet = this.createSearchCriteria(searchTweet);
		List<SearchCriteria> paramUsers = this.createSearchCriteria(searchUsers);
    	
    	long TUID = SocialUserUtil.getLoggedInUser().getTUID();
//    	paramTweet.add(new SearchCriteria("user_id", ":", TUID));
    	
    	if(size > 100 ) size = 100;
    	    	
    	Pageable pageable = PageRequest.of(page, size , Direction.fromString(direction), orderBy);
		
    	return tweetService.searchStatus(TUID, paramTweet, pageable);
    }
    
    
    private List<SearchCriteria> createSearchCriteria(String search) {
		List<SearchCriteria> params = new ArrayList<SearchCriteria>();
        if (search != null) {
            //Pattern pattern = Pattern.compile("(\\w+?)(:|!:|<|<=|>|>=)(\\w+?),");
            Pattern pattern = Pattern.compile("(\\w+?)(:|<|>)(\\S+?),");
            Matcher matcher = pattern.matcher(search + ",");
            while (matcher.find()) {
            	params.add(new SearchCriteria(matcher.group(1), 
                  matcher.group(2), matcher.group(3)));
            }
        }
        return params;
	}
    
    
    @RequestMapping("twitter/search1")
    public List<Tweet> search1() {
    	
    	List<SearchCriteria> params = new ArrayList<SearchCriteria>();
        params.add(new SearchCriteria("text", ":", "test"));
        //params.add(new SearchCriteria("id", ":", "1412072674709237764"));
        
        
        //params.add(new SearchCriteria("retweetCount", ">", "2"));

       // List<Tweet> results = tweetDAO.searchTweets(params);
		
    	return null;
    }        
    @RequestMapping("twitter/getUserName")
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

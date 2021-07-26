package com.social.twitter.auth.controller;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.RedirectView;

import com.social.config.SocialConfigurationProperties;

import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.auth.RequestToken;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

@Controller
public class GetTokenController {

	private static String consumerKey = "PeZXiz9qYeZZjNTGUU5RNSgzs";
	private static String consumerSecret = "uW5GhO39CNKpRdRpwh4o6LR90CZJij6FCKKTqFr8bQByG1H3fE";

	@Autowired
	SocialConfigurationProperties config;
	private static final Logger LOGGER = LoggerFactory.getLogger(GetTokenController.class);
	
	@RequestMapping("/twitter/auth/getToken")
    public RedirectView getToken(HttpServletRequest request,Model model) {

    	String twitterUrl = "";    	
		try {
			Twitter twitter = getTwitter();
			String callbackUrl = config.getTwitter().get("callbackUrl");
			RequestToken requestToken = twitter.getOAuthRequestToken(callbackUrl);
			request.getSession().setAttribute("requestToken", requestToken);
			request.getSession().setAttribute("twitter", twitter);
			twitterUrl = requestToken.getAuthorizationURL();
			
			LOGGER.info("Authorization url is " + twitterUrl);
		} catch (Exception e) {
			LOGGER.error("Problem logging in with Twitter!", e);
		}
    	
		//redirect to the Twitter URL
		RedirectView redirectView = new RedirectView();
	    redirectView.setUrl(twitterUrl);
	    return redirectView;
    }

    
    /*
     * Instantiates the Twitter object
     */
    public Twitter getTwitter() {
    	Twitter twitter = null;
    	
    	//set the consumer key and secret for our app
    	
		//build the configuration
    	ConfigurationBuilder builder = new ConfigurationBuilder();
    	builder.setOAuthConsumerKey(config.getTwitter().get("consumerKey"));
    	builder.setOAuthConsumerSecret(config.getTwitter().get("consumerSecret"));
    	Configuration configuration = builder.build();
    	
    	//instantiate the Twitter object with the configuration
    	TwitterFactory factory = new TwitterFactory(configuration);
    	twitter = factory.getInstance();
    	
    	return twitter;
    }
}

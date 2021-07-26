package com.social.config;

import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "social")
public class SocialConfigurationProperties {

	private int userThreads = 10;
	private Map<String, String> twitter;
	

	public Map<String, String> getTwitter() {
		return twitter;
	}

	public void setTwitter(Map<String, String> twitter) {
		this.twitter = twitter;
	}

	public int getUserThreads() {
		return userThreads;
	}

	public void setUserThreads(int userThreads) {
		this.userThreads = userThreads;
	}

}

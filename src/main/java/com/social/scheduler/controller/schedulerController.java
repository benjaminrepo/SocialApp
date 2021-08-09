package com.social.scheduler.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.social.auth.util.SocialUserUtil;
import com.social.scheduler.service.OneTimeScheduler;
import com.social.scheduler.service.UserScheduledTasks;
import com.social.twitter.model.Tweet;
import com.social.users.model.SocialUser;

@RestController
public class schedulerController {
	//private static final Logger LOGGER = LoggerFactory.getLogger(schedulerController.class);
	
	@Autowired
	OneTimeScheduler onetimeTask;
	
    @RequestMapping("/scheduler/start")
    public String start() {
    	UserScheduledTasks.resumeScheduler();
    	return "started";
    }
    
    @RequestMapping("/scheduler/stop")
    public String stop() {
    	UserScheduledTasks.pauseScheduler();
    	return "stoped";
    }

    @RequestMapping("/scheduler/isRunning")
    public boolean status() {
    	return UserScheduledTasks.getSchedulerStatus();
    }
    
	@RequestMapping("/scheduler/oneTimeSync")
	public List<Tweet> getTimeLine(HttpServletRequest request) {
		SocialUser socialUser = SocialUserUtil.getLoggedInUser();
		return onetimeTask.updateStatusForUser(socialUser); 
	}

}

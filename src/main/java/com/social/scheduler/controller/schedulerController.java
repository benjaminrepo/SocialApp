package com.social.scheduler.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.social.scheduler.service.UserScheduledTasks;

@RestController
public class schedulerController {
	//private static final Logger LOGGER = LoggerFactory.getLogger(schedulerController.class);
	
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
}

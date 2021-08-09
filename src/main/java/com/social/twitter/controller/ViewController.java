package com.social.twitter.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ViewController {
	
	//private static final Logger LOGGER = LoggerFactory.getLogger(ViewController.class);
	
    @RequestMapping("/home")
    public String twiterLogin(Model model) {
    	return "home.html";
    }

    //redirect to demo if user hits the root
    @RequestMapping("/")
    public String init(Model model) {
    	return "redirect:home";
    }
}

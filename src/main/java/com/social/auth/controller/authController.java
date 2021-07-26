package com.social.auth.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.social.auth.util.UserAuthUtil;

@Controller
public class authController {
	private static final Logger LOGGER = LoggerFactory.getLogger(authController.class);

	@RequestMapping("/logout")
	public String getHomeTimeline(HttpServletRequest request, HttpServletResponse response) {
		LOGGER.info("loggout called");
		UserAuthUtil.removeSocialUserIDCookie(response);
		request.getSession().invalidate();
		return "redirect:login";
	}

	@RequestMapping("/login")
	public String twiterLogin(Model model) {
		LOGGER.info("login called");
		return "login.html";
	}
}

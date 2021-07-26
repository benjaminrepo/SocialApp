package com.social.twitter.auth.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.social.auth.util.UserAuthUtil;
import com.social.users.service.SocialUserService;

import twitter4j.Twitter;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;

@Controller
public class CallbackController {
	private static final Logger LOGGER = LoggerFactory.getLogger(CallbackController.class);

	@Autowired
	SocialUserService userService;

	@RequestMapping("/twitter/auth/callback")
	public String twitterCallback(@RequestParam(value = "oauth_verifier", required = false) String oauthVerifier,
			@RequestParam(value = "denied", required = false) String denied, HttpServletRequest request,
			HttpServletResponse response, Model model) {

		if (denied != null) {
			return "redirect:login";
		}

		Twitter twitter = (Twitter) request.getSession().getAttribute("twitter");
		RequestToken requestToken = (RequestToken) request.getSession().getAttribute("requestToken");

		try {
			AccessToken token = twitter.getOAuthAccessToken(requestToken, oauthVerifier);
			long tuserid = twitter.verifyCredentials().getId();
			// request.getSession().removeAttribute("requestToken");
			long socialId = userService.addUser(tuserid, token, twitter);

			UserAuthUtil.setUserSocialIDCookie(response, socialId);

			LOGGER.debug("callback success {} ", tuserid);

			return "redirect:/home";
		} catch (Exception e) {
			LOGGER.error("Problem getting token!", e);
			return "redirect:logout";
		}
	}

}

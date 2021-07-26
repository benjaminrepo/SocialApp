package com.social.auth.util;

import java.util.Arrays;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class UserAuthUtil {
	public static final String COOKIE_ID = "socialID";
	// private static final Logger LOGGER =
	// LoggerFactory.getLogger(UserAuthUtil.class);

	public static String getUserSocialID(HttpServletRequest request) {
		return getValueFromCookie(request, COOKIE_ID);
	}

	private static String getValueFromCookie(HttpServletRequest request, String key) {
		if (request.getCookies() == null)
			return "";
		return Arrays.stream(request.getCookies()).filter(c -> key.equals(c.getName())).map(Cookie::getValue).findAny()
				.orElse("");
	}

	// userid to be encrpted or use jwt
	public static void setUserSocialIDCookie(HttpServletResponse response, long userid) {
		Cookie userID = new Cookie(COOKIE_ID, userid + "");
		userID.setMaxAge(60 * 60 * 24 * 30);
		userID.setPath("/");
		userID.setHttpOnly(true);
		response.addCookie(userID);
	}

	public static void removeSocialUserIDCookie(HttpServletResponse response) {
		Cookie userID = new Cookie(COOKIE_ID, "");
		userID.setMaxAge(0);
		userID.setPath("/");
		response.addCookie(userID);

	}

}

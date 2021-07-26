package com.social.auth.util;

import com.social.users.model.SocialUser;

public class SocialUserUtil {
	private static final ThreadLocal<SocialUser> loggedInUser = new ThreadLocal<>();

	public static SocialUser getLoggedInUser() {
		return loggedInUser.get();
	}

	public static void setLoggedInUser(SocialUser socialUser) {
		loggedInUser.set(socialUser);
	}

	public static void delThreadLocal() {
		loggedInUser.remove();
	}

}

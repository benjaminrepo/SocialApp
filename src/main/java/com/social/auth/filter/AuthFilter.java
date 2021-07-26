package com.social.auth.filter;

import java.io.IOException;
import java.util.Arrays;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.social.auth.util.SocialUserUtil;
import com.social.auth.util.UserAuthUtil;
import com.social.users.model.SocialUser;
import com.social.users.service.SocialUserService;

@Component
@Order(1)
public class AuthFilter implements Filter {
	private static final Logger LOGGER = LoggerFactory.getLogger(AuthFilter.class);

	@Autowired
	SocialUserService userService;

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		String[] staticURLs = new String[] { ".js", ".css" };
		if (Arrays.asList(staticURLs).stream()
				.anyMatch(u -> ((HttpServletRequest) request).getRequestURI().endsWith(u))) {
			proceed(request, response, chain);
			return;
		}

		if (isAuthenticaltedURL((HttpServletRequest) request, (HttpServletResponse) response)) {
			proceed(request, response, chain);
		}

	}

	private void proceed(ServletRequest request, ServletResponse response, FilterChain chain) {
		try {
			chain.doFilter(request, response);
		} catch (IOException | ServletException e) {

			LOGGER.debug(" servlet exception {}", e.getMessage());
			// e.printStackTrace();

		} finally {
			SocialUserUtil.delThreadLocal();
		}
	}

	private boolean isAuthenticaltedURL(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String uri = request.getRequestURI();
		String socialId = UserAuthUtil.getUserSocialID((HttpServletRequest) request);
		String[] unAuthURLs = new String[] { "/", "/login", "/twitter/auth/getToken", "/twitter/auth/callback",
				"/logout" };

		LOGGER.info("Request URL {} SID {}", request.getRequestURI(), socialId);

		boolean isUnauthenticatedURL = Arrays.asList(unAuthURLs).stream().anyMatch(u -> u.equals(uri));

		if (isUnauthenticatedURL && socialId != "" && !"/logout".equals(request.getRequestURI())) {
			response.sendRedirect("/home");
			LOGGER.warn("url redirection from{} to /home", request.getRequestURI());
			return false;
		} else if (!isUnauthenticatedURL) {
			try {
				if (socialId == "") {
					// if we get here, the user didn't authorize the app
					response.sendRedirect("/login");
					LOGGER.warn("url redirection from{} to /home", request.getRequestURI());
					return false;
				}
				loadUserToThreadLocal((HttpServletRequest) request, socialId);
			} catch (Exception e) {
				response.sendRedirect("/logout");
				LOGGER.error("invalid user information {}", e.getMessage());
				return false;
			}
		}
		return true;
	}

	private void loadUserToThreadLocal(HttpServletRequest request, String socialId) {
		SocialUser socialUser = (SocialUser) request.getSession().getAttribute("socialUser");
		LOGGER.debug("socialUser from session {}", socialUser);
		if (socialUser == null) {
			socialUser = userService.findBySocialId(Long.parseLong(socialId));
			request.getSession().setAttribute("socialUser", socialUser);
			LOGGER.debug("socialUser set in the session {}", socialUser);
		}

		// update user in the threadLocal
		SocialUserUtil.setLoggedInUser(socialUser);
	}

}

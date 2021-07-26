package com.social.users.service;


import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.SerializationUtils;

import com.social.exception.SIDNotFoundSecurityException;
import com.social.users.model.SocialUser;
import com.social.users.repository.SocialUsersRepository;

import twitter4j.Twitter;
import twitter4j.auth.AccessToken;

@Service
public class SocialUserService {
	
	@Autowired
	SocialUsersRepository usersRepository;
	private static final Logger LOGGER = LoggerFactory.getLogger(SocialUserService.class);
	
	public long addUser(long userId, AccessToken token, Twitter twitter) {
		SocialUser user = new SocialUser();
		try {
			user.setTUID(userId);
			user.setName(token.getScreenName());
			user.setAccessToken(token.getToken());			
			user.setAccessTokensecret(token.getTokenSecret());
			user.setTwitterObj(SerializationUtils.serialize(twitter));
			user = usersRepository.save(user);
			
		}catch (Exception e) {
			LOGGER.error("add user ", user);
			user = usersRepository.findByTUID(userId).get();
		}
		return user.getId();
	}
	
	/*not used
	 private Optional<SocialUser> findByTuserId(Long id) {
		 return usersRepository.findByTUID(id);
	}*/
	 public SocialUser findBySocialId(Long id) {
		 return usersRepository.findById(id)
				 .orElseThrow(() -> new SIDNotFoundSecurityException("SID not valid login again"));
	} 
	 public List<SocialUser> getUserListForScheduler(int sIndex, int size) {
			Pageable pageable = (Pageable) PageRequest.of(sIndex, size, Sort.by("lastSycAt").ascending());
			return usersRepository.findAll(pageable).getContent();
	 }

	 @Transactional
	 public void updateUpdatedTiime(SocialUser user) {
		 user.setLastSycAt(new Date());
		 usersRepository.save(user);
	}
}

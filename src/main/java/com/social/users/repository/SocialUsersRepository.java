package com.social.users.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.social.users.model.SocialUser;


public interface SocialUsersRepository extends JpaRepository<SocialUser, Integer>{
	Optional<SocialUser> findByTUID(long id);
	Optional<SocialUser> findById(long id);
}

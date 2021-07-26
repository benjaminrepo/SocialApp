package com.social.twitter.dataAccess.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.social.twitter.dataAccess.model.TUser;

public interface TUserRepository extends JpaRepository<TUser, Integer> {
	Page<TUser> findById(long TUID, Pageable pageable);
}

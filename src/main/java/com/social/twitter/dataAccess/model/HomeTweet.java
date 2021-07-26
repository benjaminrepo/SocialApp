package com.social.twitter.dataAccess.model;

import java.util.Date;
import java.util.Set;

import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

import org.hibernate.annotations.CreationTimestamp;

/**
 * 
 * Not yet completed
 *
 */

//@Entity
public class HomeTweet {

	@Id
    @OneToMany
    @JoinColumn(name = "twitter_user_id",referencedColumnName = "id")
	private Set<Tweet> statues;
	
    @CreationTimestamp
	private Date createdAt;

	public Set<Tweet> getStatues() {
		return statues;
	}

	public void setStatues(Set<Tweet> statues) {
		this.statues = statues;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

}

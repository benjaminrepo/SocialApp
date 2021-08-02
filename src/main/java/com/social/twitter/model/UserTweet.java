package com.social.twitter.model;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

/**
 * 
 * Not yet completed
 *
 */
@Entity
public class UserTweet {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long id;
	
	private long TUID;
	private long SID;
	
	@ManyToOne(cascade = CascadeType.ALL)
	@NotFound(action = NotFoundAction.IGNORE)
    @JoinColumn(name = "tweet_id", referencedColumnName = "id")
	private Tweet statues;
	
    @CreationTimestamp
	private Date createdAt;
    
	public Tweet getStatues() {
		return statues;
	}

	public void setStatues(Tweet statues) {
		this.statues = statues;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getSID() {
		return SID;
	}

	public void setSID(long sID) {
		SID = sID;
	}

	public long getTUID() {
		return TUID;
	}

	public void setTUID(long tUID) {
		TUID = tUID;
	}



}

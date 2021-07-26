package com.social.users.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.SequenceGenerator;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.util.SerializationUtils;

import twitter4j.Twitter;
enum Role {
    ADMIN, USER;
}
@Entity
public class SocialUser {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY, generator="my_entity_seq_gen")
	@SequenceGenerator(name="my_entity_seq_gen", sequenceName="MY_ENTITY_SEQ")
    private Long id;
    
	@Column(unique = true, nullable = false)
    private Long TUID;
    private String name;
    private String accessToken;
    private String accessTokensecret;
    @Lob
    @Column(name = "twitterObj", columnDefinition="BLOB")
    private byte[] twitterObj;
    @Enumerated(EnumType.STRING)
    private Role role;

	private Date lastSycAt;
	
    @CreationTimestamp
    private Date createdAt;

    @UpdateTimestamp
    private Date updatedAt;

    public SocialUser(){
    	
    }
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getTUID() {
		return TUID;
	}

	public void setTUID(Long tUID) {
		TUID = tUID;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public String getAccessTokensecret() {
		return accessTokensecret;
	}

	public void setAccessTokensecret(String accessTokensecret) {
		this.accessTokensecret = accessTokensecret;
	}

	public byte[] getTwitterObj() {
		return twitterObj;
	}

	public void setTwitterObj(byte[] twitterObj) {
		this.twitterObj = twitterObj;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public Date getLastSycAt() {
		return lastSycAt;
	}

	public void setLastSycAt(Date lastSycAt) {
		this.lastSycAt = lastSycAt;
	}
	
	public Twitter getTwitter() {
		return (Twitter) SerializationUtils.deserialize(this.twitterObj);
	}

	public Date getTstatus_createdAt() {
		return createdAt;
	}

	public void setTstatus_createdAt(Date tstatus_createdAt) {
		this.createdAt = tstatus_createdAt;
	}

	public Date getTstatus_updatedAt() {
		return updatedAt;
	}

	public void setTstatus_updatedAt(Date tstatus_updatedAt) {
		this.updatedAt = tstatus_updatedAt;
	}


}

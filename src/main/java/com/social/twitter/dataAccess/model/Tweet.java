package com.social.twitter.dataAccess.model;

import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;


@Entity
/*@Table(indexes = {
		  @Index(name = "userid_Text_Index", columnList = "id, text"),
		})*/
public class Tweet {
	@Id
    private long id;
    @Column(name = "text",length= 310)
    private String text;
	private Date updatedAt;
	private Date createdAt;    
    private int displayTextRangeStart;
    private int displayTextRangeEnd;
    private String source;
    private boolean isTruncated;
    private long inReplyToStatusId;
    private long inReplyToUserId;
    private boolean isFavorited;
    private boolean isRetweeted;
    private int favoriteCount;
    private String inReplyToScreenName;
    private long retweetCount;
    private boolean isPossiblySensitive;
    private String lang;
    
    private String geoLocation;
    private String place;
    private long currentUserRetweetId;
    
    @ManyToOne(cascade = {CascadeType.MERGE})
    //@NotFound(action = NotFoundAction.IGNORE)
    @JoinColumn(name = "user_id",referencedColumnName = "id")
    private TUser user;

    @OneToOne(cascade = {CascadeType.MERGE})
    //@NotFound(action = NotFoundAction.IGNORE)
    private Tweet retweetedStatus;
    
    @OneToMany(cascade = {CascadeType.ALL})
    private Set<TUser> userMentionEntities;

    @OneToOne(cascade = {CascadeType.MERGE})
    //@NotFound(action = NotFoundAction.IGNORE)
    private Tweet quotedStatus;    
    
    @OneToMany(cascade = {CascadeType.ALL})
    //@NotFound(action = NotFoundAction.IGNORE)
    private Set<TUser> contributorsIDs;
    
    
    @CreationTimestamp
    private Date tweet_created;

    @UpdateTimestamp
    private Date tweet_updated;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public Date getUpdatedAt() {
		return updatedAt;
	}
	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}
	public Date getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}
	public int getDisplayTextRangeStart() {
		return displayTextRangeStart;
	}
	public void setDisplayTextRangeStart(int displayTextRangeStart) {
		this.displayTextRangeStart = displayTextRangeStart;
	}
	public int getDisplayTextRangeEnd() {
		return displayTextRangeEnd;
	}
	public void setDisplayTextRangeEnd(int displayTextRangeEnd) {
		this.displayTextRangeEnd = displayTextRangeEnd;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public boolean isTruncated() {
		return isTruncated;
	}
	public void setTruncated(boolean isTruncated) {
		this.isTruncated = isTruncated;
	}
	public long getInReplyToStatusId() {
		return inReplyToStatusId;
	}
	public void setInReplyToStatusId(long inReplyToStatusId) {
		this.inReplyToStatusId = inReplyToStatusId;
	}
	public long getInReplyToUserId() {
		return inReplyToUserId;
	}
	public void setInReplyToUserId(long inReplyToUserId) {
		this.inReplyToUserId = inReplyToUserId;
	}
	public boolean isFavorited() {
		return isFavorited;
	}
	public void setFavorited(boolean isFavorited) {
		this.isFavorited = isFavorited;
	}
	public boolean isRetweeted() {
		return isRetweeted;
	}
	public void setRetweeted(boolean isRetweeted) {
		this.isRetweeted = isRetweeted;
	}
	public int getFavoriteCount() {
		return favoriteCount;
	}
	public void setFavoriteCount(int favoriteCount) {
		this.favoriteCount = favoriteCount;
	}
	public String getInReplyToScreenName() {
		return inReplyToScreenName;
	}
	public void setInReplyToScreenName(String inReplyToScreenName) {
		this.inReplyToScreenName = inReplyToScreenName;
	}
	public long getRetweetCount() {
		return retweetCount;
	}
	public void setRetweetCount(long retweetCount) {
		this.retweetCount = retweetCount;
	}
	public boolean isPossiblySensitive() {
		return isPossiblySensitive;
	}
	public void setPossiblySensitive(boolean isPossiblySensitive) {
		this.isPossiblySensitive = isPossiblySensitive;
	}
	public String getLang() {
		return lang;
	}
	public void setLang(String lang) {
		this.lang = lang;
	}
	public Set<TUser> getContributorsIDs() {
		return contributorsIDs;
	}
	public void setContributorsIDs(Set<TUser> contributorsIDs) {
		this.contributorsIDs = contributorsIDs;
	}
	public String getGeoLocation() {
		return geoLocation;
	}
	public void setGeoLocation(String geoLocation) {
		this.geoLocation = geoLocation;
	}
	public String getPlace() {
		return place;
	}
	public void setPlace(String place) {
		this.place = place;
	}
	public long getCurrentUserRetweetId() {
		return currentUserRetweetId;
	}
	public void setCurrentUserRetweetId(long currentUserRetweetId) {
		this.currentUserRetweetId = currentUserRetweetId;
	}
	public TUser getUser() {
		return user;
	}
	public void setUser(TUser user) {
		this.user = user;
	}
	public Tweet getRetweetedStatus() {
		return retweetedStatus;
	}
	public void setRetweetedStatus(Tweet retweetedStatus) {
		this.retweetedStatus = retweetedStatus;
	}
	public Set<TUser> getUserMentionEntities() {
		return userMentionEntities;
	}
	public void setUserMentionEntities(Set<TUser> userMentionEntities) {
		this.userMentionEntities = userMentionEntities;
	}
	public Tweet getQuotedStatus() {
		return quotedStatus;
	}
	public void setQuotedStatus(Tweet quotedStatus) {
		this.quotedStatus = quotedStatus;
	}
	public Date getTstatus_created() {
		return tweet_created;
	}
	public void setTstatus_created(Date tstatus_created) {
		this.tweet_created = tstatus_created;
	}
	public Date getTstatus_updated() {
		return tweet_updated;
	}
	public void setTstatus_updated(Date tstatus_updated) {
		this.tweet_updated = tstatus_updated;
	}
}

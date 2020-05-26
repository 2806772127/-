package com.fb.goldencudgel.auditDecisionSystem.domain.ratingConfig;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

@Entity
@Table(name = "COMPANY_RATING_CONFIG")
public class RatingConfig implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "CONFIG_ID")
	private String configId;
	
	@Column(name = "RATING")
	private String rating;
	
	@Column(name = "RATING_DESCRIBE")
	private String ratingDescribe;
	
	@Column(name = "CREATE_TIME")
	private Timestamp createTime;
	
	@Column(name = "UPDATE_TIME")
	private Timestamp updateTime;

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public String getConfigId() {
		return configId;
	}

	public void setConfigId(String configId) {
		this.configId = configId;
	}

	public String getRating() {
		return rating;
	}

	public void setRating(String rating) {
		this.rating = rating;
	}

	public String getRatingDescribe() {
		return ratingDescribe;
	}

	public void setRatingDescribe(String ratingDescribe) {
		this.ratingDescribe = ratingDescribe;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public Timestamp getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Timestamp updateTime) {
		this.updateTime = updateTime;
	}
}

package com.fb.goldencudgel.auditDecisionSystem.domain.incatorsConfig;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.sql.Timestamp;


@Entity
@Table(name = "COMPANY_INDICATORS_CONFIG")
public class IncatorsConfig implements Serializable {

  private static final long serialVersionUID = 1L;


  @Id
  @Column(name = "CONFIG_ID")
  private String configId;

  @Column(name = "POWER_TYPE")
  private String powerType;

  @Column(name = "INDUSTRY_TYPE")
  private String industryType;

  @Column(name = "CONPANY_YEAR_TYPE")
  private String conpanyYearType;

  @Column(name = "RATE")
  private Double rate;

  @Column(name = "CREATE_TIME")
  private Timestamp createTime;

  @Column(name = "UPDATE_TIME")
  private Timestamp updateTime;

	public String getConfigId() {
		return configId;
	}
	
	public void setConfigId(String configId) {
		this.configId = configId;
	}
	
	public String getPowerType() {
		return powerType;
	}
	
	public void setPowerType(String powerType) {
		this.powerType = powerType;
	}
	
	
	
	public String getIndustryType() {
		return industryType;
	}

	public void setIndustryType(String industryType) {
		this.industryType = industryType;
	}

	public String getConpanyYearType() {
		return conpanyYearType;
	}
	
	public void setConpanyYearType(String conpanyYearType) {
		this.conpanyYearType = conpanyYearType;
	}
	
	public Double getRate() {
		return rate;
	}
	
	public void setRate(Double rate) {
		this.rate = rate;
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
	
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	  
 
}

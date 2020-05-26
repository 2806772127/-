package com.fb.goldencudgel.auditDecisionSystem.domain.abilityCompareConfig;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.sql.Timestamp;


@Entity
@Table(name = "COMPANY_ABILITY_COMPARE_CONFIG")
public class AbilityCompareConfig implements Serializable {

  private static final long serialVersionUID = 1L;


  @Id
  @Column(name = "CONFIG_ID")
  private String configId;

  @Column(name = "TYPE")
  private String type;

  @Column(name = "START_RANGE")
  private Double startRange;

  @Column(name = "END_INCLUDE_FLAG")
  private String endIncludeFlag;

  @Column(name = "END_RANGE")
  private Double endRange;

  @Column(name = "START_INCLUDE_FLAG")
  private String startIncludeFlag;

  @Column(name = "ABILITY_TYPE")
  private String abilityType;

  @Column(name = "ABILITY_DESCRIBE")
  private String abilityDescribe;


  @Column(name = "CREATE_TIME")
  private Timestamp createtime;


  @Column(name = "UPDATE_TIME")
  private Timestamp updatetime;

  public String getEndIncludeFlag() {
    return endIncludeFlag;
  }

  public void setEndIncludeFlag(String endIncludeFlag) {
    this.endIncludeFlag = endIncludeFlag;
  }

  public static long getSerialVersionUID() {
    return serialVersionUID;
  }

  public String getConfigId() {
    return configId;
  }

  public void setConfigId(String configId) {
    this.configId = configId;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public Double getStartRange() {
    return startRange;
  }

  public void setStartRange(Double startRange) {
    this.startRange = startRange;
  }

  public String getStartIncludeFlag() {
    return startIncludeFlag;
  }

  public void setStartIncludeFlag(String startIncludeFlag) {
    this.startIncludeFlag = startIncludeFlag;
  }

  public Double getEndRange() {
    return endRange;
  }

  public void setEndRange(Double endRange) {
    this.endRange = endRange;
  }

  public String getAbilityType() {
    return abilityType;
  }

  public void setAbilityType(String abilityType) {
    this.abilityType = abilityType;
  }

  public String getAbilityDescribe() {
    return abilityDescribe;
  }

  public void setAbilityDescribe(String abilityDescribe) {
    this.abilityDescribe = abilityDescribe;
  }

  public Timestamp getCreatetime() {
    return createtime;
  }

  public void setCreatetime(Timestamp createtime) {
    this.createtime = createtime;
  }

  public Timestamp getUpdatetime() {
    return updatetime;
  }

  public void setUpdatetime(Timestamp updatetime) {
    this.updatetime = updatetime;
  }
}

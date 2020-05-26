package com.fb.goldencudgel.auditDecisionSystem.domain.visit;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 打卡历史类
 * @author  David
 * */
@Entity
@Table(name = "FB_PUNCH_CARD_RECODE")
public class FbPunchCardRecode  implements Serializable  {
  private static final long serialVersionUID = 1L;
  @Id
  @Column(name = "PUNCH_CARD_ID")
  private String punchCardId;

  @Column(name = "COMPILATION_NO")
  private String compilationNo;

  @Column(name = "TRAND_ID")
  private String trandId;

  @Column(name = "PUNCH_START_ADDRESS")
  private String punchStartAddress;

  @Column(name = "PUNCH_END_ADDRESS")
  private String punchEndAddress;

  @Column(name = "PUNCH_START_ADDRESS_LONGITUDE")
  private String startLongitude;

  @Column(name = "PUNCH_START_ADDRESS_LATITUDE")
  private String startLatitude;

  @Column(name = "PUNCH_END_ADDRESS_LONGITUDE")
  private String endLongitude;

  @Column(name = "PUNCH_END_ADDRESS_LATITUDE")
  private String endLatitude;

  @Column(name = "PUNCH_START_TIME")
  private java.sql.Timestamp punchStartTime;

  @Column(name = "PUNCH_END_TIME")
  private java.sql.Timestamp punchEndTime;

  @Column(name = "APPOINTMENT_DATE")
  private String appointmentDate;

  @Column(name = "CREATE_TIME")
  private java.sql.Timestamp createTime;

  @Column(name = "CREATE_USER")
  private String createUser;

  @Column(name = "UPDATE_TIME")
  private java.sql.Timestamp updateTime;

  @Column(name = "UPDATE_USER")
  private String updateUser;


  public String getPunchCardId() {
    return punchCardId;
  }

  public void setPunchCardId(String punchCardId) {
    this.punchCardId = punchCardId;
  }

  public String getCompilationNo() {
    return compilationNo;
  }

  public void setCompilationNo(String compilationNo) {
    this.compilationNo = compilationNo;
  }

  public String getTrandId() {
    return trandId;
  }

  public void setTrandId(String trandId) {
    this.trandId = trandId;
  }

  public String getPunchStartAddress() {
    return punchStartAddress;
  }

  public void setPunchStartAddress(String punchStartAddress) {
    this.punchStartAddress = punchStartAddress;
  }

  public String getPunchEndAddress() {
    return punchEndAddress;
  }

  public void setPunchEndAddress(String punchEndAddress) {
    this.punchEndAddress = punchEndAddress;
  }

  public String getStartLongitude() {
    return startLongitude;
  }

  public void setStartLongitude(String startLongitude) {
    this.startLongitude = startLongitude;
  }

  public String getStartLatitude() {
    return startLatitude;
  }

  public void setStartLatitude(String startLatitude) {
    this.startLatitude = startLatitude;
  }

  public String getEndLongitude() {
    return endLongitude;
  }

  public void setEndLongitude(String endLongitude) {
    this.endLongitude = endLongitude;
  }

  public String getEndLatitude() {
    return endLatitude;
  }

  public void setEndLatitude(String endLatitude) {
    this.endLatitude = endLatitude;
  }

  public Timestamp getPunchStartTime() {
    return punchStartTime;
  }

  public void setPunchStartTime(Timestamp punchStartTime) {
    this.punchStartTime = punchStartTime;
  }

  public Timestamp getPunchEndTime() {
    return punchEndTime;
  }

  public void setPunchEndTime(Timestamp punchEndTime) {
    this.punchEndTime = punchEndTime;
  }

  public String getAppointmentDate() {
    return appointmentDate;
  }

  public void setAppointmentDate(String appointmentDate) {
    this.appointmentDate = appointmentDate;
  }

  public Timestamp getCreateTime() {
    return createTime;
  }

  public void setCreateTime(Timestamp createTime) {
    this.createTime = createTime;
  }

  public String getCreateUser() {
    return createUser;
  }

  public void setCreateUser(String createUser) {
    this.createUser = createUser;
  }

  public Timestamp getUpdateTime() {
    return updateTime;
  }

  public void setUpdateTime(Timestamp updateTime) {
    this.updateTime = updateTime;
  }

  public String getUpdateUser() {
    return updateUser;
  }

  public void setUpdateUser(String updateUser) {
    this.updateUser = updateUser;
  }




}

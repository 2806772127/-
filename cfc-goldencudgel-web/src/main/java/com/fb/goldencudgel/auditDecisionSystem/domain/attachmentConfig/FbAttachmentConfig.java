package com.fb.goldencudgel.auditDecisionSystem.domain.attachmentConfig;


import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name="FB_ATTACHMENT_CONFIG")
@GenericGenerator(name = "jpa-uuid", strategy = "uuid")
public class FbAttachmentConfig {

  @Id
  @GeneratedValue(generator = "jpa-uuid")
  @Column(name = "ATTACH_TYPE_ID")
  private String attachTypeId;
  @Column(name = "NODE_CODE")
  private String nodeCode;
  @Column(name = "NODE_NAME")
  private String nodeName;
  @Column(name = "INDUSTRY_TYPE")
  private String industryType;
  @Column(name = "CHECK_ITEM")
  private String checkItem;
  @Column(name = "ATTACH_TYPE_CODE")
  private String attachTypeCode;
  @Column(name = "ATTACH_TYPE_NAME")
  private String attachTypeName;
  @Column(name = "IS_ENABLE")
  private String isEnable;
  @Column(name = "IS_REQUIRED")
  private String isRequired;
  @Column(name = "SHOW_ORDER")
  private double showOrder;
  @Column(name = "ATTACH_NAME_CODE")
  private String attactNameCode;
  @Column(name = "ATTACH_NAME")
  private String attactName;


  public String getAttachTypeId() {
    return attachTypeId;
  }

  public void setAttachTypeId(String attachTypeId) {
    this.attachTypeId = attachTypeId;
  }


  public String getNodeCode() {
    return nodeCode;
  }

  public void setNodeCode(String nodeCode) {
    this.nodeCode = nodeCode;
  }


  public String getNodeName() {
    return nodeName;
  }

  public void setNodeName(String nodeName) {
    this.nodeName = nodeName;
  }


  public String getIndustryType() {
    return industryType;
  }

  public void setIndustryType(String industryType) {
    this.industryType = industryType;
  }


  public String getCheckItem() {
    return checkItem;
  }

  public void setCheckItem(String checkItem) {
    this.checkItem = checkItem;
  }


  public String getAttachTypeCode() {
    return attachTypeCode;
  }

  public void setAttachTypeCode(String attachTypeCode) {
    this.attachTypeCode = attachTypeCode;
  }


  public String getAttachTypeName() {
    return attachTypeName;
  }

  public void setAttachTypeName(String attachTypeName) {
    this.attachTypeName = attachTypeName;
  }


  public String getIsEnable() {
    return isEnable;
  }

  public void setIsEnable(String isEnable) {
    this.isEnable = isEnable;
  }


  public String getIsRequired() {
    return isRequired;
  }

  public void setIsRequired(String isRequired) {
    this.isRequired = isRequired;
  }


  public double getShowOrder() {
    return showOrder;
  }

  public void setShowOrder(double showOrder) {
    this.showOrder = showOrder;
  }

  public String getAttactNameCode() {
    return attactNameCode;
  }

  public void setAttactNameCode(String attactNameCode) {
    this.attactNameCode = attactNameCode;
  }

  public String getAttactName() {
    return attactName;
  }

  public void setAttactName(String attactName) {
    this.attactName = attactName;
  }

}

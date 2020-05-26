package com.fb.goldencudgel.auditDecisionSystem.domain.caseAllotProc;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 *  案件分配歷程表
 */
@Entity
@Table(name="FB_CASE_ALLOT_PROC")
public class CaseAllotProc implements Serializable {

    private static final long serialVersionUID = 1L;

    //id
    @Id
    @Column(name = "ALLOT_ID")
    @GeneratedValue(generator = "idGenerator")
    @GenericGenerator(name = "idGenerator", strategy = "uuid")
    private String allotId;

    //案件資料編號
    @Column(name = "CASE_INFO_ID")
    private String caseInfoId;

    //授信戶統編
    @Column(name = "COMP_CODE")
    private String compCode;

    //公司名稱
    @Column(name = "COMP_NAME")
    private String compName;

    //經辦人員編
    @Column(name = "AGENT_USER_CODE")
    private String agentUserCode;

    //經辦人姓名
    @Column(name = "AGENT_USER_NAME")
    private String agentUserName;

    //分配時間
    @Column(name = "ALLOT_TIME")
    private Date allotTime;

    //分配用戶名
    @Column(name = "ALLOT_USER")
    private String allotUser;

    //分配類型 1:用戶管理員分配組長 2:組長分配業務員 3:回收 4:列為黑名單
    @Column(name = "ALLOT_TYPE")
    private String allotType;

    //排序
    @Column(name = "SHOW_ORDER")
    private String showOrder;

    public String getAllotId() {
        return allotId;
    }

    public void setAllotId(String allotId) {
        this.allotId = allotId;
    }

    public String getCaseInfoId() {
        return caseInfoId;
    }

    public void setCaseInfoId(String caseInfoId) {
        this.caseInfoId = caseInfoId;
    }

    public String getCompCode() {
        return compCode;
    }

    public void setCompCode(String compCode) {
        this.compCode = compCode;
    }

    public String getCompName() {
        return compName;
    }

    public void setCompName(String compName) {
        this.compName = compName;
    }

    public String getAgentUserCode() {
        return agentUserCode;
    }

    public void setAgentUserCode(String agentUserCode) {
        this.agentUserCode = agentUserCode;
    }

    public String getAgentUserName() {
        return agentUserName;
    }

    public void setAgentUserName(String agentUserName) {
        this.agentUserName = agentUserName;
    }

    public Date getAllotTime() {
        return allotTime;
    }

    public void setAllotTime(Date allotTime) {
        this.allotTime = allotTime;
    }

    public String getAllotUser() {
        return allotUser;
    }

    public void setAllotUser(String allotUser) {
        this.allotUser = allotUser;
    }

    public String getAllotType() {
        return allotType;
    }

    public void setAllotType(String allotType) {
        this.allotType = allotType;
    }

    public String getShowOrder() {
        return showOrder;
    }

    public void setShowOrder(String showOrder) {
        this.showOrder = showOrder;
    }
}

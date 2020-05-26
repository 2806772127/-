package com.fb.goldencudgel.auditDecisionSystem.domain.applyProc;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

/**
 * hu
 * 申請流程表
 */
@Entity
@Table(name = "FB_APPLY_PROC")
public class ApplyProc implements Serializable {

    private static final long serialVersionUID = 1L;

    //申請流程編號
    @Id
    @Column(name = "APPLY_PROC_NUM")
    @GeneratedValue(generator = "idGenerator")
    @GenericGenerator(name = "idGenerator", strategy = "uuid")
    private String appyProcNum;

    //授信戶統編
    @Column(name = "COMP_CODE")
    private String compCode;

    //公司名稱
    @Column(name = "COMP_NAME")
    private String compName;

    //申請類型 01:智能貸 02:Line Pay貸
    @Column(name = "APPLY_TYPE")
    private String applyType;

    //當前流程 01:拜訪筆記 02:聯征同意書 03:測字館 04:額度試算 05:進件申請 06:徵信實訪
    @Column(name = "PROC_NODE")
    private String procNode;

    //拜訪筆記狀態 01:未開始 02:進行中 03:已完成
    @Column(name = "VISIT_STATUS")
    private String vistStatus;

    //聯征同意書狀態 01:未開始 02:進行中 03:已完成
    @Column(name = "LETTER_STATUS")
    private String letterStatus;

    //測字館狀態 01:未開始 02:進行中 03:已完成
    @Column(name = "MEASURE_STATUS")
    private String measureStatus;

    //進件申請狀態 01:未開始 02:進行中 03:已完成
    @Column(name = "APPLY_STATUS")
    private String applyStatus;

    //徵信實訪狀態 01:未開始 02:進行中 03:已完成
    @Column(name = "CREDIT_STATUS")
    private String creditStatus;

    //流程狀態 01:未完成 02:完成
    @Column(name = "PROC_STATUS")
    private String procStatus;

    //申請產品編號
    @Column(name = "APPLY_PROD_CODE")
    private String applyProdCode;

    //申請產品名稱
    @Column(name = "APPLY_PROD_NAME")
    private String applyProdName;

    @Column(name = "CREATE_USER")
    private String createUser;

    @Column(name = "CREATE_USER_NAME")
    private String createUserName;

    @Column(name = "CREATE_TIME")
    private Date createTime;

    @Column(name = "UPDATE_USER")
    private String updateUser;

    @Column(name = "UPDATE_USER_NAME")
    private String updateUserName;

    @Column(name = "UPDATE_TIME")
    private Date updateTime;

    public String getAppyProcNum() {
        return appyProcNum;
    }

    public void setAppyProcNum(String appyProcNum) {
        this.appyProcNum = appyProcNum;
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

    public String getApplyType() {
        return applyType;
    }

    public void setApplyType(String applyType) {
        this.applyType = applyType;
    }

    public String getProcNodee() {
        return procNode;
    }

    public void setProcNode(String procNode) {
        this.procNode = procNode;
    }

    public String getVistStatus() {
        return vistStatus;
    }

    public void setVistStatus(String vistStatus) {
        this.vistStatus = vistStatus;
    }

    public String getLetterStatus() {
        return letterStatus;
    }

    public void setLetterStatus(String letterStatus) {
        this.letterStatus = letterStatus;
    }

    public String getMeasureStatus() {
        return measureStatus;
    }

    public void setMeasureStatus(String measureStatus) {
        this.measureStatus = measureStatus;
    }

    public String getApplyStatus() {
        return applyStatus;
    }

    public void setApplyStatus(String applyStatus) {
        this.applyStatus = applyStatus;
    }

    public String getCreditStatus() {
        return creditStatus;
    }

    public void setCreditStatus(String creditStatus) {
        this.creditStatus = creditStatus;
    }

    public String getProcStatus() {
        return procStatus;
    }

    public void setProcStatus(String procStatus) {
        this.procStatus = procStatus;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public String getCreateUserName() {
        return createUserName;
    }

    public void setCreateUserName(String createUserName) {
        this.createUserName = createUserName;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }

    public String getUpdateUserName() {
        return updateUserName;
    }

    public void setUpdateUserName(String updateUserName) {
        this.updateUserName = updateUserName;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getApplyProdCode() {
        return applyProdCode;
    }

    public void setApplyProdCode(String applyProdCode) {
        this.applyProdCode = applyProdCode;
    }

    public String getApplyProdName() {
        return applyProdName;
    }

    public void setApplyProdName(String applyProdName) {
        this.applyProdName = applyProdName;
    }
}

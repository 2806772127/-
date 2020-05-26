package com.fb.goldencudgel.auditDecisionSystem.domain.queryJcicRecored;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "QUERY_JCIC_RECORED")
public class QueryJcicRecored implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "ID", unique = true, nullable = false, length = 36)
    @GeneratedValue(generator = "idGenerator")
    @GenericGenerator(name = "idGenerator", strategy = "uuid")
    private String id;

    @Column(name = "OPERATION_ACCOUNT")
    private String account;

    @Column(name = "OPERATION_USER")
    private String userName;

    @Column(name = "OPERATION_USER_CODE")
    private String userCode;

    @Column(name = "OPERATION_USER_UNIT_CODE")
    private String userUnitCode;

    @Column(name = "COMPILATION_NO")
    private String compilationNo;

    @Column(name = "ZJX_ID")
    private String zjxId;

    @Column(name = "QITEM")
    private String qitem;

    @Column(name = "OPERATION_IP")
    private String ip;

    @Column(name = "OPERATION_TIME")
    private Date operationTime;

    @Column(name = "CREATE_TIME")
    private Date createTime;

    @Column(name = "CREATE_USER")
    private String createUser;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public String getUserUnitCode() {
        return userUnitCode;
    }

    public void setUserUnitCode(String userUnitCode) {
        this.userUnitCode = userUnitCode;
    }

    public String getCompilationNo() {
        return compilationNo;
    }

    public void setCompilationNo(String compilationNo) {
        this.compilationNo = compilationNo;
    }

    public String getZjxId() {
        return zjxId;
    }

    public void setZjxId(String zjxId) {
        this.zjxId = zjxId;
    }

    public String getQitem() {
        return qitem;
    }

    public void setQitem(String qitem) {
        this.qitem = qitem;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Date getOperationTime() {
        return operationTime;
    }

    public void setOperationTime(Date operationTime) {
        this.operationTime = operationTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }
}

package com.fb.goldencudgel.auditDecisionSystem.domain.fbZjxJcic;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "FB_ZJX_JCIC")
public class FbZjxJcic implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "JCIC_ID")
    private String jcicId;

    //展業金信評ID
    @Column(name = "ZJX_ID")
    private String zjxId;

    //类型： 1授信户 2负责人 3负责人配偶
    @Column(name = "ZJX_TYPE")
    private String zjxType;

    //客户ID
    @Column(name = "IDN_BAN")
    private String idnBan;

    //客户ID
    @Column(name = "CMPNAME")
    private String cmpName;

    //聯征數據的類型 B33 K33
    @Column(name = "QITEM")
    private String qItem;

    //html的路径
    @Column(name = "URL")
    private String url;

    @Column(name ="CREATE_TIME")
    private Date createTime;

    @Column(name ="CREATE_USER")
    private String createUser;

    @Transient
    private String showUrl;

    public String getJcicId() {
        return jcicId;
    }

    public void setJcicId(String jcicId) {
        this.jcicId = jcicId;
    }

    public String getZjxId() {
        return zjxId;
    }

    public void setZjxId(String zjxId) {
        this.zjxId = zjxId;
    }

    public String getZjxType() {
        return zjxType;
    }

    public void setZjxType(String zjxType) {
        this.zjxType = zjxType;
    }

    public String getIdnBan() {
        return idnBan;
    }

    public void setIdnBan(String idnBan) {
        this.idnBan = idnBan;
    }

    public String getCmpName() {
        return cmpName;
    }

    public void setCmpName(String cmpName) {
        this.cmpName = cmpName;
    }

    public String getqItem() {
        return qItem;
    }

    public void setqItem(String qItem) {
        this.qItem = qItem;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
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

    public String getShowUrl() {
        return showUrl;
    }

    public void setShowUrl(String showUrl) {
        this.showUrl = showUrl;
    }
}

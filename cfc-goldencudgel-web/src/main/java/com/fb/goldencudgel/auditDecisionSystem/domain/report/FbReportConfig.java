package com.fb.goldencudgel.auditDecisionSystem.domain.report;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * @author mazongjian
 * @createdDate 2019年3月24日 - 下午1:30:05
 */
@Entity
@Table(name = "FB_REPORT_CONFIG")
public class FbReportConfig {
    // 報表配置ID
    @Id
    @Column(name = "REPORT_ID", unique = true, nullable = false, length = 36)
    @GeneratedValue(generator = "idGenerator")
    @GenericGenerator(name = "idGenerator", strategy = "uuid")
    private String reportId;
    
    // 報表編碼
    @Column(name = "REPORT_CODE")
    private String reportCode;
    
    // 報表名稱
    @Column(name = "REPORT_NAME")
    private String reportName;
    
    // 報表路徑
    @Column(name = "REPORT_PATH")
    private String reportPath;
    
    // 報表類型;S-SPOTFIRE報表;E-EXCEL報表
    @Column(name = "REPORT_TYPE")
    private String reportType;
    
    // 日期查詢標籤
    @Column(name = "QUERY_DATE_LABEL")
    private String queryDateLabel;
    
    // 日期查詢格式
    @Column(name = "QUERY_DATE_FORMAT")
    private String queryDateFormat;
    
    // 是否需要區間
    @Column(name = "REPORT_QUERY_REGION")
    private String reportQueryRegion;
    
    // 是否需要下載
    @Column(name = "REPORT_DOWNLOAD")
    private String reportDownload;
    
    // 創建時間
    @Column(name = "CREATE_TIME")
    private Date createTime;
    
    // 創建人
    @Column(name = "CREATE_USER")
    private String createUser;
    
    // 更新時間
    @Column(name = "UPDATE_TIME")
    private Date updateTime;
    
    // 更新人
    @Column(name = "UPDATE_USER")
    private String updateUser;
    
    // 報表權限
    @Column(name = "REPORT_AUTHORITY")
    private String reportAuthority;

    public String getReportId() {
        return reportId;
    }

    public void setReportId(String reportId) {
        this.reportId = reportId;
    }
    
    public String getReportCode() {
        return reportCode;
    }

    public void setReportCode(String reportCode) {
        this.reportCode = reportCode;
    }

    public String getReportName() {
        return reportName;
    }

    public void setReportName(String reportName) {
        this.reportName = reportName;
    }

    public String getReportPath() {
        return reportPath;
    }

    public void setReportPath(String reportPath) {
        this.reportPath = reportPath;
    }

    public String getReportType() {
        return reportType;
    }

    public void setReportType(String reportType) {
        this.reportType = reportType;
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

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }

    public String getQueryDateLabel() {
        return queryDateLabel;
    }

    public void setQueryDateLabel(String queryDateLabel) {
        this.queryDateLabel = queryDateLabel;
    }

    public String getQueryDateFormat() {
        return queryDateFormat;
    }

    public void setQueryDateFormat(String queryDateFormat) {
        this.queryDateFormat = queryDateFormat;
    }

    public String getReportQueryRegion() {
        return reportQueryRegion;
    }

    public void setReportQueryRegion(String reportQueryRegion) {
        this.reportQueryRegion = reportQueryRegion;
    }

    public String getReportDownload() {
        return reportDownload;
    }

    public void setReportDownload(String reportDownload) {
        this.reportDownload = reportDownload;
    }

    public String getReportAuthority() {
        return reportAuthority;
    }

    public void setReportAuthority(String reportAuthority) {
        this.reportAuthority = reportAuthority;
    }
    
}

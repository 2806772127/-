package com.fb.goldencudgel.auditDecisionSystem.domain.systemData;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * @description: 系統資料實體類
 * @author: mazongjian
 * @date: 2019年8月5日
 */
@Entity
@Table(name = "FB_SYSTEM_DATA")
public class FbSystemData {
    // UUID
    @Id
    @Column(name = "ID", unique = true, nullable = false, length = 36)
    @GeneratedValue(generator = "idGenerator")
    @GenericGenerator(name = "idGenerator", strategy = "uuid")
    private String id;
    
    // 數據庫SCHEMA名稱
    @Column(name = "SCHEMA_NAME")
    private String schemaName;
    
    // 表名
    @Column(name = "TABLE_NAME")
    private String tableName;
    
    // 產制狀態;0-未產制,1-產制中,2-產制失敗,3-產制成功
    @Column(name = "GENERATE_STATUS")
    private String generateStatus;
    
    // 創建時間
    @Column(name = "CREATE_TIME")
    private Date createTime;
    
    // 更新人
    @Column(name = "CREATE_USER")
    private String createUser;
    
    // 更新時間
    @Column(name = "UPDATE_TIME")
    private Date updateTime;
    
    // 更新人
    @Column(name = "UPDATE_USER")
    private String updateUser;

    // 需要打碼的欄位，以|分隔
    @Column(name = "MASK_COLUMN")
    private String maskColumn;
    
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSchemaName() {
        return schemaName;
    }

    public void setSchemaName(String schemaName) {
        this.schemaName = schemaName;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
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

    public String getGenerateStatus() {
        return generateStatus;
    }

    public void setGenerateStatus(String generateStatus) {
        this.generateStatus = generateStatus;
    }

    public String getMaskColumn() {
        return maskColumn;
    }

    public void setMaskColumn(String maskColumn) {
        this.maskColumn = maskColumn;
    }
    
}

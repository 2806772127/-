package com.fb.goldencudgel.auditDecisionSystem.domain.commons;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 *
 * 系统配置实体类
 *
 * @date 2019-02-13 11:29:36,578
 * @author David Ma
 */
@Entity
@Table(name="SYSTEM_CONFIG")
public class SystemConfig implements Serializable {

  private static final long serialVersionUID = 1L;

  /**主键**/
  @Id
  @Column(name = "ID")
  private String id;
  /**键名称**/
  @Column(name = "KEYNAME")
  private String keyname;
  /**键值**/
  @Column(name = "KEYVALUE")
  private String keyvalue;
  /**配置注释**/
  @Column(name = "REMARK")
  private String remark;
  /**是否启用 0不启用 1启用**/
  @Column(name = "ENABLED")
  private String enabled;
  /**创建时间**/
  @Column(name = "CREATE_TIME")
  private java.sql.Timestamp createTime;


  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }


  public String getKeyname() {
    return keyname;
  }

  public void setKeyname(String keyname) {
    this.keyname = keyname;
  }


  public String getKeyvalue() {
    return keyvalue;
  }

  public void setKeyvalue(String keyvalue) {
    this.keyvalue = keyvalue;
  }


  public String getRemark() {
    return remark;
  }

  public void setRemark(String remark) {
    this.remark = remark;
  }


  public String getEnabled() {
    return enabled;
  }

  public void setEnabled(String enabled) {
    this.enabled = enabled;
  }


  public java.sql.Timestamp getCreateTime() {
    return createTime;
  }

  public void setCreateTime(java.sql.Timestamp createTime) {
    this.createTime = createTime;
  }

}

package com.fb.goldencudgel.auditDecisionSystem.domain.role;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.core.GrantedAuthority;
/**
 *  角色表
 * @Auther David
 */
@Entity
@Table(name = "SYS_ROLE")
public class SysRole implements GrantedAuthority, ConfigAttribute, Serializable, Comparable<SysRole>  {

  //测试
  public static final String CMLF_CRE_USRADMIN = "CMLF_CRE_USRADMIN";
  //徵信員
  public static final String CMLF_CRE_USR = "CMLF_CRE_USR";
  //區主管
  public static final String CMLF_DIS_USR = "CMLF_DIS_USR";
  //業務員
  public static final String CMLF_SAL_USR = "CMLF_SAL_USR";
  //处主管
  public static final String CMLF_SUP_USR = "CMLF_SUP_USR";
  //系统管理员
  public static final String CMLF_SYS_ADMIN = "CMLF_SYS_ADMIN";
  //组长
  public static final String CMLF_TEA_USR = "CMLF_TEA_USR";
  //用戶管理員
  public static final String CMLF_SYS_USRADMIN = "CMLF_SYS_USRADMIN";
  //派案管理員
  public static final String CMLF_CASE_ADMIN = "CMLF_CASE_ADMIN";

  private static final long serialVersionUID = 1L;
  /** 角色ID**/
  @Id
  @Column(name = "ROLE_ID")
  private String id;
  /**角色名称 **/
  @Column(name = "ROLE_NAME")
  private String name;
  /** 是否启用 1：啟用 0：不啟用**/
  @Column(name = "ROLE_ENABLE")
  private Integer status;
  /** 角色类型**/
  @Column(name = "ROLE_TYPE")
  private String type;
  /** 序号**/
  @Column(name = "ORDER_NUM")
  private String orderNum;

  @ManyToMany
  @LazyCollection(LazyCollectionOption.FALSE)
  @JoinTable(name = "SYS_ROLE_FUNC", joinColumns = @JoinColumn(name = "ROLE_ID",
          foreignKey = @ForeignKey(name = "FK_ROLEFUNC_ROLEID")), inverseJoinColumns = @JoinColumn(
          name = "FUNC_CODE", foreignKey = @ForeignKey(name = "FK_7M1XDQUX3F2FD5YKWEFNWPK9C")))
  @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
  private List<SysFunction> functions = new ArrayList<SysFunction>();

  public List<SysFunction> getFunctions() {
    return functions;
  }

  public void setFunctions(List<SysFunction> functions) {

    this.functions = functions;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getOrderNum() {
    return orderNum;
  }

  public void setOrderNum(String orderNum) {
    this.orderNum = orderNum;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((id == null) ? 0 : id.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    SysRole other = (SysRole) obj;
    if (id == null) {
      if (other.id != null)
        return false;
    } else if (!id.equals(other.id))
      return false;
    return true;
  }

  @Override
  public int compareTo(SysRole o) {
    Integer orderNum = Integer.valueOf(this.orderNum);
    Integer targetOrderNum = Integer.valueOf(o.getOrderNum());
    if (orderNum == null || targetOrderNum == null)
      return 0;
    if (orderNum > targetOrderNum)
      return 1;
    if (orderNum < targetOrderNum)
      return -1;
    return 0;
  }

  @Override
  public String toString() {
    return super.toString() + "[" + id + "]";
  }

  @Override
  public String getAttribute() {
    return null;
  }

  @Override
  public String getAuthority() {
    return null;
  }

    public String getRoleId(String id) {
        String roleId = "";
        if (id == null) {
            id = "";
        }
        if (id.contains("CMLF_TEA")) {
            roleId = CMLF_TEA_USR;
        } else if (id.contains("CMLF_CRE")) {
            roleId = CMLF_CRE_USR;
        } else if (id.contains("CMLF_SAL")) {
            roleId = CMLF_SAL_USR;
        } else if (id.contains("CMLF_DIS")) {
            roleId = CMLF_DIS_USR;
        } else if (id.contains("CMLF_SUP")) {
            roleId = CMLF_SUP_USR;
        } else if (id.contains("CMLF_SYS_USRADMIN")) {
          roleId = CMLF_SYS_USRADMIN;
        }  else if (id.contains("CMLF_CASE")) {
          roleId = CMLF_CASE_ADMIN;
        } else {
              roleId = CMLF_SYS_ADMIN;
        }
        return roleId;
    }
}

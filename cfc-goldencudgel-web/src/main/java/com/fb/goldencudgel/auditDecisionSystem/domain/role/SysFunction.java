package com.fb.goldencudgel.auditDecisionSystem.domain.role;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.*;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.springframework.security.access.ConfigAttribute;
/**
 * 权限表
 * @author David
 *
 * */
@Entity
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
@Table(name = "SYS_FUNCTION")
public class SysFunction implements Serializable{

  /** 功能编号*/
  @Id
  @Column(name = "FUNC_CODE")
  private String code;
  /** 權限類型 1：APP  2:WEB*/
  @Column(name = "AUTH_TYPE")
  private String authType;

  /** 是否隐藏*/
  @Column(name = "IS_ENABLED")
  private Integer enabled;

  /** 是否菜单*/
  @Column(name = "IS_MENU")
  private Integer menu;
  /**功能名称 */
  @Column(name = "FUNC_NAME")
  private String name;

  /**功能地址 */
  @Column(name = "FUNC_ADDRESS")
  private String url;

  /** 显示顺序 *. */
  @Column(name = "SORT_NO")
  private Integer sortNo;

  /** 父功能编号*/
  @ManyToOne
  @JoinColumn(name="PARENT_CODE",foreignKey = @ForeignKey(name = "FK_FUNCTION_FUNC_CODE"))
  private SysFunction parent;

  /** 依赖功能编号*/
  @ManyToOne
  @JoinColumn(name="DEPENDENCY_CODE",foreignKey = @ForeignKey(name = "FK_FUNCTION_DEPENDENCY"))
  private SysFunction dependency;

  /** 显示顺序*/
  @OneToMany(mappedBy = "parent",fetch = FetchType.EAGER)
  @OrderBy("SORT_NO")
  @Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
  private List<SysFunction> children = new ArrayList<>();

//  /** 角色权限. */
//  @ManyToMany
//  /* 及时加载 */
//  @LazyCollection(LazyCollectionOption.FALSE)
//  @JoinTable(name = "SYS_ROLE_FUNC", joinColumns = @JoinColumn(name = "FUNC_CODE",
//          foreignKey = @ForeignKey(name = "FK_7M1XDQUX3F2FD5YKWEFNWPK9C")),  inverseJoinColumns = @JoinColumn(
//          name = "ROLE_ID"), foreignKey = @ForeignKey(name = "FK_ROLEFUNC_ROLEID"))
//  @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
//  private List<SysRole> roleList = new ArrayList<>();
//

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public Integer getSortNo() {
    return sortNo;
  }

  public void setSortNo(Integer sortNo) {
    this.sortNo = sortNo;
  }

  public Integer isEnabled() {
    return enabled;
  }

  public void setEnabled(Integer enabled) {
    this.enabled = enabled;
  }

  public Integer isMenu() {
    return menu;
  }

  public void setMenu(Integer menu) {
    this.menu = menu;
  }

  public SysFunction getParent() {
    return parent;
  }

  public void setParent(SysFunction parent) {
    this.parent = parent;
  }

  public SysFunction getDependency() {
    return dependency;
  }

  public void setDependency(SysFunction dependency) {
    this.dependency = dependency;
  }

  public String getAuthType() { return authType; }

  public void setAuthType(String authType) { this.authType = authType; }

  public Integer getEnabled() { return enabled; }

  public Integer getMenu() { return menu; }

  public List<SysFunction> getChildren() {
    return children;
  }

  public void setChildren(List<SysFunction> children) {
    this.children = children;
    configAttributeList = null;
  }

//  public List<SysRole> getRoleList() {
//    return roleList;
//  }

  /** The config attribute list. */
  @Transient
  Collection<ConfigAttribute> configAttributeList = null;

  /**
   * Config attribute collection.
   *
   * @return the collection
   */
//  public Collection<ConfigAttribute> configAttributeCollection() {
//    if (getRoleList() != null
//            && (configAttributeList == null || ((roleList instanceof StatusList) && ((StatusList<SysRole>) roleList)
//            .isUpdate()))) {
//      configAttributeList = new ArrayList<>();
//      for (SysRole role : getRoleList()) {
//        configAttributeList.add((ConfigAttribute)role);
//      }
//    }
//    return configAttributeList;
//  }

//  public void setRoleList(List<SysRole> roleList) {
//    this.roleList = roleList;
//  }

  /**
   * The Class StatusList.
   *
   * @param <T> the generic type
   */
  public class StatusList<T> extends ArrayList<T> {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The updated. */
    boolean updated = false;

    public boolean isUpdate() {
      if (updated) {
        updated = false;
        return true;
      }
      return false;
    }

    public boolean add(T e) {
      updated = true;
      return super.add(e);
    }

    public void add(int index, T element) {
      super.add(index, element);
      updated = true;
    }

    public T remove(int index) {
      updated = true;
      return super.remove(index);
    }

    public boolean remove(Object o) {
      updated = true;
      return super.remove(o);
    }

    public boolean addAll(Collection<? extends T> c) {
      updated = true;
      return super.addAll(c);
    }

    public boolean addAll(int index, Collection<? extends T> c) {
      updated = true;
      return super.addAll(index, c);
    }

    protected void removeRange(int fromIndex, int toIndex) {
      updated = true;
      super.removeRange(fromIndex, toIndex);
      updated = true;
    }

    public boolean removeAll(Collection<?> c) {
      updated = true;
      return super.removeAll(c);
    }

    public void clear() {
      updated = true;
      super.clear();
    }
  }

}

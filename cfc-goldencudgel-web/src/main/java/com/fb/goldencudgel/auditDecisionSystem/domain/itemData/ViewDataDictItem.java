package com.fb.goldencudgel.auditDecisionSystem.domain.itemData;

import com.fb.goldencudgel.auditDecisionSystem.domain.ItemDBPK;

import javax.persistence.*;

@Entity
@Table(name = "VIEW_DATA_DICT_ITEM")
@IdClass(ItemDBPK.class)
public class ViewDataDictItem {

  @Id
  @Column(name = "DICT_ID")
  private String dictId;
  @Id
  @Column(name = "ITEM_CODE")
  private String itemCode;
  @Column(name = "ITEM_NAME")
  private String itemName;
  @Column(name = "ITEM_REMARK")
  private String itemRemark;
  @Column(name = "ITEM_EXT")
  private String itemExt;


  public String getDictId() {
    return dictId;
  }

  public void setDictId(String dictId) {
    this.dictId = dictId;
  }


  public String getItemCode() {
    return itemCode;
  }

  public void setItemCode(String itemCode) {
    this.itemCode = itemCode;
  }


  public String getItemName() {
    return itemName;
  }

  public void setItemName(String itemName) {
    this.itemName = itemName;
  }


  public String getItemRemark() {
    return itemRemark;
  }

  public void setItemRemark(String itemRemark) {
    this.itemRemark = itemRemark;
  }


  public String getItemExt() {
    return itemExt;
  }

  public void setItemExt(String itemExt) {
    this.itemExt = itemExt;
  }

}

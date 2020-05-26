package com.fb.goldencudgel.auditDecisionSystem.domain.itemData;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "VIEW_DATA_DICT")
public class ViewDataDict {

  @Id
  @Column(name = "DICT_ID")
  private String dictId;
  @Column(name ="DICT_NAME")
  private String dictName;
  @Column(name = "CRUDFLAG")
  private String crudflag;


  public String getDictId() {
    return dictId;
  }

  public void setDictId(String dictId) {
    this.dictId = dictId;
  }


  public String getDictName() {
    return dictName;
  }

  public void setDictName(String dictName) {
    this.dictName = dictName;
  }


  public String getCrudflag() {
    return crudflag;
  }

  public void setCrudflag(String crudflag) {
    this.crudflag = crudflag;
  }

}

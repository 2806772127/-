package com.fb.goldencudgel.auditDecisionSystem.domain;

import javax.persistence.*;
import java.io.Serializable;

/**
 *  数据字典
 * @Auther hu
 */
@Entity
@Table(name = "DATA_DICT_ITEM")
@IdClass(DataDictItemPK.class)
public class DataDictItem implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "ITEM_ID")
    private String dictId;//

    @Id
    @Column(name = "ITEM_CODE")
    private String dictItemId;//

    @Column(name = "ITEM_NAME")
    private String dictItemName;//

    @Column(name="ITEM_REMAKE")
    private String itemRemark;

    public String getItemRemark() {
        return itemRemark;
    }

    public void setItemRemark(String itemRemark) {
        this.itemRemark = itemRemark;
    }

    public String getDictItemName() {
        return dictItemName;
    }

    public void setDictItemName(String dictItemName) {
        this.dictItemName = dictItemName;
    }

    public String getDictId() {
        return dictId;
    }

    public void setDictId(String dictId) {
        this.dictId = dictId;
    }

    public String getDictItemId() {
        return dictItemId;
    }

    public void setDictItemId(String dictItemId) {
        this.dictItemId = dictItemId;
    }
}

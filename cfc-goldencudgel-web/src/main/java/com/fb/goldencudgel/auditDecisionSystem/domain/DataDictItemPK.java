package com.fb.goldencudgel.auditDecisionSystem.domain;

import java.io.Serializable;

public class DataDictItemPK implements Serializable {
    private static final long serialVersionUID = 1L;
    private String dictId;
    private String dictItemId;

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

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((dictId == null) ? 0 : dictId.hashCode());
        result = prime * result + ((dictItemId == null) ? 0 : dictItemId.hashCode());
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
        DataDictItemPK other = (DataDictItemPK) obj;
        if (dictId == null) {
            if (other.dictId != null)
                return false;
        } else if (!dictId.equals(other.dictId))
            return false;
        if (dictItemId == null) {
            if (other.dictItemId != null)
                return false;
        } else if (!dictItemId.equals(other.dictItemId))
            return false;
        return true;
    }

}

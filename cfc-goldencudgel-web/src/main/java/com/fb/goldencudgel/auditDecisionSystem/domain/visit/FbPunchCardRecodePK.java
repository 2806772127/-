package com.fb.goldencudgel.auditDecisionSystem.domain.visit;

import java.io.Serializable;

public class FbPunchCardRecodePK implements Serializable {

    private static final long serialVersionUID = 1L;
    private String punchCardId;
    private java.sql.Timestamp createTime;
    public String getTrandId() {
        return punchCardId;
    }
    public void setTrandId(String trandId) {
        this.punchCardId = trandId;
    }
    public java.sql.Timestamp getCreateTime() {
        return createTime;
    }
    public void setCreateTime(java.sql.Timestamp punchTime) {
        this.createTime = punchTime;
    }


    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((punchCardId == null) ? 0 : punchCardId.hashCode());
        result = prime * result + ((createTime == null) ? 0 : createTime.hashCode());
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
        FbPunchCardRecodePK other = (FbPunchCardRecodePK) obj;

        if (punchCardId == null) {
            if (other.punchCardId != null)
                return false;
        } else if (!punchCardId.equals(other.punchCardId))
            return false;
        if (createTime == null) {
            if (other.createTime != null)
                return false;
        } else if (!createTime.equals(other.createTime))
            return false;
        return true;
    }
}

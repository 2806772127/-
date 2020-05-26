package com.fb.goldencudgel.auditDecisionSystem.domain.visit;

import java.io.Serializable;

public class VisitCommonPk implements Serializable {
    private static final long serialVersionUID = 1L;
    private String uuid;
    private String seqNo;
    public String getUuid() {return uuid; }
    public void setUuid(String uuid) { this.uuid = uuid; }
    public String getSeqNo() { return seqNo; }
    public void setSeqNo(String seqNo) { this.seqNo = seqNo; }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((uuid == null) ? 0 : uuid.hashCode());
        result = prime * result + ((seqNo == null) ? 0 : seqNo.hashCode());
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
        VisitCommonPk other = (VisitCommonPk) obj;
        if (uuid == null) {
            if (other.uuid != null)
                return false;
        } else if (!uuid.equals(other.uuid))
            return false;
        if (seqNo == null) {
            if (other.seqNo != null)
                return false;
        } else if (!seqNo.equals(other.seqNo))
            return false;
        return true;
    }
}

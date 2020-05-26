package com.fb.goldencudgel.auditDecisionSystem.domain.role;

import java.io.Serializable;

public class SysFunctionPK implements Serializable {
    private static final long serialVersionUID = 1L;
    private String code;
    private String authType;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getAuthType() {
        return authType;
    }

    public void setAuthType(String authType) {
        this.authType = authType;
    }
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((code == null) ? 0 : code.hashCode());
        result = prime * result + ((authType == null) ? 0 : authType.hashCode());
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
        SysFunctionPK other = (SysFunctionPK) obj;
        if (code == null) {
            if (other.code != null)
                return false;
        } else if (!code.equals(other.code))
            return false;
        if (authType == null) {
            if (other.authType != null)
                return false;
        } else if (!authType.equals(other.authType))
            return false;
        return true;
    }


}

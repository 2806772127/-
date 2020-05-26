package com.fb.goldencudgel.auditDecisionSystem.domain.role;



import java.io.Serializable;

public class SysRoleFuncPK implements Serializable {
    private static final long serialVersionUID = 1L;
    private String roleId;
    private String funcCode;
    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getFuncCode() {
        return funcCode;
    }

    public void setFuncCode(String funcCode) {
        this.funcCode = funcCode;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((roleId == null) ? 0 : roleId.hashCode());
        result = prime * result + ((funcCode == null) ? 0 : funcCode.hashCode());
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
        SysRoleFuncPK other = (SysRoleFuncPK) obj;
        if (roleId == null) {
            if (other.roleId != null)
                return false;
        } else if (!roleId.equals(other.roleId))
            return false;
        if (funcCode == null) {
            if (other.funcCode != null)
                return false;
        } else if (!funcCode.equals(other.funcCode))
            return false;
        return true;
    }


}

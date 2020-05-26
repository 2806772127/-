package com.fb.goldencudgel.auditDecisionSystem.domain.user;

import java.io.Serializable;
import java.util.Objects;

public class UserListPK  implements Serializable {

    private static final long serialVersionUID = 1L;

    private String userCode;
    private String userGroup;
    private String userArea;

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public String getUserGroup() {
        return userGroup;
    }

    public void setUserGroup(String userGroup) {
        this.userGroup = userGroup;
    }

    public String getUserArea() {
        return userArea;
    }

    public void setUserArea(String userArea) {
        this.userArea = userArea;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserListPK that = (UserListPK) o;
        return Objects.equals(userCode, that.userCode) &&
                Objects.equals(userGroup, that.userGroup) &&
                Objects.equals(userArea, that.userArea);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userCode, userGroup, userArea);
    }
}

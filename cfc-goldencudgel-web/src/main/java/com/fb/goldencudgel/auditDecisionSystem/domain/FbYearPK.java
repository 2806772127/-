package com.fb.goldencudgel.auditDecisionSystem.domain;

import java.io.Serializable;
import java.util.Objects;

public class FbYearPK implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String userCustomerId;
	private String userCode;
	private String rateDate;

	public String getUserCustomerId() {
		return userCustomerId;
	}

	public void setUserCustomerId(String userCustomerId) {
		this.userCustomerId = userCustomerId;
	}

	public String getUserCode() {
		return userCode;
	}

	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}

	public String getRateDate() {
		return rateDate;
	}

	public void setRateDate(String rateDate) {
		this.rateDate = rateDate;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		FbYearPK fbYearPK = (FbYearPK) o;
		return Objects.equals(userCustomerId, fbYearPK.userCustomerId) &&
				Objects.equals(userCode, fbYearPK.userCode) &&
				Objects.equals(rateDate, fbYearPK.rateDate);
	}

	@Override
	public int hashCode() {
		return Objects.hash(userCustomerId, userCode, rateDate);
	}
}

package com.fb.goldencudgel.auditDecisionSystem.domain.letterConsent;

import java.io.Serializable;

public class FbLetterConsentDetailPK implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String compilationNo;
	private String letterType;
	public String getCompilationNo() {
		return compilationNo;
	}
	public void setCompilationNo(String compilationNo) {
		this.compilationNo = compilationNo;
	}
	public String getLetterType() {
		return letterType;
	}
	public void setLetterType(String letterType) {
		this.letterType = letterType;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((compilationNo == null) ? 0 : compilationNo.hashCode());
		result = prime * result + ((letterType == null) ? 0 : letterType.hashCode());
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
		FbLetterConsentDetailPK other = (FbLetterConsentDetailPK) obj;
		if (compilationNo == null) {
			if (other.compilationNo != null)
				return false;
		} else if (!compilationNo.equals(other.compilationNo))
			return false;
		if (letterType == null) {
			if (other.letterType != null)
				return false;
		} else if (!letterType.equals(other.letterType))
			return false;
		return true;
	}
	
	
}

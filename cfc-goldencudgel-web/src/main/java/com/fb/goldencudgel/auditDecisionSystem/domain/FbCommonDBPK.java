package com.fb.goldencudgel.auditDecisionSystem.domain;

import java.io.Serializable;

public class FbCommonDBPK implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String compilationNo;
	private String trandId;
	public String getCompilationNo() {
		return compilationNo;
	}
	public void setCompilationNo(String compilationNo) {
		this.compilationNo = compilationNo;
	}
	public String getTrandId() {
		return trandId;
	}
	public void setTrandId(String trandId) {
		this.trandId = trandId;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((compilationNo == null) ? 0 : compilationNo.hashCode());
		result = prime * result + ((trandId == null) ? 0 : trandId.hashCode());
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
		FbCommonDBPK other = (FbCommonDBPK) obj;
		if (compilationNo == null) {
			if (other.compilationNo != null)
				return false;
		} else if (!compilationNo.equals(other.compilationNo))
			return false;
		if (trandId == null) {
			if (other.trandId != null)
				return false;
		} else if (!trandId.equals(other.trandId))
			return false;
		return true;
	}
	
	
}

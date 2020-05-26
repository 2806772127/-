package com.fb.goldencudgel.auditDecisionSystem.domain;

import java.io.Serializable;

public class ItemDBPK implements Serializable{

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private String dictId;
	private String itemCode;
	public String getdictId() {
		return dictId;
	}
	public void setdictId(String dictId) {
		this.dictId = dictId;
	}
	public String getitemCode() {
		return itemCode;
	}
	public void setitemCode(String itemCode) {
		this.itemCode = itemCode;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((dictId == null) ? 0 : dictId.hashCode());
		result = prime * result + ((itemCode == null) ? 0 : itemCode.hashCode());
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
		ItemDBPK other = (ItemDBPK) obj;
		if (dictId == null) {
			if (other.dictId != null)
				return false;
		} else if (!dictId.equals(other.dictId))
			return false;
		if (itemCode == null) {
			if (other.itemCode != null)
				return false;
		} else if (!itemCode.equals(other.itemCode))
			return false;
		return true;
	}


}

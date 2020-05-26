package com.fb.goldencudgel.auditDecisionSystem.domain.addreData;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "VIEW_STREET")
public class ViewStreet implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "STREET_CODE")
    private String streetCode;

    @Column(name = "STREET_NAME")
    private String streetName;

    @Column(name = "CITY_CODE")
    private String districtCode;

    public String getStreetCode() {
        return streetCode;
    }

    public void setStreetCode(String streetCode) {
        this.streetCode = streetCode;
    }

    public String getStreetName() {
        return streetName;
    }

    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }

    public String getDistrictCode() {
        return districtCode;
    }

    public void setDistrictCode(String districtCode) {
        this.districtCode = districtCode;
    }
}

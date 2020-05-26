package com.fb.goldencudgel.auditDecisionSystem.domain.addreData;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "VIEW_DISTRICT")
public class ViewDistrict implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "DISTRICT_CODE")
    private String districtCode;

    @Column(name = "DISTRICT_NAME")
    private String districtName;

    @Column(name = "CITY_CODE")
    private String cityCode;

    public String getDistrictCode() {
        return districtCode;
    }

    public void setDistrictCode(String districtCode) {
        this.districtCode = districtCode;
    }

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }
}

package com.fb.goldencudgel.auditDecisionSystem.domain.addreData;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "VIEW_CITY")
public class ViewCity implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "CITY_CODE")
    private String cityCode;

    @Column(name = "CITY_NAME")
    private String cityName;

    @Column(name = "PROVINCE_CODE")
    private String prpvinceCode;

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getPrpvinceCode() {
        return prpvinceCode;
    }

    public void setPrpvinceCode(String prpvinceCode) {
        this.prpvinceCode = prpvinceCode;
    }
}

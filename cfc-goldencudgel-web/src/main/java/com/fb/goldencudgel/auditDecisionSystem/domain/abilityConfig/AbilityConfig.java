package com.fb.goldencudgel.auditDecisionSystem.domain.abilityConfig;


import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

@Entity
@Table(name="COMPANY_ABILITY_CONFIG")
public class AbilityConfig implements Serializable {

    @Id
    @Column(name = "CONFIG_ID")
    private String configId;

    @Column(name = "TYPE")
    private String type;

    @Column(name = "FINANCIAL_ABILITY")
    private String financialAbility;

    @Column(name = "DEBIT_ABILITY")
    private String debitAbility;

    @Column(name = "BUSINESS_ABILITY")
    private String businessAbility;

    @Column(name = "CONPANY_YEAR_TYPE")
    private String conpanyYearType;

    @Column(name = "ABILITY_DESCRIBE")
    private String abilityDescribe;

    @Column(name = "CREATE_TIME")
    private Timestamp createTime;

    @Column(name = "UPDATE_TIME")
    private Timestamp updateTime;

    public String getConfigId() {
        return configId;
    }

    public void setConfigId(String configId) {
        this.configId = configId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFinancialAbility() {
        return financialAbility;
    }

    public void setFinancialAbility(String financialAbility) {
        this.financialAbility = financialAbility;
    }

    public String getDebitAbility() {
        return debitAbility;
    }

    public void setDebitAbility(String debitAbility) {
        this.debitAbility = debitAbility;
    }

    public String getBusinessAbility() {
        return businessAbility;
    }

    public void setBusinessAbility(String businessAbility) {
        this.businessAbility = businessAbility;
    }

    public String getConpanyYearType() {
        return conpanyYearType;
    }

    public void setConpanyYearType(String conpanyYearType) {
        this.conpanyYearType = conpanyYearType;
    }

    public String getAbilityDescribe() {
        return abilityDescribe;
    }

    public void setAbilityDescribe(String abilityDescribe) {
        this.abilityDescribe = abilityDescribe;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public Timestamp getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Timestamp updateTime) {
        this.updateTime = updateTime;
    }
}

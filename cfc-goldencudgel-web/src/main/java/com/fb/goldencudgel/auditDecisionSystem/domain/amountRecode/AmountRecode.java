package com.fb.goldencudgel.auditDecisionSystem.domain.amountRecode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * @ClassName AmountRecode
 * @Author panha
 * @Date 2019/5/14 10:41
 * @Version 1.0
 **/
@Entity
@Table(name = "AMOUNT_RECORD")
public class AmountRecode implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "ID")
    private String id;

    @Column(name = "MEASURD_ID")
    private String measurdId;

    @Column(name = "PROD_NAME")
    private String prodName;

    @Column(name = "C_GRE_PAYMENT_AMT")
    private BigDecimal cGrePaymentAmt;

    @Column(name = "C_GRE_PAYMENT_AMT_MAX")
    private BigDecimal cGrePaymentAmtMax;

    @Column(name = "C_REPAYMENT_AMT")
    private BigDecimal cRepaymentAmt;

    @Column(name = "C_REPAYMENT_AMT_MAX")
    private BigDecimal cRepaymentAmtMax;

    @Column(name = "CG_CYCLE_AMT")
    private BigDecimal cgCycletAmt;

    @Column(name = "CG_CYCLE_AMT_MAX")
    private BigDecimal cgCycletAmtMax;

    @Column(name = "C_CYCLE_AMT")
    private BigDecimal cCycletAmt;

    @Column(name = "C_CYCLE_AMT_MAX")
    private BigDecimal cCycletAmtMax;

    @Column(name = "CREATE_TIME")
    private java.sql.Timestamp createTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getMeasurdId() {
        return measurdId;
    }

    public void setMeasurdId(String measurdId) {
        this.measurdId = measurdId;
    }

    public String getProdName() {
        return prodName;
    }

    public void setProdName(String prodName) {
        this.prodName = prodName;
    }

    public BigDecimal getcGrePaymentAmt() {
        return cGrePaymentAmt;
    }

    public void setcGrePaymentAmt(BigDecimal cGrePaymentAmt) {
        this.cGrePaymentAmt = cGrePaymentAmt;
    }

    public BigDecimal getcGrePaymentAmtMax() {
        return cGrePaymentAmtMax;
    }

    public void setcGrePaymentAmtMax(BigDecimal cGrePaymentAmtMax) {
        this.cGrePaymentAmtMax = cGrePaymentAmtMax;
    }

    public BigDecimal getcRepaymentAmt() {
        return cRepaymentAmt;
    }

    public void setcRepaymentAmt(BigDecimal cRepaymentAmt) {
        this.cRepaymentAmt = cRepaymentAmt;
    }

    public BigDecimal getcRepaymentAmtMax() {
        return cRepaymentAmtMax;
    }

    public void setcRepaymentAmtMax(BigDecimal cRepaymentAmtMax) {
        this.cRepaymentAmtMax = cRepaymentAmtMax;
    }

    public BigDecimal getCgCycletAmt() {
        return cgCycletAmt;
    }

    public void setCgCycletAmt(BigDecimal cgCycletAmt) {
        this.cgCycletAmt = cgCycletAmt;
    }

    public BigDecimal getCgCycletAmtMax() {
        return cgCycletAmtMax;
    }

    public void setCgCycletAmtMax(BigDecimal cgCycletAmtMax) {
        this.cgCycletAmtMax = cgCycletAmtMax;
    }

    public BigDecimal getcCycletAmt() {
        return cCycletAmt;
    }

    public void setcCycletAmt(BigDecimal cCycletAmt) {
        this.cCycletAmt = cCycletAmt;
    }

    public BigDecimal getcCycletAmtMax() {
        return cCycletAmtMax;
    }

    public void setcCycletAmtMax(BigDecimal cCycletAmtMax) {
        this.cCycletAmtMax = cCycletAmtMax;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }
}

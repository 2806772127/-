package com.fb.goldencudgel.auditDecisionSystem.service.impl;


import com.comwave.core.data.domain.QueryPage;
import com.comwave.core.jpa.BaseJpaDAO;
import com.fb.goldencudgel.auditDecisionSystem.domain.abilityConfig.AbilityConfig;
import com.fb.goldencudgel.auditDecisionSystem.repository.AbilityConfigRepository;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional
public class AbilityConfigServiceImpl extends BaseJpaDAO {

    private Logger logger = LoggerFactory.getLogger(AbilityConfigServiceImpl.class);

    @Autowired
    private AbilityConfigRepository abilityConfigRepository;

    public QueryPage<Object[]> queryAbilityConfig(String type, String financial, String debit, String business, String conpanyYear, String startDate, String endDate,String startDates, String endDates, QueryPage<Object[]> queryPage){

        Map<String,Object> params =  new HashMap<String,Object>();
        StringBuffer sql = new StringBuffer(" SELECT CONFIG_ID ");
        sql.append(" ,(CASE WHEN TYPE = '01' THEN '經營能力' WHEN TYPE = '02' THEN '償債能力' WHEN TYPE = '03' THEN '財務能力' END) TYPE_NAME ");
        sql.append(" ,(CASE WHEN FINANCIAL_ABILITY = '01' THEN '優於同業' WHEN FINANCIAL_ABILITY = '02' THEN '與同業相當' WHEN FINANCIAL_ABILITY = '03' THEN '劣於同業' END) FINANCIAL_ABILITY_NAME ");
        sql.append(" ,(CASE WHEN DEBIT_ABILITY = '01' THEN '優於同業' WHEN DEBIT_ABILITY = '02' THEN '與同業相當' WHEN DEBIT_ABILITY = '03' THEN '劣於同業' END) DEBIT_ABILITY_NAME ");
        sql.append(" ,(CASE WHEN BUSINESS_ABILITY = '01' THEN '優於同業' WHEN BUSINESS_ABILITY = '02' THEN '與同業相當' WHEN BUSINESS_ABILITY = '03' THEN '劣於同業' END) BUSINESS_ABILITY_NAME ");
        sql.append(" ,(CASE WHEN CONPANY_YEAR_TYPE = '01' THEN '1-5年' WHEN CONPANY_YEAR_TYPE = '02' THEN '6-10年' WHEN CONPANY_YEAR_TYPE = '03' THEN '10年以上' END) CONPANY_YEAR_TYPE_NAME ");
        sql.append(" ,ABILITY_DESCRIBE,DATE_FORMAT(CREATE_TIME,'%Y-%m-%d') CREATE_TIME,DATE_FORMAT(UPDATE_TIME,'%Y-%m-%d') UPDATE_TIME ");
        sql.append(" FROM COMPANY_ABILITY_CONFIG  ");
        sql.append(" WHERE 1=1 ");
        if (StringUtils.isNoneBlank(type)) {
            sql.append(" AND TYPE = :type");
            params.put("type", type);
        }
        if (StringUtils.isNoneBlank(financial)) {
            sql.append(" AND FINANCIAL_ABILITY = :financial");
            params.put("financial", financial);
        }
        if (StringUtils.isNoneBlank(debit)) {
            sql.append(" AND DEBIT_ABILITY = :debit");
            params.put("debit", debit);
        }
        if (StringUtils.isNoneBlank(business)) {
            sql.append(" AND BUSINESS_ABILITY = :business");
            params.put("business", business);
        }
        if (StringUtils.isNoneBlank(conpanyYear)) {
            sql.append(" AND CONPANY_YEAR_TYPE = :conpanyYear");
            params.put("conpanyYear", conpanyYear);
        }
        //創建時間
        if(StringUtils.isNoneBlank(startDate)){
            startDate =startDate.replace("/","-");
            sql.append(" AND Date(CREATE_TIME) >= :startDate");
            params.put("startDate",startDate);
        }
        if(StringUtils.isNoneBlank(endDate)){
            endDate = endDate.replace("/","-");
            sql.append(" AND Date(CREATE_TIME) <= :endDate");
            params.put("endDate",endDate);
        }
        //更新時間
        if(StringUtils.isNoneBlank(startDates)){
            startDates =startDates.replace("/","-");
            sql.append(" AND Date(UPDATE_TIME) >= :startDates");
            params.put("startDates",startDates);
        }
        if(StringUtils.isNoneBlank(endDates)){
            endDates = endDates.replace("/","-");
            sql.append(" AND Date(UPDATE_TIME) <= :endDates");
            params.put("endDates",endDates);
        }

        sql.append(" ORDER BY TYPE,BUSINESS_ABILITY,DEBIT_ABILITY,FINANCIAL_ABILITY,CONPANY_YEAR_TYPE ");
        // System.out.println(sql.toString());

        return findBySQL(sql,queryPage,params);
    }


    public QueryPage<Object[]> lookAbilityConfig(String configId){

        Map<String,Object> params =  new HashMap<String,Object>();
        StringBuffer sql = new StringBuffer("SELECT CONFIG_ID, (CASE WHEN TYPE = '01' THEN '經營能力' WHEN TYPE = '02' THEN '償債能力' WHEN TYPE = '03' THEN '財務能力' END) TYPE, (CASE WHEN FINANCIAL_ABILITY = '01' THEN '優於同業' WHEN FINANCIAL_ABILITY = '02' THEN '與同業相當' WHEN FINANCIAL_ABILITY = '03' THEN '劣於同業' END) FINANCIAL_ABILITY, (CASE WHEN DEBIT_ABILITY = '01' THEN '優於同業' WHEN DEBIT_ABILITY = '02' THEN '與同業相當' WHEN DEBIT_ABILITY = '03' THEN '劣於同業' END) DEBIT_ABILITY, (CASE WHEN BUSINESS_ABILITY = '01' THEN '優於同業' WHEN BUSINESS_ABILITY = '02' THEN '與同業相當' WHEN BUSINESS_ABILITY = '03' THEN '劣於同業' END) BUSINESS_ABILITY, (CASE WHEN CONPANY_YEAR_TYPE = '01' THEN '1-5年' WHEN CONPANY_YEAR_TYPE = '02' THEN '6-10年' WHEN CONPANY_YEAR_TYPE = '03' THEN '10年以上' END) CONPANY_YEAR_TYPE, ABILITY_DESCRIBE,DATE_FORMAT(CREATE_TIME,'%Y-%m-%d') CREATE_TIME,DATE_FORMAT(UPDATE_TIME,'%Y-%m-%d') UPDATE_TIME FROM COMPANY_ABILITY_CONFIG ");
        sql.append(" WHERE CONFIG_ID = :configId");
        params.put("configId", configId);

        return findBySQL(sql,params);
    }

    public boolean updataAbilityConfig(String configId, String abiDescribe){


        if (StringUtils.isNoneBlank(configId)) {
            Optional<AbilityConfig> ac = abilityConfigRepository.findById(configId);
            AbilityConfig abilityConfig = new AbilityConfig();

            abilityConfig.setConfigId(ac.get().getConfigId());
            abilityConfig.setType(ac.get().getType());
            abilityConfig.setFinancialAbility(ac.get().getFinancialAbility());
            abilityConfig.setDebitAbility(ac.get().getDebitAbility());
            abilityConfig.setBusinessAbility(ac.get().getBusinessAbility());
            abilityConfig.setConpanyYearType(ac.get().getConpanyYearType());
            abilityConfig.setAbilityDescribe(abiDescribe);
            abilityConfig.setCreateTime(ac.get().getCreateTime());
            Date date = new Date();
            Timestamp updateTime = new Timestamp(date.getTime());
            abilityConfig.setUpdateTime(updateTime);

            abilityConfigRepository.save(abilityConfig);
            return true;

        }else{
            return false;
        }



    }

}

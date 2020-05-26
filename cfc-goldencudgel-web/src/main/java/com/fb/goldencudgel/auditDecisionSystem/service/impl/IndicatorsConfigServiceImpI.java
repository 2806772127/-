package com.fb.goldencudgel.auditDecisionSystem.service.impl;

import com.comwave.core.data.domain.QueryPage;
import com.fb.goldencudgel.auditDecisionSystem.utils.UserUtil;
import com.fb.goldencudgel.auditDecisionSystem.model.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import com.comwave.core.jpa.BaseJpaDAO;
import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class IndicatorsConfigServiceImpI extends BaseJpaDAO {

    public QueryPage<Object[]> findByConditions(String type, String industryType, String establishYear, String startDate, String endDate
            , QueryPage<Object[]> queryPage, String startDates, String endDates) {
        //查询
        Map<String,Object> params =  new HashMap<String,Object>();
        StringBuffer sql = new StringBuffer("SELECT CONFIG_ID");
        sql.append(" ,(CASE WHEN POWER_TYPE = '01' THEN '營收成長率' WHEN POWER_TYPE = '02' THEN '存積營收比' WHEN POWER_TYPE = '03' THEN '營授比' END) POWER_TYPE_NAME ");
        sql.append(" ,(CASE WHEN INDUSTRY_TYPE = '01' THEN '批發業' WHEN INDUSTRY_TYPE = '02' THEN '零售業' WHEN INDUSTRY_TYPE = '03' THEN '營造業'  WHEN INDUSTRY_TYPE = '04' THEN '服務業' WHEN INDUSTRY_TYPE = '05' THEN '製造業' WHEN INDUSTRY_TYPE = '06' THEN '旅遊業' WHEN INDUSTRY_TYPE = '99' THEN '其他' END) INDUSTRY_TYPE_NAME ");
        sql.append(" ,(CASE WHEN CONPANY_YEAR_TYPE = '01' THEN '1-5年' WHEN CONPANY_YEAR_TYPE = '02' THEN '6-10年' WHEN CONPANY_YEAR_TYPE = '03' THEN '10年以上' END) CONPANY_YEAR_TYPE_NAME ");
        sql.append(" ,RATE,DATE_FORMAT(CREATE_TIME,'%Y-%m-%d') CREATE_TIME ");
        sql.append(" ,DATE_FORMAT(UPDATE_TIME,'%Y-%m-%d') UPDATE_TIME ");
        sql.append(" FROM COMPANY_INDICATORS_CONFIG ");
        sql.append(" WHERE 1=1 ");

        if (StringUtils.isNoneBlank(type)){
            sql.append(" AND POWER_TYPE=:type");
            params.put("type",type);
        }
        if (StringUtils.isNoneBlank(industryType)){
            sql.append(" AND INDUSTRY_TYPE=:industryType");
            params.put("industryType",industryType);
        }
        if (StringUtils.isNoneBlank(establishYear)){
            sql.append(" AND CONPANY_YEAR_TYPE=:establishYear ");
            params.put("establishYear",establishYear);
        }
        if (StringUtils.isNoneBlank(startDate)){
            sql.append(" AND Date(CREATE_TIME) >= :startDate");
            params.put("startDate",startDate);
        }
        if (StringUtils.isNoneBlank(endDate)){
            sql.append(" AND Date(CREATE_TIME) <= :endDate");
            params.put("endDate",endDate);
        }
        if (StringUtils.isNoneBlank(startDates)){
            sql.append(" AND Date(UPDATE_TIME) >= :startDates");
            params.put("startDates",startDates);
        }
        if (StringUtils.isNoneBlank(endDates)){
            sql.append(" AND Date(UPDATE_TIME) <= :endDates");
            params.put("endDates",endDates);
        }
        sql.append(" ORDER BY POWER_TYPE,INDUSTRY_TYPE,CONPANY_YEAR_TYPE ");
        return findBySQL(sql,queryPage,params);
        
    }
    
    
    public QueryPage<Object[]> lookIndicatorsConfig(String configId){
        Map<String,Object> params =  new HashMap<String,Object>();
        StringBuffer sql = new StringBuffer("SELECT CONFIG_ID, (CASE WHEN POWER_TYPE = '01' THEN '營收成長率' WHEN POWER_TYPE = '02' THEN '存積營收比' WHEN POWER_TYPE = '03' THEN '營授比' END) POWER_TYPE, (CASE WHEN INDUSTRY_TYPE = '01' THEN '製造業' WHEN INDUSTRY_TYPE = '02' THEN '批發業' WHEN INDUSTRY_TYPE = '03' THEN '服務業'  WHEN INDUSTRY_TYPE = '04' THEN '零售業' WHEN INDUSTRY_TYPE = '05' THEN '營建營造業' WHEN INDUSTRY_TYPE = '06' THEN '旅遊業' WHEN INDUSTRY_TYPE = '99' THEN '其他' END) INDUSTRY_TYPE, (CASE WHEN CONPANY_YEAR_TYPE = '01' THEN '1-5年' WHEN CONPANY_YEAR_TYPE = '02' THEN '6-10年' WHEN CONPANY_YEAR_TYPE = '03' THEN '10年以上' END) CONPANY_YEAR_TYPE,  RATE,DATE_FORMAT(CREATE_TIME,'%Y-%m-%d') CREATE_TIME,DATE_FORMAT(UPDATE_TIME,'%Y-%m-%d') UPDATE_TIME  FROM COMPANY_INDICATORS_CONFIG ");
    	sql.append(" WHERE CONFIG_ID = :configId");
    	params.put("configId", configId);
        return findBySQL(sql,params);
    	  
    }
    
    
    public QueryPage<Object[]> findById(String configId,QueryPage<Object[]> queryPage) {
        //查询
        Map<String,Object> params =  new HashMap<String,Object>();
        /*StringBuffer sql = new StringBuffer("SELECT * FROM COMPANY_INDICATORS_CONFIG where CONFIG_ID=:configid");*/
        StringBuffer sql = new StringBuffer("SELECT CONFIG_ID, (CASE WHEN POWER_TYPE = '01' THEN '營收成長率' WHEN POWER_TYPE = '02' THEN '存積營收比' WHEN POWER_TYPE = '03' THEN '營授比' END) POWER_TYPE, (CASE WHEN INDUSTRY_TYPE = '01' THEN '批發業' WHEN INDUSTRY_TYPE = '02' THEN '零售業' WHEN INDUSTRY_TYPE = '03' THEN '營造業'  WHEN INDUSTRY_TYPE = '04' THEN '服務業' WHEN INDUSTRY_TYPE = '05' THEN '製造業' WHEN INDUSTRY_TYPE = '06' THEN '旅遊業' WHEN INDUSTRY_TYPE = '99' THEN '其他' END) INDUSTRY_TYPE, (CASE WHEN CONPANY_YEAR_TYPE = '01' THEN '1-5年' WHEN CONPANY_YEAR_TYPE = '02' THEN '6-10年' WHEN CONPANY_YEAR_TYPE = '03' THEN '10年以上' END) CONPANY_YEAR_TYPE,  RATE,DATE_FORMAT(CREATE_TIME,'%Y-%m-%d') CREATE_TIME,DATE_FORMAT(UPDATE_TIME,'%Y-%m-%d') UPDATE_TIME  FROM COMPANY_INDICATORS_CONFIG");
        sql.append(" WHERE CONFIG_ID = :configId");
        params.put("configId",configId);
        queryPage= findBySQL(sql,queryPage,params);
        return queryPage;
    }
    
    public QueryPage<Object[]> findUpdateById(String configId,QueryPage<Object[]> queryPage) {
        //查询
        Map<String,Object> params =  new HashMap<String,Object>();
        StringBuffer sql = new StringBuffer("SELECT CONFIG_ID,POWER_TYPE,INDUSTRY_TYPE,CONPANY_YEAR_TYPE,RATE,CREATE_TIME FROM COMPANY_INDICATORS_CONFIG where CONFIG_ID=:configId");
       // StringBuffer sql = new StringBuffer("SELECT CONFIG_ID, (CASE WHEN POWER_TYPE = '01' THEN '營收成長率' WHEN POWER_TYPE = '02' THEN '存積營收比' WHEN POWER_TYPE = '03' THEN '營授比' END) POWER_TYPE, (CASE WHEN INDUSTRY_TYPE = '01' THEN '批發業' WHEN INDUSTRY_TYPE = '02' THEN '零售業' WHEN INDUSTRY_TYPE = '03' THEN '營造業'  WHEN INDUSTRY_TYPE = '04' THEN '服務業' WHEN INDUSTRY_TYPE = '05' THEN '製造業' WHEN INDUSTRY_TYPE = '06' THEN '旅遊業' WHEN INDUSTRY_TYPE = '99' THEN '其他' END) INDUSTRY_TYPE, (CASE WHEN CONPANY_YEAR_TYPE = '01' THEN '1-5年' WHEN CONPANY_YEAR_TYPE = '02' THEN '6-10年' WHEN CONPANY_YEAR_TYPE = '03' THEN '10年以上' END) CONPANY_YEAR_TYPE,  RATE,DATE_FORMAT(CREATE_TIME,'%Y-%m-%d') CREATE_TIME  FROM COMPANY_INDICATORS_CONFIG where CONFIG_ID=:configId ");
          
        params.put("configId",configId);
        queryPage= findBySQL(sql,queryPage,params);
        return queryPage;
    }
    

}
